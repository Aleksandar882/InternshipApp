package com.spring.internshipapp.Service;

import com.spring.internshipapp.Model.Coordinator;
import com.spring.internshipapp.Model.Role;
import com.spring.internshipapp.Model.Student;

import java.util.List;
import java.util.Optional;

public interface CoordinatorService {

    Optional<Coordinator> addNewCoordinator(String name, String surname,List<Long> studentsId);

    List<Coordinator> getAllCoordinators();

    Optional<Coordinator> getCoordinatorById(Long id);

    Optional<Coordinator> updateCoordinator(Long id,String name, String surname,List<Long> studentsId);

    boolean delete(Long id);

}
