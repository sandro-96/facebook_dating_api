package com.fbd.controller;
import com.fbd.model.FilterOption;
import com.fbd.service.MatchService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Log4j2
@RequestMapping("/match")
public class MatchController {
    @Autowired
    @Lazy
    private MatchService matchService;

    @GetMapping(value = "/filter_option")
    @ApiOperation(value = "Get user filter option")
    public Optional<FilterOption> getFilter(
            @AuthenticationPrincipal UserDetails user) {
        return matchService.getFilter(user.getUsername());
    }
    @GetMapping(value = "/count/liked")
    @ApiOperation(value = "Get count of liked")
    public int countLiked(
            @AuthenticationPrincipal UserDetails user) {
        return matchService.getCountLiked(user.getUsername());
    }
}
