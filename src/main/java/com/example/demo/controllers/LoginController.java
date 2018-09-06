package com.example.demo.controllers;

import com.example.demo.crypto.MD5Encryption;
import com.example.demo.models.AdminUser;
import com.example.demo.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class LoginController {
    @Autowired
    AdminUserService adminUserService;

    @Autowired
    private HttpServletRequest request;
public ResponseEntity loginValidation(AdminUser adminUser){
    ResponseEntity responseEntity = null;
    adminUser.setPassword(MD5Encryption.encrypt(adminUser.getPassword()));
   Boolean checkState =adminUserService.checkPassOrEmail(adminUser);
   if (adminUser.getUserEmail().isEmpty()||adminUser.getPassword().isEmpty()){
       responseEntity =ResponseEntity.status(404).body("Email veya şifre boş olamaz");
   }
else if (checkState==false){
       responseEntity =ResponseEntity.status(404).body("Email veya şifre hatalıdır");

   }
else {
    Boolean state;

    state = adminUserService.checkAdmin(adminUser);
    if (state) {
        HttpSession session = request.getSession(true);

        session.setAttribute("username", adminUser.getUserEmail());
        responseEntity=ResponseEntity.ok(adminUser);
    }



}
    return   responseEntity;
}


    @PostMapping("/check")
    public ResponseEntity login(@RequestBody AdminUser admin) {

        return loginValidation(admin);
    }

    @GetMapping("/logout")
        public String logout(){
            HttpSession httpSession = request.getSession();
            httpSession.invalidate();
              return "redirect:/login";
        }
}
