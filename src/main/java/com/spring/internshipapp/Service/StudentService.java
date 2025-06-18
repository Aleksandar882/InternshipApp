package com.spring.internshipapp.Service;

import com.spring.internshipapp.Model.ApplicationStatus;
import com.spring.internshipapp.Model.Student;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface StudentService {
    Optional<Student> addNewStudent(Long index,String email, String password, String name, String surname);

    List<Student> getAllStudents();

    Optional<Student> getStudentByIndex(Long index);

    Optional<Student> updateStudent(Long index,String email, String password, String name, String surname);

    Student addInternship(String email, Long internshipId);

    List<Student>getAllByCompany(String companyEmail);

    List<Student>getAllByCoordinator(String companyEmail);

    Optional<Student> findById(Long id);

    boolean delete(Long index);

    public Student storeCv(Long studentId, MultipartFile file) throws IOException;

    public Optional<Student> getStudentWithCv(Long studentId);

    public void acceptStudentApplication(Long studentId, Long companyId);

    public void declineStudentApplication(Long studentId, Long companyId);

    public boolean isStudentAcceptedByThisCompany(Long studentId, Long companyId);

    public boolean isStudentFinishedForCompany(Long studentId, Long companyId);

    public List<Student> findPendingApplicantsForCompany(Long companyId);

    public List<Student> findAcceptedStudentsForCompany(Long companyId);

    public List<Student> findFinishedStudentsForCompany(Long companyId);

    public void finishStudentInternship(Long studentId, Long companyId);

    public List<Student> findFinishedInternshipsForCoordinator(String coordinatorEmail);

    public List<Student> getAllByCoordinatorAndApplicationStatus(String coordinatorEmail, ApplicationStatus applicationStatus);

    public void markInternshipAsCompletedByCoordinator(Long studentId, Long coordinatorId);

    public List<Student> findCompletedStudentsForCoordinator(Long coordinatorId);

}
