package com.spring.internshipapp.Web;

import com.spring.internshipapp.Model.Student;
import com.spring.internshipapp.Model.User;
import com.spring.internshipapp.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType; // Import this
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/student/cv")
public class StudentCvController {

    @Autowired
    private StudentService studentService;


    private Student getFreshAuthenticatedStudent(User currentUserPrincipal) {
        return studentService.findById(currentUserPrincipal.getId())
                .orElseThrow(() -> new IllegalStateException("Student record not found for authenticated user."));
    }

    @GetMapping("/manage")
    public String manageCvPage(Model model, @AuthenticationPrincipal User currentUserPrincipal, RedirectAttributes redirectAttributes) {
        if (currentUserPrincipal == null) return "redirect:/login";
        try {
            Student student =  getFreshAuthenticatedStudent(currentUserPrincipal);
            model.addAttribute("student", student);
            model.addAttribute("pageTitle", "Управување со CV");
            return "cv-manage.html";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/internships";
        }
    }

    @PostMapping("/upload")
    public String uploadCv(@RequestParam("cvFile") MultipartFile cvFile,
                           @AuthenticationPrincipal User currentUserPrincipal,
                           RedirectAttributes redirectAttributes) {
        if (currentUserPrincipal == null) return "redirect:/login";
        try {
            Student student = getFreshAuthenticatedStudent(currentUserPrincipal);
            if (cvFile.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Ве молиме изберете датотека за прикачување.");
                return "redirect:/student/cv/manage";
            }

            String contentType = cvFile.getContentType();
            if (contentType == null || !contentType.equals("application/pdf")) {
                redirectAttributes.addFlashAttribute("errorMessage", "Невалиден тип на датотека. Дозволени се само PDF датотеки.");
                return "redirect:/student/cv/manage";
            }

            studentService.storeCv(student.getId(), cvFile);
            redirectAttributes.addFlashAttribute("successMessage", "CV-то е успешно прикачено!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Грешка при прикачување на CV-то: " + e.getMessage());
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/internships";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/student/cv/manage";
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadCv(@AuthenticationPrincipal User currentUserPrincipal) {
        if (currentUserPrincipal == null) return ResponseEntity.status(401).build();
        try {
            Student student = getFreshAuthenticatedStudent(currentUserPrincipal);
            if (student.getCvData() == null || student.getCvFileName() == null) {
                return ResponseEntity.notFound().build();
            }

            ByteArrayResource resource = new ByteArrayResource(student.getCvData());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + student.getCvFileName() + "\"")
                    .body(resource);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).build();
        }
    }
}
