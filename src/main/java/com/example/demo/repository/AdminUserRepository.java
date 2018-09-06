package com.example.demo.repository;

import com.example.demo.models.AdminUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserRepository extends CrudRepository<AdminUser,Integer> {
 AdminUser findByPasswordAndUserEmail(String password,String email);
}
