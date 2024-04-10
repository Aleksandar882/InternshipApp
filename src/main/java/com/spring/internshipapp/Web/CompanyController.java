package com.spring.internshipapp.Web;


import com.spring.internshipapp.Model.Company;
import com.spring.internshipapp.Model.Internship;
import com.spring.internshipapp.Service.CompanyService;
import com.spring.internshipapp.Service.InternshipService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CompanyController {

    private final CompanyService companyService;
    private final InternshipService internshipService;

    public CompanyController(CompanyService companyService, InternshipService internshipService) {
        this.companyService = companyService;
        this.internshipService = internshipService;
    }


    @GetMapping({"/companies"})
    public String showList(Model model) {
        List<Internship> internships;
        internships = this.internshipService.getAllInternships();
        List<Company> companies=this.companyService.getAllCompanies();
        model.addAttribute("internships", internships);
        model.addAttribute("companies", companies);
        return "companies.html";
    }
}
