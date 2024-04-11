package com.spring.internshipapp.Repository;

import com.spring.internshipapp.Model.Student;
import com.spring.internshipapp.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {

    Optional<Student> findByIndex(Long index);
}
