package com.mycompany.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserService service;

    @Autowired
    public UserController(UserService userService){
        this.service = userService;
    }

    @GetMapping("/users")
    public String showUserList(Model model) {
        List<User> listUsers = service.listAll();
        model.addAttribute("listUsers", listUsers);
        return "users";
    }

    @GetMapping("/users/view")
    public String showUserDetails(@RequestParam("id") Integer userId, Model model) {
        Optional<User> userOptional = service.getUserDetailsById(userId);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            model.addAttribute("user",user);
            return "user_details";
        }
        else{
            return "user_not_found";
        }
    }

    @GetMapping("/users/new")
    public String showNewForm(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("pageTitle", "Add New User");
        return "user_form";
    }
    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute User user, RedirectAttributes ra) throws UserNotFoundException {
        service.save(user);
        ra.addFlashAttribute("message","The user has been saved successfully.");
        return "redirect:/users";
    }
    @GetMapping("/users/edit/{id}")
    public String showEditForm(@PathVariable("id")Integer id, Model model, RedirectAttributes ra) {
        try {
            User user = service.get(id);
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Edit User (ID" + id + ")");
            return "user_form";
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/users";
        }
    }
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id")Integer id, RedirectAttributes ra) {
        try {
            service.delete(id);

        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());

        }
        return "redirect:/users";


    }
    @GetMapping("/")
    public String showMainPage(){
        return "index";
    }


}
