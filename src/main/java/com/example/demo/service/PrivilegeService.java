package com.example.demo.service;

import com.example.demo.exeptions.UserNotFoundException;
import com.example.demo.models.Privilege;
import com.example.demo.repository.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrivilegeService {
    @Autowired
    PrivilegeRepository privilegeRepository;


    public Privilege addPrivilege(Privilege privilege) {
        return privilegeRepository.save(privilege);
    }

    public boolean getPrivileges(Integer uid, String resourcename, String privilegename) throws UserNotFoundException {
        List<Privilege> currentUserPrivilege = new ArrayList<>();
        currentUserPrivilege = privilegeRepository.findByUidAndResourcenameAndPrivilegename(uid, resourcename, privilegename);
        if (currentUserPrivilege.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public Iterable<Privilege> getPrivilege(Integer uid) {
        return privilegeRepository.findAllByUid(uid);
    }

    public void deletePrivilege(Integer id) {
        privilegeRepository.deleteById(id);
    }

    public void userDeleteallPrivilege(Integer uid) {
        Iterable<Privilege> allPrivilege = privilegeRepository.findAllByUid(uid);
        privilegeRepository.deleteAll(allPrivilege);
    }
}
