package com.fbd.config;

import com.fbd.model.User;
import com.fbd.mongo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Value("${frontend.url}")
    private String frontendUrl;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = principal.getAttributes();
        String email = attributes.get("email").toString();
        String name = attributes.get("name").toString();
        String picture = attributes.get("picture").toString();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            DefaultOAuth2User newUser = new DefaultOAuth2User(Arrays.asList(new SimpleGrantedAuthority(user.get().getRole().name())),
                    attributes, "email");
            Authentication authentication1 = new OAuth2AuthenticationToken(newUser,
                    Arrays.asList(new SimpleGrantedAuthority(user.get().getRole().name())), token.getAuthorizedClientRegistrationId());
            SecurityContextHolder.getContext().setAuthentication(authentication1);
        } else {
            User user1 = new User();
            user1.setEmail(email);
            user1.setName(name);
            user1.setAvatarUrl(picture);
            userRepository.save(user1);

            DefaultOAuth2User newUser = new DefaultOAuth2User(Arrays.asList(new SimpleGrantedAuthority(user1.getRole().name())),
                    attributes, "email");
            Authentication authentication1 = new OAuth2AuthenticationToken(newUser,
                    Arrays.asList(new SimpleGrantedAuthority(user1.getRole().name())), token.getAuthorizedClientRegistrationId());
            SecurityContextHolder.getContext().setAuthentication(authentication1);
        }

        String callBackPath ="/callBack?authorizationCode=";
        String authorizationCode = request.getParameter("code");
        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl(frontendUrl + callBackPath + authorizationCode);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
