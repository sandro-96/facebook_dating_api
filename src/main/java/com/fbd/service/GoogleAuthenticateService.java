package com.fbd.service;

import com.fbd.dto.OauthResponse;
import com.fbd.dto.UserDTO;
import com.fbd.model.User;
import com.fbd.mongo.MongoUserRepository;
import com.fbd.provider.JwtTokenProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.security.sasl.AuthenticationException;

@Service
@Log4j2
public class GoogleAuthenticateService {

    @Autowired
    @Lazy
    private JwtTokenProvider jwtTokenProvider;

    private final MongoUserRepository mongoUserRepository;

    private final OauthTokenService oauthTokenService;

    public GoogleAuthenticateService(MongoUserRepository mongoUserRepository, OauthTokenService oauthTokenService) {
        this.mongoUserRepository = mongoUserRepository;
        this.oauthTokenService = oauthTokenService;
    }

    public UserDTO authenticateAccessToken(String oathCode) throws Exception {

        OauthResponse oauthResponse;
        oauthResponse = handleGoogleOathCode(oathCode);

        String accessToken = oauthResponse.getAccess_token();
        String refreshToken = oauthResponse.getRefresh_token();

        return checkAndInitGoogleAccountUser(oathCode, accessToken, refreshToken);

    }

    public OauthResponse handleGoogleOathCode(String code) throws Exception {

        final OauthResponse oauthResponse = oauthTokenService.fetchToken(code);
        if (oauthResponse == null || StringUtils.isEmpty(oauthResponse.getAccess_token())) {
            throw new Exception("Invalid token");
        }

        if (StringUtils.isEmpty(code)) throw new Exception("Authorization from google failed");
        return oauthResponse;
    }

    public Userinfo validateAccessToken(String accessToken) {
        try {
            GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

            Oauth2 oauth2 =
                    new Oauth2.Builder(new NetHttpTransport(),
                            new GsonFactory(), credential).setApplicationName(
                                    "Oauth2")
                            .build();
            final Userinfo userinfo = oauth2.userinfo()
                    .get()
                    .execute();
            return userinfo;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private UserDTO checkAndInitGoogleAccountUser(String oathCode, String accessToken, String refreshToken) throws Exception {
        final Userinfo userinfo = validateAccessToken(accessToken);
        if (userinfo == null) throw new AuthenticationException("The access token is invalid");

        User user = null;
        try {
            // Handling Web business
            user = mongoUserRepository.findByEmail(userinfo.getEmail());
        } catch (Exception e) {
            log.error("Error: {}", e);
            log.error(e.getMessage(), e);
            throw new DataAccessResourceFailureException(e.getMessage());
        }
        if (user == null) {

            // create user
            log.info("create user with email " + userinfo.getEmail());

            User newUser = User.builder()
                    .email(userinfo.getEmail())
                    .username(userinfo.getGivenName().concat(userinfo.getFamilyName()))
                    .build();

            user = mongoUserRepository.save(newUser);

            String picture = userinfo.getPicture();
            log.info("User Google Original Picture: " + picture);
        }
        if (user.getUsername() == null) user.setUsername(userinfo.getGivenName().concat(userinfo.getFamilyName()));

        return UserDTO.builder()
                .user(user)
                .JWTToken(jwtTokenProvider.generateToken(user.getId()))
                .build();
    }
}
