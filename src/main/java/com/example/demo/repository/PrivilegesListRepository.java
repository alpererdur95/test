package com.example.demo.repository;

import com.example.demo.models.PrivilegesList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegesListRepository extends CrudRepository<PrivilegesList,Integer> {
}

