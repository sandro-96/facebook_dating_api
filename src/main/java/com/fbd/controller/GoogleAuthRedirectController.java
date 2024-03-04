package com.fbd.controller;

import com.fbd.dto.LoginDTO;
import com.fbd.dto.UserDTO;
import com.fbd.service.GoogleAuthenticateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;


@Controller
public class GoogleAuthRedirectController {
    @Value("${app.fe.url}")
    private String feDomain;

    @Autowired
    @Lazy
    private GoogleAuthenticateService googleAuthenticateService;

    @RequestMapping("/oauth2/callback/google-sandro")
    public RedirectView googleCallbackUrl(HttpServletRequest request) {
        return googleCallbackUrl(request, feDomain);
    }

    public RedirectView googleCallbackUrl(HttpServletRequest request, String domain) {
        String authorizationCode = request.getParameter("code");
        RedirectView redirectView = new RedirectView();
        String callBackPath;
        callBackPath = "/callBack?authorizationCode=";
        redirectView.setUrl(domain + callBackPath + authorizationCode);
        return redirectView;
    }

    @RequestMapping("/oauth2/google/login/process")
    @ApiOperation(
            value = "Google login process"
    )
    public @ResponseBody ResponseEntity googleLoginProcess(@RequestParam String authorizationCode) {
        return loginProcess(authorizationCode);
    }

    public ResponseEntity loginProcess(String authorizationCode) {
        final UserDTO userDTO;
        try {
            userDTO = googleAuthenticateService.authenticateAccessToken(authorizationCode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (userDTO == null) {
            throw new RuntimeException("Authorization from google failed");
        }
        return getLoginDTOResponseEntity(userDTO);
    }

    static ResponseEntity<LoginDTO> getLoginDTOResponseEntity(UserDTO userDTO) {
        return ResponseEntity.ok(LoginDTO.builder()
                .user(userDTO.getUser())
                .jwtToken(userDTO.getJWTToken())
                .build());
    }
}
