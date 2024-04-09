package com.fbd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class GoogleController {

    @RequestMapping("/oauth2/callback/google")
    public boolean googleCallbackUrlBackOffice(HttpServletRequest request) {
        return true;
    }
}