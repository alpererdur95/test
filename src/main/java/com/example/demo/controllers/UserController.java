package com.example.demo.controllers;

import com.example.demo.crypto.MD5Encryption;
import com.example.demo.exeptions.UserNotFoundException;
import com.example.demo.models.*;
import com.example.demo.service.EmailService;
import com.example.demo.service.PrivilegeService;
import com.example.demo.service.PrivilegesListService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    PrivilegeService privilegeService;
    @Autowired
    PrivilegesListService privilegesService;
    @Autowired
    EmailService emailService;
    public static Country country = new Country();
    public static List<String> countryList = country.getCountryList();
    @Autowired
    private HttpServletRequest request;

    @Value("${spring.mail.username}")
    String smtpFrom;


    public static boolean emailValidation(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static boolean cellValidation(String cellNumber) {
        boolean state;
        String convertedCell = "";
        if (cellNumber.equals("") || cellNumber.equals("+90")) {
            state = true;
        } else {
            for (Integer i = 0; i < countryList.size(); i++) {
                if (cellNumber.startsWith(countryList.get(i))) {
                    convertedCell = cellNumber.substring(countryList.get(i).length(), cellNumber.length());
                    break;
                }
            }
            if (convertedCell.matches("[0-9]{10}")) {
                state = true;
            } else {
                state = false;
            }
        }
        return state;
    }

    public ResponseEntity validateAndSaveUser(User user, Boolean isCreate) {
        ApiExeption exeption = new ApiExeption();
        ResponseEntity response;
        if (user.getFirstname().isEmpty()) {
            exeption.setCode("404");
            exeption.setMessage("Lütfen geçerli bir isim giriniz");
            response = ResponseEntity.status(404).body(exeption);
        } else if (user.getLastname().isEmpty()) {
            exeption.setCode("404");
            exeption.setMessage("Lütfen geçerli bir soyisim giriniz");
            response = ResponseEntity.status(404).body(exeption);
        } else if (emailValidation(user.getEmail()) != true) {
            exeption.setCode("404");
            exeption.setMessage("Lütfen Uygun bir Email giriniz");
            response = ResponseEntity.status(404).body(exeption);

        } else if (isCreate && user.getPassword().isEmpty()) {
            exeption.setCode("404");
            exeption.setMessage("Lütfen şifre Oluşturunuz");
            response = ResponseEntity.status(404).body(exeption);
        } else if (cellValidation(user.getCell()) != true) {
            exeption.setCode("404");
            exeption.setMessage("Lütfen uygun telefon no giriniz");
            response = ResponseEntity.status(404).body(exeption);
        }

        else {
            User persistedUser = userService.saveUser(user);
            response=ResponseEntity.ok(persistedUser);

            if (!user.getPassword().isEmpty()) {
                userService.updatePaswoord(MD5Encryption.encrypt(user.getPassword()), user.getUid());
                String adminEmail = (String) request.getSession().getAttribute("username");
                if (emailValidation(adminEmail)) {
                    Email email =new Email();
                    email.setTo(adminEmail);
                    email.setFrom(smtpFrom);
                    email.setSubject("Üye Güncelleme");

                    new Thread(() -> {
                        try {
                            emailService.sendSimpleMessage(email,user);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();


                }

            }

        }
        return response;
    }

    public ResponseEntity priviligeValidation(ArrivalPrivilege arrivalPrivilege) {
        ApiExeption exeption = new ApiExeption();
        ResponseEntity responseEntity = null;
        if (arrivalPrivilege.getPrivilegesLists().isEmpty() && arrivalPrivilege.getResourceName().isEmpty())
            responseEntity = null;
        else {
            if (arrivalPrivilege.getPrivilegesLists().isEmpty()) {
                exeption.setCode("404");
                exeption.setMessage("Lütfen tablodan en az 1 adet yetki seçininiz");
                responseEntity = ResponseEntity.status(404).body(exeption);
            } else if (arrivalPrivilege.getResourceName().isEmpty()) {
                exeption.setCode("404");
                exeption.setMessage("Lütfen geçerli bir resource giririniz");
                responseEntity = ResponseEntity.status(404).body(exeption);
            } else {
                for (Integer i = 0; i < arrivalPrivilege.getPrivilegesLists().size(); i++) {
                    try {
                        if (privilegeService.getPrivileges(arrivalPrivilege.getUid(), arrivalPrivilege.getResourceName(), arrivalPrivilege.getPrivilegesLists().get(i))) {
                            Privilege privilege = new Privilege();
                            privilege.setUid(arrivalPrivilege.getUid());
                            privilege.setPrivilegename(arrivalPrivilege.getPrivilegesLists().get(i));
                            privilege.setResourcename(arrivalPrivilege.getResourceName());

                            responseEntity = ResponseEntity.ok(privilegeService.addPrivilege(privilege));
                        }
                    } catch (UserNotFoundException e) {
                        e.printStackTrace();
                        responseEntity = null;
                    }
                }
            }
        }
        return responseEntity;
    }

    @GetMapping("/user")
    public Iterable<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/privilege")
    public Integer getPrivilege() {
        Integer count = 0;
        Iterable<PrivilegesList> priList = privilegesService.getAllPrivileges();
        for (PrivilegesList priligelist : priList) {
            count++;
        }
        return count;
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable(value = "id") Integer uid) {
        User user = null;
        try {
            user = userService.getUser(uid);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }

    @DeleteMapping("/user/delete/{id}")
    public void DeleteUser(@PathVariable(value = "id") Integer uid) {
        User user = null;
        try {
            user = userService.getUser(uid);
            user.setIs_deleted(true);
            userService.saveUser(user);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        privilegeService.userDeleteallPrivilege(uid);
    }

    @PostMapping("/user")
    public ResponseEntity addUser(@RequestBody User responseUser) {
        return validateAndSaveUser(responseUser, true);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity userUpdate(@PathVariable(value = "id") Integer uid, @RequestBody User responseUser) {
        return validateAndSaveUser(responseUser, false);
    }

    @PostMapping("/addPrivilege")
    public ResponseEntity addPrivilege(@RequestBody ArrivalPrivilege responsePrivilige) {
        return priviligeValidation(responsePrivilige);

    }

    @GetMapping("/getPrivilege/{id}")
    public Iterable<Privilege> getUserPrivilege(@PathVariable(value = "id") Integer uid) {
        return privilegeService.getPrivilege(uid);
    }

    @DeleteMapping("/deletePrivilege/{id}")
    public void deletePrivilege(@PathVariable(value = "id") Integer uid) {
        privilegeService.deletePrivilege(uid);

    }


}
