package com.spring.internshipapp.Web;

import com.spring.internshipapp.Model.*;
import com.spring.internshipapp.Service.StudentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/internship-add")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping({"/success"})
    public String showList(Model model) {
        return "success.html";
    }

    @PostMapping("/add-internship/{id}")
    public String addInternship(@PathVariable Long id,Authentication authentication) {
            try {
                this.studentService.addInternship(authentication.getName(), id);
                return "redirect:/internship-add/success";
            } catch (RuntimeException exception) {
                return "redirect:/internship-add/success?error=" + exception.getMessage();
            }

    }
}
