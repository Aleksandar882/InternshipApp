package com.spring.internshipapp.Web;

import com.spring.internshipapp.Model.*;
import com.spring.internshipapp.Service.CompanyService;
import com.spring.internshipapp.Service.InternshipService;
import com.spring.internshipapp.Service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class InternshipController {

    private final InternshipService internshipService;
    private final CompanyService companyService;
    private final StudentService studentService;


    public InternshipController(InternshipService internshipService, CompanyService companyService, StudentService studentService) {
        this.internshipService = internshipService;
        this.companyService = companyService;
        this.studentService = studentService;
    }

    private Student getAuthenticatedStudent(User currentUserPrincipal) {
        return studentService.getStudentWithCv(currentUserPrincipal.getId())
                .orElseThrow(() -> new IllegalStateException("Student record not found for authenticated user."));
    }

    @GetMapping({"/", "/internships"})
    public String showList(Model model) {
        List<Internship> internships;
        internships = this.internshipService.getAllInternships();
        List<Company> companies = this.companyService.getAllCompanies();
        model.addAttribute("internships", internships);
        model.addAttribute("companies", companies);
        return "list.html";
    }

    @GetMapping("/internships/add")
    public String showAdd(Model model) {
        List<Company> companies = this.companyService.getAllCompanies();
        model.addAttribute("companies", companies);
        return "form.html";
    }

    @GetMapping("/internships/{id}/edit")
    public String showEdit(@PathVariable Long id, Model model) {
        Internship internship = this.internshipService.getInternshipById(id);
        List<Company> companies = this.companyService.getAllCompanies();
        model.addAttribute("internship", internship);
        model.addAttribute("companies", companies);
        return "form.html";
    }

    @PostMapping("/internships/")
    public String create(@RequestParam String position,
                         @RequestParam String location,
                         @RequestParam String description,
                         @RequestParam String timePeriod,
                         @RequestParam Long companyId

    ) {
        this.internshipService.addNewInternship(position, location, description, timePeriod, companyId);
        return "redirect:/internships";
    }

    @PostMapping("/internships/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String position,
                         @RequestParam String location,
                         @RequestParam String description,
                         @RequestParam String timePeriod,
                         @RequestParam Long companyId

    ) {
        this.internshipService.updateInternship(id, position, location, description, timePeriod, companyId);
        return "redirect:/internships";
    }

    @PostMapping("/internships/{id}/delete")
    public String delete(@PathVariable Long id) {
        this.internshipService.delete(id);
        return "redirect:/internships";
    }

    @GetMapping({"/internships-company"})
    public String getInternshipsByCompany(@RequestParam(required = false) String error,
                                          HttpServletRequest req,
                                          Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        String username = req.getRemoteUser();
        List<Internship> internships = this.internshipService.getAllByCompany(username);
        List<Company> companies = this.companyService.getAllCompanies();
        model.addAttribute("internships", internships);
        model.addAttribute("companies", companies);
        return "list-for-company.html";
    }

    @GetMapping("/internships-company/{id}/edit")
    public String showEditFromCompany(@PathVariable Long id, Model model) {
        Internship internship = this.internshipService.getInternshipById(id);
        List<Company> companies = this.companyService.getAllCompanies();
        model.addAttribute("internship", internship);
        model.addAttribute("companies", companies);
        return "form-from-company.html";
    }

    @PostMapping("/internships-company/{id}")
    public String updateFromCompany(@PathVariable Long id,
                                    @RequestParam String position, String error,
                                    HttpServletRequest req, Model model,
                                    @RequestParam String location,
                                    @RequestParam String description,
                                    @RequestParam String timePeriod) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        String username = req.getRemoteUser();
        {
            Company company=this.companyService.getCompanyByName(username);
            Long companyId= company.getId();
            this.internshipService.updateInternship(id,position, location, description, timePeriod, companyId);
            return "redirect:/internships-company";
        }
    }

    @PostMapping("/internships-company/{id}/delete")
    public String deleteFromCompany(@PathVariable Long id) {
        this.internshipService.delete(id);
        return "redirect:/internships-company";
    }

    @PostMapping("/internships-company/")
    public String createFromCompany(@RequestParam String position, String error,
                                    HttpServletRequest req, Model model,
                                    @RequestParam String location,
                                    @RequestParam String description,
                                    @RequestParam String timePeriod) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        String username = req.getRemoteUser();
        {
            Company company=this.companyService.getCompanyByName(username);
            Long companyId= company.getId();
            this.internshipService.addNewInternship(position, location, description, timePeriod, companyId);
            return "redirect:/internships-company";
        }
    }
    @GetMapping("/internships-company/add")
    public String showAddFromCompany(Model model) {
        List<Company> companies = this.companyService.getAllCompanies();
        model.addAttribute("companies", companies);
        return "form-from-company.html";
    }

    @GetMapping("/student/current-internship")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public String viewCurrentInternship(Model model, @AuthenticationPrincipal User currentUserPrincipal) {
        if (currentUserPrincipal == null) {
            return "redirect:/login";
        }
        try {
            Student student = getAuthenticatedStudent(currentUserPrincipal);

            if (student.getApplicationStatus() == ApplicationStatus.ACCEPTED && student.getInternship() != null) {
                model.addAttribute("currentInternship", student.getInternship());
                model.addAttribute("pageTitle", "Тековна Пракса");
            } else {
                model.addAttribute("currentInternship", null);
                model.addAttribute("pageTitle", "Тековна Пракса");
                model.addAttribute("infoMessage", "Моментално немате активна пракса.");
            }
            return "current-internship-view.html";
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", "Грешка при вчитување на податоците за студентот.");
            return "error";
        }
    }
}
