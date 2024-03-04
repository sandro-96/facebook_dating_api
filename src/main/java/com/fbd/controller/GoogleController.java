package com.fbd.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Controller
@Log4j2
public class GoogleController {

    @RequestMapping("/oauth2/callback/google")
    public boolean googleCallbackUrlBackOffice(HttpServletRequest request) {
        return true;
    }
}
