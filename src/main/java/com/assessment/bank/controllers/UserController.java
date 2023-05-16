package com.assessment.bank.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.assessment.bank.entities.User;
import com.assessment.bank.service.UserServices;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserServices userService;

    @Autowired
    public UserController(UserServices userService) {
        this.userService = userService;
    }

    @GetMapping("/add")
    public String showAddUserForm() {
        return "add-user";
    }

    @PostMapping("/add")
    public String addUser(@RequestParam("name") String name,
                          @RequestParam("email") String email,
                          @RequestParam("password") String password,
                          @RequestParam("roles") String roles,
                          Model model) {
        try {
            User newUser = new User(name, email, password, roles);
            userService.saveNewUser(newUser);
            return "redirect:/users/list";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to add user: " + e.getMessage());
            return "add-user";
        }
    }

    @GetMapping("/list")
    public String userList(Model model) {
        List<User> users = userService.listUsers();
        model.addAttribute("users", users);
        return "list-users";
    }
}