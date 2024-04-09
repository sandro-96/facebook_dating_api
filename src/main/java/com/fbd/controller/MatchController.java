package com.fbd.controller;

import com.fbd.model.FilterOption;
import com.fbd.service.MatchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/match")
public class MatchController {
    @Autowired
    private MatchServiceImpl matchService;

    @GetMapping(value = "/filter_option")
    public Optional<FilterOption> getFilter(@AuthenticationPrincipal UserDetails user) {
        return matchService.getFilter(user.getUsername());
    }

    @GetMapping(value = "/count/liked")
    public int countLiked(@AuthenticationPrincipal UserDetails user) {
        return matchService.getCountLiked(user.getUsername());
    }
}