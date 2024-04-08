package com.spring.internshipapp.Web;

import com.spring.internshipapp.Model.Exceptions.InvalidUserCredentialsException;
import com.spring.internshipapp.Model.User;
import com.spring.internshipapp.Service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;


    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String getLoginPage() {
        return "login.html";
    }

    @PostMapping
    public String login(HttpServletRequest request, Model model) {
        User user = null;
        try{
            user = this.authService.login(request.getParameter("email"),
                    request.getParameter("password"));
            request.getSession().setAttribute("user", user);
            return "redirect:/internships";
        }
        catch (InvalidUserCredentialsException exception) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", exception.getMessage());
            return "login";
        }
    }
}
