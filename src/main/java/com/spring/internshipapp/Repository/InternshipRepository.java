package com.spring.internshipapp.Repository;

import com.spring.internshipapp.Model.Internship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InternshipRepository extends JpaRepository<Internship,Long> {

    List<Internship>findAllByCompanyEmail(String companyEmail);
}
