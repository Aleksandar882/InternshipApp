package com.spring.internshipapp.Web;

import com.spring.internshipapp.Model.Exceptions.InvalidArgumentsException;
import com.spring.internshipapp.Model.Exceptions.PasswordsDoNotMatchException;
import com.spring.internshipapp.Service.AuthService;
import com.spring.internshipapp.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    private final AuthService authService;
    private final UserService userService;

    public RegisterController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }


    @GetMapping("/register")
    public String getRegisterPage(@RequestParam(required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        return "register.html";
    }


    @PostMapping("/register")
    public String register(@RequestParam Long index,
                           @RequestParam String name,
                           @RequestParam String surname,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String repeatPassword) {
        try {
            this.userService.register(index,name,surname,email, password, repeatPassword);
            return "redirect:/login";
        } catch (InvalidArgumentsException | PasswordsDoNotMatchException exception) {
            return "redirect:/register?error=" + exception.getMessage();
        }
    }

    @GetMapping("/register-company")
    public String getRegisterPageCompany(@RequestParam(required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        return "register-company.html";
    }

    @PostMapping("/register-company")
    public String registerCompany(
                           @RequestParam String name,
                           @RequestParam String description,
                           @RequestParam String address,
                           @RequestParam Integer number,
                           @RequestParam String image_url,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String repeatPassword) {
        try {
            this.userService.registerCompany(name,description,address,number,image_url,email,password,repeatPassword);
            return "redirect:/login";
        } catch (InvalidArgumentsException | PasswordsDoNotMatchException exception) {
            return "redirect:/register-company?error=" + exception.getMessage();
        }
    }

}
