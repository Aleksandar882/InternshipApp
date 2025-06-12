package com.spring.internshipapp.Web;

import com.spring.internshipapp.Model.JournalEntry;
import com.spring.internshipapp.Model.Student;
import com.spring.internshipapp.Model.User;
import com.spring.internshipapp.Service.JournalService;
import com.spring.internshipapp.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/my-journal")
public class JournalController {

    @Autowired
    private JournalService journalService;

    @Autowired
    private StudentService studentService;

    private Student getCurrentStudent(User currentUserPrincipal) {
        if (currentUserPrincipal instanceof Student) {
            return (Student) currentUserPrincipal;
        }
        throw new IllegalStateException("Authenticated user is not a Student instance.");
    }


    @GetMapping
    public String viewOrEditJournal(Model model) {

        Student currentStudent = (Student) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<JournalEntry> journalOpt = journalService.getJournalForStudent(currentStudent);

        String content = journalOpt.map(JournalEntry::getContent).orElse("");

        model.addAttribute("journalContent", content);
        model.addAttribute("pageTitle", "Дневник за студентска пракса");

        return "my-journal-form.html";
    }

    @PostMapping("/save")
    public String saveJournal(@RequestParam("content") String content,
                              @AuthenticationPrincipal User currentUserPrincipal,
                              RedirectAttributes redirectAttributes) {
        if (currentUserPrincipal == null) {
            return "redirect:/login";
        }

        if (! (currentUserPrincipal instanceof Student)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Only students can save journals.");
            return "redirect:/internships";
        }
        Student currentStudent = (Student) currentUserPrincipal;

        try {
            journalService.saveOrUpdateJournal(content, currentStudent);
            redirectAttributes.addFlashAttribute("successMessage", "Дневникот е зачуван!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Проблем при зачувување: " + e.getMessage());
        }
        return "redirect:/my-journal";
    }

    @GetMapping("/view/student/{studentId}")
    @PreAuthorize("hasAnyAuthority('COORDINATOR', 'ADMIN')")
    public String viewStudentJournal(@PathVariable("studentId") Long studentId, Model model,
                                     RedirectAttributes redirectAttributes,
                                     @AuthenticationPrincipal User currentUserPrincipal) {

        if (currentUserPrincipal == null) {
            return "redirect:/login";
        }

        Optional<JournalEntry> journalOpt = journalService.getJournalByStudentId(studentId);

        if (journalOpt.isPresent()) {
            model.addAttribute("journalContent", journalOpt.get().getContent());
            Optional<Student> student = studentService.findById(studentId);
            model.addAttribute("viewingStudent", student);
            model.addAttribute("pageTitle", "Дневник за пракса");
            return "view-student-journal.html";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Student or journal not found.");
            return "redirect:/students";
        }
    }
}
