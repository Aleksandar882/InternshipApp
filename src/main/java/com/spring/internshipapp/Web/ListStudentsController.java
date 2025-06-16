package com.spring.internshipapp.Web;

import com.spring.internshipapp.Model.*;
import com.spring.internshipapp.Service.CompanyService;
import com.spring.internshipapp.Service.InternshipService;
import com.spring.internshipapp.Service.JournalService;
import com.spring.internshipapp.Service.StudentService;
import com.spring.internshipapp.Service.impl.PdfGenerationService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class ListStudentsController {

    @Autowired
    private JournalService journalService;

    @Autowired
    private PdfGenerationService pdfGenerationService;


    private final StudentService studentService;
    private final InternshipService internshipService;
    private final CompanyService companyService;

    public ListStudentsController(StudentService studentService, InternshipService internshipService, CompanyService companyService) {
        this.studentService = studentService;
        this.internshipService = internshipService;
        this.companyService = companyService;
    }

    private Company getAuthenticatedCompany(User currentUserPrincipal) {
        if (currentUserPrincipal instanceof Company) {
            return (Company) currentUserPrincipal;
        }
        throw new IllegalStateException("Authenticated user is not a Company or company record not found.");
    }

    private Coordinator getAuthenticatedCoordinator(User currentUserPrincipal) {
        if (currentUserPrincipal instanceof Coordinator) {
            return (Coordinator) currentUserPrincipal;
        }
        throw new IllegalStateException("Authenticated user is not a Coordinator or coordinator record not found.");
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
        List<Student> students = this.studentService.getAllByCoordinatorAndApplicationStatus(username, ApplicationStatus.FINISHED);
        List<Company> companies=this.companyService.getAllCompanies();
        List<Internship> internships=this.internshipService.getAllInternships();
        model.addAttribute("students", students);
        model.addAttribute("companies", companies);
        model.addAttribute("internships", internships);
        return "students.html";
    }

    @GetMapping({"/students-company"})
    @PreAuthorize("hasAuthority('ROLE_COMPANY')")
    public String listCompanyApplicants(Model model, @AuthenticationPrincipal User currentUserPrincipal) {
        try {
            Company company = getAuthenticatedCompany(currentUserPrincipal);
            List<Student> pendingApplicants = studentService.findPendingApplicantsForCompany(company.getId());
            List<Student> acceptedApplicants = studentService.findAcceptedStudentsForCompany(company.getId());
            List<Student> finishedApplicants = studentService.findFinishedStudentsForCompany(company.getId());

            model.addAttribute("pendingApplicants", pendingApplicants);
            model.addAttribute("acceptedApplicants", acceptedApplicants);
            model.addAttribute("finishedApplicants", finishedApplicants);
            model.addAttribute("pageTitle", "Пријавени Студенти");
            return "students-company";
        } catch (IllegalStateException e) {
            return "redirect:/error-unauthorized";
        }
    }

    @PostMapping("/students/{id}/delete")
    public String delete(@PathVariable Long id) {
        this.studentService.delete(id);
        return "redirect:/students";
    }

    @GetMapping("/students/{id}/cv/download")
    @PreAuthorize("hasAuthority('ROLE_COMPANY')")
    public ResponseEntity<ByteArrayResource> downloadApplicantCv(@PathVariable("id") Long studentId,
                                                        @AuthenticationPrincipal User currentUserPrincipal) {
        if (currentUserPrincipal == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            Optional<Student> studentOpt = studentService.getStudentWithCv(studentId);

            if (!studentOpt.isPresent() || studentOpt.get().getCvData() == null || studentOpt.get().getCvFileName() == null) {
                return ResponseEntity.notFound().build();
            }

            Student student = studentOpt.get();
            ByteArrayResource resource = new ByteArrayResource(student.getCvData());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + student.getCvFileName() + "\"")
                    .body(resource);

        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).build();
        }
    }

    @PostMapping("/students/{id}/accept")
    @PreAuthorize("hasAuthority('ROLE_COMPANY')")
    public String acceptApplicant(@PathVariable("id") Long studentId,
                                  @AuthenticationPrincipal User currentUserPrincipal,
                                  RedirectAttributes redirectAttributes) {
        try {
            Company company = getAuthenticatedCompany(currentUserPrincipal);
            studentService.acceptStudentApplication(studentId, company.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Студентот е прифатен.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Грешка: " + e.getMessage());
        }
        return "redirect:/students-company";
    }

    @PostMapping("/students/{id}/decline")
    @PreAuthorize("hasAuthority('ROLE_COMPANY')")
    public String declineApplicant(@PathVariable("id") Long studentId,
                                   @AuthenticationPrincipal User currentUserPrincipal,
                                   RedirectAttributes redirectAttributes) {
        try {
            Company company = getAuthenticatedCompany(currentUserPrincipal);
            studentService.declineStudentApplication(studentId, company.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Студентот е одбиен.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Грешка: " + e.getMessage());
        }
        return "redirect:/students-company";
    }

    @PostMapping("/students/{id}/finish")
    @PreAuthorize("hasAuthority('ROLE_COMPANY')")
    public String finishInternship(@PathVariable("id") Long studentId,
                                   @AuthenticationPrincipal User currentUserPrincipal,
                                   RedirectAttributes redirectAttributes) {
        try {
            Company company = getAuthenticatedCompany(currentUserPrincipal);
            studentService.finishStudentInternship(studentId, company.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Праксата е означена како завршена.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Грешка: " + e.getMessage());
        }
        return "redirect:/students-company";
    }

    @GetMapping("/students/{id}/journal")
    @PreAuthorize("hasAuthority('ROLE_COMPANY')")
    public String viewAcceptedStudentJournal(@PathVariable("id") Long studentId,
                                             @AuthenticationPrincipal User currentUserPrincipal,
                                             Model model, RedirectAttributes redirectAttributes) {

            Company company = getAuthenticatedCompany(currentUserPrincipal);
            boolean isAcceptedByThisCompany = studentService.isStudentAcceptedByThisCompany(studentId, company.getId());
            boolean isFinished = studentService.isStudentFinishedForCompany(studentId, company.getId());
            if (!isAcceptedByThisCompany && !isFinished) {
                redirectAttributes.addFlashAttribute("errorMessage", "Немате пристап до овој дневник или студентот не е прифатен од вашата компанија.");
                return "redirect:/students-company";
            }
            Optional<JournalEntry> journalOpt = journalService.getJournalByStudentId(studentId);
            if (journalOpt.isPresent()) {
                model.addAttribute("journalContent", journalOpt.get().getContent());
                Optional<Student> student = studentService.findById(studentId);
                model.addAttribute("viewingStudent", student);
                model.addAttribute("pageTitle", "Дневник за пракса");
            return "view-student-journal.html";
        } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Студентот нема дневник.");
                return "redirect:/students-company";
            }
    }

    @GetMapping("/students/{id}/confirmation-pdf")
    @PreAuthorize("hasAuthority('ROLE_COORDINATOR')")
    public ResponseEntity<ByteArrayResource> downloadInternshipConfirmation(
            @PathVariable("id") Long studentId,
            @AuthenticationPrincipal User currentUserPrincipal) throws IOException {

        Optional<Student> studentOpt = studentService.getStudentWithCv(studentId);
        if (!studentOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Student student = studentOpt.get();

        Coordinator loggedInCoordinator = getAuthenticatedCoordinator(currentUserPrincipal);

            if (loggedInCoordinator == null) {
                System.err.println("Could not identify logged in coordinator for PDF download.");
                return ResponseEntity.status(403).build();
            }

        boolean isAssigned = student.getCoordinator() != null &&
                student.getCoordinator().getId().equals(loggedInCoordinator.getId());
        boolean isFinished = student.getApplicationStatus() == ApplicationStatus.FINISHED;

        if (!isAssigned || !isFinished) {
            System.err.println("Unauthorized attempt to download confirmation PDF.");
            return ResponseEntity.status(403).build();
        }

        byte[] pdfBytes = pdfGenerationService.generateInternshipConfirmationPdf(student);
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);

        String filename = String.format("Potvrda_Praksa_%s_%s.pdf",
                student.getSurname().replaceAll("[^a-zA-Z0-9]", ""),
                student.getName().replaceAll("[^a-zA-Z0-9]", ""));

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }
}
