package com.fbd.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;


@Controller
public class GoogleAuthRedirectController {
    @Value("${app.fe.url}")
    private String feDomain;

    @RequestMapping("/oauth2/callback/google")
    public RedirectView googleCallbackUrlBackOffice(HttpServletRequest request) {
        return googleCallbackUrl(request, feDomain);
    }

    public RedirectView googleCallbackUrl(HttpServletRequest request, String domain) {
        String authorizationCode = request.getParameter("code");
        RedirectView redirectView = new RedirectView();
        String callBackPath;
        callBackPath = "/webLoginCallback?authorizationCode=";
        redirectView.setUrl(domain + callBackPath + authorizationCode + "&type=google");
        return redirectView;
    }
}
