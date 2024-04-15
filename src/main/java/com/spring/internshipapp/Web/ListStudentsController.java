package com.spring.internshipapp.Web;

import com.spring.internshipapp.Model.Company;
import com.spring.internshipapp.Model.Internship;
import com.spring.internshipapp.Model.Student;
import com.spring.internshipapp.Service.CompanyService;
import com.spring.internshipapp.Service.InternshipService;
import com.spring.internshipapp.Service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ListStudentsController {

    private final StudentService studentService;
    private final InternshipService internshipService;
    private final CompanyService companyService;

    public ListStudentsController(StudentService studentService, InternshipService internshipService, CompanyService companyService) {
        this.studentService = studentService;
        this.internshipService = internshipService;
        this.companyService = companyService;
    }

    @GetMapping({"/students"})
    public String showList(@RequestParam(required = false) String error,
                           HttpServletRequest req,
                           Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        String username = req.getRemoteUser();
        List<Student> students = this.studentService.getAllByCoordinator(username);
        List<Company> companies=this.companyService.getAllCompanies();
        List<Internship> internships=this.internshipService.getAllInternships();
        model.addAttribute("students", students);
        model.addAttribute("companies", companies);
        model.addAttribute("internships", internships);
        return "students.html";
    }

    @GetMapping({"/students-company"})
    public String getStudentsByCompany(@RequestParam(required = false) String error,
                                      HttpServletRequest req,
                                      Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        String username = req.getRemoteUser();
        List<Student> students = this.studentService.getAllByCompany(username);
        model.addAttribute("students", students);

        return "students-company.html";
    }

    @PostMapping("/students/{id}/delete")
    public String delete(@PathVariable Long id) {
        this.studentService.delete(id);
        return "redirect:/students";
    }

}
