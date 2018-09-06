package com.example.demo.repository;

import com.example.demo.models.Privilege;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PrivilegeRepository extends CrudRepository<Privilege,Integer> {
    List<Privilege> findByUidAndResourcenameAndPrivilegename(Integer uid,String resourcename,String privilegename);
    Iterable<Privilege> findAllByUid(Integer Uid);
    void deleteAllByUid(Integer uid);
}
