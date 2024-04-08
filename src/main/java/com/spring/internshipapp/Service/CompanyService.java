package com.spring.internshipapp.Service;

import com.spring.internshipapp.Model.Company;
import com.spring.internshipapp.Model.Role;

import java.util.List;
import java.util.Optional;

public interface CompanyService {
    Optional<Company> addNewCompany(String name, Integer number, String description, String imageUrl, String address, Role role);

    List<Company> getAllCompanies();

    Company getCompanyById(Long id);

    Optional<Company> updateCompany(Long id,String name, Integer number, String description, String imageUrl, String address, Role role);

    boolean delete(Long id);
}
