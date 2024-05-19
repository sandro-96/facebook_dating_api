package com.fbd.controller;

import com.fbd.model.User;
import com.fbd.service.UserApiServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserApiServiceImpl userApiService;

    @GetMapping(value = "/list")
    public Page<User> list(@AuthenticationPrincipal UserDetails user,
                           @RequestParam(required = false) String page,
                           @RequestParam(required = false) String size
                           ) {
        return userApiService.list(Pageable.ofSize(Integer.parseInt(size)).withPage(Integer.parseInt(page)), user.getUsername());
    }

    @GetMapping(value = "/likedList")
    public List<User> likedList(@AuthenticationPrincipal UserDetails user) {
        return userApiService.likedList(user.getUsername());
    }
    @PatchMapping(value = "/location/update")
    public User locationUpdate(@AuthenticationPrincipal UserDetails user,
                                 @RequestParam double latitude,
                                    @RequestParam double longitude) {
        log.info("User {} location updated to {}, {}", user.getUsername(), latitude, longitude);
        return userApiService.locationUpdate(user.getUsername(), latitude, longitude);
    }

    @GetMapping("/nearbyUsers")
    public List<User> findNearbyUsers(@RequestParam double longitude, @RequestParam double latitude) {
        return userApiService.findNearbyUsers(longitude, latitude);
    }
}