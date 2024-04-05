package com.spring.internshipapp.Repository;

import com.spring.internshipapp.Model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
