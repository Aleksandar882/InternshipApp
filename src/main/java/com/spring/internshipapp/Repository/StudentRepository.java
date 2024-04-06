package com.spring.internshipapp.Repository;

import com.spring.internshipapp.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Long> {
}
