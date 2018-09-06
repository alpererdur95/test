package com.example.demo.controllers;

import com.example.demo.models.Country;
import com.example.demo.models.PrivilegesList;
import com.example.demo.models.User;
import com.example.demo.service.PrivilegeService;
import com.example.demo.service.PrivilegesListService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/")
public class MainController {
    @Autowired
    UserService userService;
    @Autowired
    PrivilegeService privilegeService;
    @Autowired
    PrivilegesListService privilegesService;
    @Autowired
    private HttpServletRequest request;
    private static final Integer limit = 5;
    Country country = new Country();
    List<String> countryList = country.getCountryList();


    @RequestMapping(method = RequestMethod.GET)
    public String home() {


        return "redirect:login";
    }
    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        if (request.getSession(false) != null) {

            return new ModelAndView("redirect:users");
        } else {
            modelAndView.setViewName("login.html");
            return modelAndView;
        }

    }

    @GetMapping("/createuser")
    public ModelAndView CreateUser() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("countryCode", countryList);
        modelAndView.setViewName("createuser");
        return modelAndView;
    }

    @GetMapping("/users")
    public ModelAndView GetUser(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "search", required = false) String search) {
        Integer count;
        List<User> userList;
        ModelAndView modelAndView = new ModelAndView();
        String queryString = search == null ? " " : search;
        List<User> searchUserList = userService.findSearchUser(queryString, page, limit);

        Iterable<PrivilegesList> privilegelist = privilegesService.getAllPrivileges();
        modelAndView.addObject("countryCode", countryList);
        modelAndView.addObject("message", "Aramanıza uyan kimse bulanamamıştır");
        count = userService.getCount(queryString);
        modelAndView.addObject("pageCount", count);
        modelAndView.addObject("users", searchUserList);
        modelAndView.setViewName("user");
        modelAndView.addObject("privileges", privilegelist);

        return modelAndView;
    }
}
