package com.fbd.controller;

import com.fbd.model.Group;
import com.fbd.service.GroupService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/group")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @GetMapping("/joinGroup/{id}")
    public Group joinGroup(@PathVariable String id, @RequestParam String userId) {
        return groupService.joinGroup(id, userId);
    }

    @GetMapping("/list")
    public List<Group> list() {
        return groupService.list();
    }

    @GetMapping("/removeMember/{id}")
    public Group removeMember(@PathVariable String id, @RequestParam String userId) {
        return groupService.removeMember(id, userId);
    }
}
