package com.example.demo.service;

import com.example.demo.models.PrivilegesList;
import com.example.demo.repository.PrivilegesListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrivilegesListService {
    @Autowired
    PrivilegesListRepository privilegesRepository;
    public Iterable<PrivilegesList> getAllPrivileges(){
        return privilegesRepository.findAll();
    }
}
