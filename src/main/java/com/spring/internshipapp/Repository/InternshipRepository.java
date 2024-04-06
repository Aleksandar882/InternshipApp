package com.spring.internshipapp.Repository;

import com.spring.internshipapp.Model.Internship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternshipRepository extends JpaRepository<Internship,Long> {
}
