package com.spring.internshipapp.Service.impl;

import com.spring.internshipapp.Model.Coordinator;
import com.spring.internshipapp.Model.Exceptions.CoordinatorNotFound;
import com.spring.internshipapp.Model.Role;
import com.spring.internshipapp.Model.Student;
import com.spring.internshipapp.Repository.CoordinatorRepository;
import com.spring.internshipapp.Repository.StudentRepository;
import com.spring.internshipapp.Service.CoordinatorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoordinatorServiceImpl implements CoordinatorService {

    private final CoordinatorRepository coordinatorRepository;
    private final StudentRepository studentRepository;

    public CoordinatorServiceImpl(CoordinatorRepository coordinatorRepository, StudentRepository studentRepository) {
        this.coordinatorRepository = coordinatorRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public Optional<Coordinator> addNewCoordinator(String name, String surname, String email, String password, Role role, List<Long> studentsId) {
        List<Student> students = this.studentRepository.findAllById(studentsId);
        Coordinator coordinator = new Coordinator(name,surname,email,password,role,students);
        return Optional.of(this.coordinatorRepository.save(coordinator));
    }

    @Override
    public List<Coordinator> getAllCoordinators() {
        return this.coordinatorRepository.findAll();
    }

    @Override
    public Optional<Coordinator> getCoordinatorById(Long id) {
        return this.coordinatorRepository.findById(id);
    }

    @Override
    public Optional<Coordinator> updateCoordinator(Long id, String name, String surname, String email, String password, Role role, List<Long> studentsId) {
        List<Student> students = this.studentRepository.findAllById(studentsId);
        Coordinator coordinator= this.coordinatorRepository.findById(id).orElseThrow(CoordinatorNotFound::new);
        coordinator.setName(name);
        coordinator.setSurname(surname);
        coordinator.setEmail(email);
        coordinator.setPassword(password);
        coordinator.setRole(role);
        coordinator.setStudents(students);
        return Optional.of(this.coordinatorRepository.save(coordinator));
    }

    @Override
    public boolean delete(Long id) {
        this.coordinatorRepository.deleteById(id);
        return this.coordinatorRepository.findById(id).isEmpty();
    }
}
