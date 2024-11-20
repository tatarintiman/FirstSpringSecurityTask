package ru.itmentor.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.service.RoleService;
import ru.itmentor.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminsController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminsController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/mainPage")
    public String getAdminInfo(Principal principal, Model model){
        String username = principal.getName();
        User admin = userService.getCurrentUser(username);

        model.addAttribute("admin", admin);
        return "admin/mainPage";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.findAll();
        List<User> filteredUsers = users.stream()
                .filter(user -> user.getAuthorities()
                        .stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")))
                .collect(Collectors.toList());
        model.addAttribute("users", filteredUsers);
        return "admin/userList";
    }

    @GetMapping("/addUser")
    public String addUserForm(User user){
        return "admin/addUserForm";
    }

    @PostMapping("/addUser")
    public String addUser(User user) {
        Role userRole = roleService.findByName("ROLE_USER");

        if (userRole == null) {
            userRole = new Role();
            userRole.setName("ROLE_USER");
            roleService.save(userRole);
        }

        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }

        user.getRoles().add(userRole);

        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/separateUser/{id}")
    public String getUserInfo(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "admin/separateUser";
    }


    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id){
        userService.deleteById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/updateUser/{id}")
    public String updateUserForm(@PathVariable Long id, Model model){
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "admin/updateUserForm";
    }

    @PostMapping("/updateUser")
    public String updateUser(User user){
        Role userRole = roleService.findByName("ROLE_USER");

        if (userRole == null) {
            userRole = new Role();
            userRole.setName("ROLE_USER");
            roleService.save(userRole);
        }

        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }

        user.getRoles().add(userRole);
        userService.saveUser(user);
        return "redirect:/admin/users";
    }
}
