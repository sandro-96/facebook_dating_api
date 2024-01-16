package com.fbd.service;

import com.fbd.model.Group;
import com.fbd.mongo.GroupRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class GroupService {
    @Autowired
    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group joinGroup(String id, String userId) {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isPresent()) {
            List<String> members = group.get().getMemberIds();
            members.add(userId);
            group.get().setMemberIds(members);
            return groupRepository.save(group.get());
        } else throw new RuntimeException("Fail to join group");
    }

    public Group removeMember(String id, String userId) {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isPresent()) {
            List<String> members = group.get().getMemberIds();
            members.remove(userId);
            group.get().setMemberIds(members);
            return groupRepository.save(group.get());
        } else throw new RuntimeException("Fail to remove member");
    }

    public List<Group> list() {
        return groupRepository.findAll();
    }
}
