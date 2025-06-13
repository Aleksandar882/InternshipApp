package com.spring.internshipapp.Service.impl;

import com.spring.internshipapp.Model.ApplicationStatus;
import com.spring.internshipapp.Model.Exceptions.InternshipNotFound;
import com.spring.internshipapp.Model.Exceptions.StudentNotFound;
import com.spring.internshipapp.Model.Exceptions.UserNotFoundException;
import com.spring.internshipapp.Model.Internship;
import com.spring.internshipapp.Model.Student;
import com.spring.internshipapp.Repository.InternshipRepository;
import com.spring.internshipapp.Repository.StudentRepository;
import com.spring.internshipapp.Repository.UserRepository;
import com.spring.internshipapp.Service.StudentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final InternshipRepository internshipRepository;
    private final UserRepository userRepository;

    public StudentServiceImpl(StudentRepository studentRepository, InternshipRepository internshipRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.internshipRepository = internshipRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Student> addNewStudent(Long index, String email, String password, String name, String surname) {
        Student student= new Student(index,email,password,name,surname);
        return Optional.of(this.studentRepository.save(student));
    }

    @Override
    public List<Student> getAllStudents() {
        return this.studentRepository.findAll();
    }

    @Override
    public Optional<Student> getStudentByIndex(Long index) {
        return this.studentRepository.findById(index);
    }

    @Override
    public Optional<Student> updateStudent(Long index, String email, String password, String name, String surname) {
        Student student=this.studentRepository.findById(index).orElseThrow(StudentNotFound::new);
        student.setEmail(email);
        student.setPassword(password);
        student.setName(name);
        student.setSurname(surname);
        return Optional.of(this.studentRepository.save(student));
    }

    @Transactional
    @Override
    public Student addInternship(String email, Long internshipId) {
        Student student = this.studentRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        Internship internship = this.internshipRepository.findById(internshipId).orElseThrow(InternshipNotFound::new);
        student.setInternship(internship);
        student.setApplicationStatus(ApplicationStatus.PENDING);
        return this.studentRepository.save(student);
    }

    @Transactional
    @Override
    public List<Student> getAllByCompany(String companyEmail) {
        return this.studentRepository.findAllByInternshipCompanyEmail(companyEmail);
    }

    @Transactional
    @Override
    public List<Student> getAllByCoordinator(String coordinatorEmail) {
        return this.studentRepository.findAllByCoordinatorEmail(coordinatorEmail);
    }

    @Override
    public Optional<Student> findById(Long id) {
        return this.studentRepository.findById(id);
    }

    @Override
    public boolean delete(Long index) {
        this.studentRepository.deleteById(index);
        return this.studentRepository.findById(index).isEmpty();
    }

    @Transactional
    @Override
    public Student storeCv(Long studentId, MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id " + studentId));

        if (fileName.contains("..")) {
            throw new RuntimeException("Извинете! Името на датотеката содржи невалидна патека " + fileName);
        }
        if (file.isEmpty()) {
            throw new RuntimeException("Неуспешно зачувување на празна датотека.");
        }

        if (!"application/pdf".equals(file.getContentType())) {
            throw new RuntimeException("Невалиден тип на датотека. Дозволени се само PDF датотеки.");
        }


        student.setCvFileName(fileName);
        student.setCvData(file.getBytes());

        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public Optional<Student> getStudentWithCv(Long studentId) {
        return studentRepository.findById(studentId);
    }

    @Override
    @Transactional
    public void acceptStudentApplication(Long studentId, Long companyId) {
        Student student = studentRepository.findByIdAndInternship_Company_IdAndApplicationStatus(
                        studentId, companyId, ApplicationStatus.PENDING)
                .orElseThrow(() -> new RuntimeException("Pending student application not found for this company."));
        student.setApplicationStatus(ApplicationStatus.ACCEPTED);
        studentRepository.save(student);
    }

    @Override
    @Transactional
    public void declineStudentApplication(Long studentId, Long companyId) {
        Student student = studentRepository.findByIdAndInternship_Company_IdAndApplicationStatus(
                        studentId, companyId, ApplicationStatus.PENDING)
                .orElseThrow(() -> new RuntimeException("Pending student application not found for this company."));

        student.setApplicationStatus(ApplicationStatus.DECLINED);
        student.setInternship(null);
        studentRepository.save(student);
    }

    @Override
    @Transactional
    public boolean isStudentAcceptedByThisCompany(Long studentId, Long companyId) {
        Optional<Student> studentOpt = studentRepository.findByIdAndInternship_Company_IdAndApplicationStatus(
                studentId, companyId, ApplicationStatus.ACCEPTED);
        return studentOpt.isPresent();
    }

    @Override
    @Transactional
    public List<Student> findPendingApplicantsForCompany(Long companyId) {
        return studentRepository.findByInternship_Company_IdAndApplicationStatus(companyId, ApplicationStatus.PENDING);
    }

    @Override
    @Transactional
    public List<Student> findAcceptedStudentsForCompany(Long companyId) {
        return studentRepository.findByInternship_Company_IdAndApplicationStatus(companyId, ApplicationStatus.ACCEPTED);
    }
}
