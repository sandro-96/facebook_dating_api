package com.fbd.controller;

import com.fbd.model.CustomUserDetails;
import com.fbd.model.FeedBack;
import com.fbd.model.HelpSupport;
import com.fbd.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setting")
public class SettingController {
    @Autowired
    private SettingService settingService;

    @GetMapping(value = "/help_support/getAllByUser")
    public List<HelpSupport> getAllHelpSupportsById(@AuthenticationPrincipal UserDetails user) {
        return settingService.getAllHelpSupportsById(user.getUsername());
    }

    @GetMapping(value = "/help_support/get/{id}")
    public HelpSupport getHelpSupport(@PathVariable String id) {
        return settingService.getHelpSupport(id);
    }

    @PatchMapping(value = "/help_support/answer/{id}")
    public HelpSupport answerHelpSupport(String answer, @PathVariable String id) {
        return settingService.answerHelpSupport(answer, id);
    }

    @PostMapping("/help_support/create")
    public HelpSupport createHelpSupport(@AuthenticationPrincipal UserDetails user, String question) {
        HelpSupport helpSupport = new HelpSupport();
        helpSupport.setQuestion(question);
        helpSupport.setEmail(((CustomUserDetails) user).getUser().getEmail());
        return settingService.createHelpSupport(helpSupport);
    }

    @PostMapping("/feedback/create")
    public FeedBack createFeedback(String content) {
        FeedBack feedBack = new FeedBack();
        feedBack.setContent(content);
        return settingService.createFeedback(feedBack);
    }

    @GetMapping(value = "/feedback/getAllByUser")
    public List<FeedBack> getAllFeedbackById(@AuthenticationPrincipal UserDetails user) {
        return settingService.getAllFeedbackById(user.getUsername());
    }
}