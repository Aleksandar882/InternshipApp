package com.spring.internshipapp.Service;

import com.spring.internshipapp.Model.Role;
import com.spring.internshipapp.Model.Student;

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

    boolean delete(Long index);


}
