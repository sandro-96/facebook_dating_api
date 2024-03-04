package com.fbd.service;

import com.fbd.dto.OauthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class OauthTokenService {

    private final RestTemplate restTemplate;


    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUrl;

    public OauthTokenService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public OauthResponse fetchToken(String code) {
        final String uri = "https://oauth2.googleapis.com/token?access_type=offline&prompt=consent";
        String redirectUrl = googleRedirectUrl;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("code", code);
        requestBody.add("client_id", CLIENT_ID);
        requestBody.add("client_secret", CLIENT_SECRET);

        requestBody.add("grant_type", "authorization_code");
        requestBody.add("redirect_uri", redirectUrl);
//        requestBody.add("scope", scope);
        requestBody.add("access_type", "offline");
        requestBody.add("prompt", "consent");

        HttpEntity formEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<OauthResponse> response = restTemplate.exchange(uri, HttpMethod.POST, formEntity, OauthResponse.class);
        return response.getBody();
    }
}
