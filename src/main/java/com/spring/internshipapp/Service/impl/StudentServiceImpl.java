package com.spring.internshipapp.Service.impl;

import com.spring.internshipapp.Model.Company;
import com.spring.internshipapp.Model.Exceptions.CompanyNotFound;
import com.spring.internshipapp.Model.Exceptions.StudentNotFound;
import com.spring.internshipapp.Model.Role;
import com.spring.internshipapp.Model.Student;
import com.spring.internshipapp.Repository.StudentRepository;
import com.spring.internshipapp.Service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
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

    @Override
    public boolean delete(Long index) {
        this.studentRepository.deleteById(index);
        return this.studentRepository.findById(index).isEmpty();
    }
}
