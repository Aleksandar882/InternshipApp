package com.spring.internshipapp.Web;

import com.spring.internshipapp.Model.Company;
import com.spring.internshipapp.Model.Internship;
import com.spring.internshipapp.Service.CompanyService;
import com.spring.internshipapp.Service.InternshipService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class InternshipController {

    private final InternshipService internshipService;
    private final CompanyService companyService;


    public InternshipController(InternshipService internshipService, CompanyService companyService) {
        this.internshipService = internshipService;
        this.companyService = companyService;
    }

    @GetMapping({"/","/internships"})
    public String showList(Model model) {
        List<Internship> internships;
        internships = this.internshipService.getAllInternships();
        List<Company> companies=this.companyService.getAllCompanies();
        model.addAttribute("internships", internships);
        model.addAttribute("companies", companies);
        return "list.html";
    }

    @GetMapping("/internships/add")
    public String showAdd(Model model) {
        List<Company> companies=this.companyService.getAllCompanies();
        model.addAttribute("companies", companies);
        return "form.html";
    }

    @GetMapping("/internships/{id}/edit")
    public String showEdit(@PathVariable Long id, Model model) {
        Internship internship= this.internshipService.getInternshipById(id);
        List<Company> companies=this.companyService.getAllCompanies();
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
        this.internshipService.addNewInternship(position, location,  description,  timePeriod,  companyId);
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
        this.internshipService.updateInternship(id,position, location,  description,  timePeriod,  companyId);
        return "redirect:/internships";
    }

    @PostMapping("/internships/{id}/delete")
    public String delete(@PathVariable Long id) {
        this.internshipService.delete(id);
        return "redirect:/internships";
    }

}
