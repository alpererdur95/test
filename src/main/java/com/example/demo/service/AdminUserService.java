package com.example.demo.service;

import com.example.demo.models.AdminUser;
import com.example.demo.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminUserService {
    @Autowired
    AdminUserRepository adminUserRepository;

    public Boolean checkAdmin(AdminUser admin) {

        if ((adminUserRepository.findByPasswordAndUserEmail(admin.getPassword(), admin.getUserEmail()) != null)) {
            return true;
        } else return false;
    }
    public Boolean checkPassOrEmail(AdminUser adminUser){
AdminUser adminUser1 =adminUserRepository.findByPasswordAndUserEmail(adminUser.getPassword(),adminUser.getUserEmail());
if (adminUser1!=null){
    return true;
}
else return false;


    }
}