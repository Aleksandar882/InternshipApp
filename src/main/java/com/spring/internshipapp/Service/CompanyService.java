package com.spring.internshipapp.Service;

import com.spring.internshipapp.Model.Company;
import com.spring.internshipapp.Model.Role;

import java.util.List;
import java.util.Optional;

public interface CompanyService {
    Optional<Company> addNewCompany(String name, Integer number, String description, String imageUrl, String address);

    List<Company> getAllCompanies();

    Company getCompanyById(Long id);

    Company getCompanyByName(String companyEmail);

    Optional<Company> updateCompany(Long id,String name, Integer number, String description, String imageUrl, String address);

    boolean delete(Long id);
}
