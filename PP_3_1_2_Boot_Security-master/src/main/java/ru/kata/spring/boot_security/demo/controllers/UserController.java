package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String welcomePage() {
        return "main";
    }

    @GetMapping("/user")
    public String userPage(Principal principal, Model model) {
        User user = userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allUsers", userService.allUsers());
        model.addAttribute("allRoles", userService.allRoles());
        return "admin";
    }

    @GetMapping("/admin/update/{id}")
    public String updateUserForm(@PathVariable long id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("allRoles", userService.allRoles());
        return "update";
    }

    @PostMapping("/admin/save")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.saveUser(user, userService.getPasswordEncoder());
        return "redirect:/admin";
    }

    @PostMapping("/admin/update")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.saveUser(user, userService.getPasswordEncoder());
        return "redirect:/admin";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}
