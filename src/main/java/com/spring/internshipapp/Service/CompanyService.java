package com.spring.internshipapp.Service;

import com.spring.internshipapp.Model.Company;
import com.spring.internshipapp.Model.Role;

import java.util.List;
import java.util.Optional;

public interface CompanyService {
    Optional<Company> addNewCompany(String name, String email,String password, Integer number, String description, String imageUrl, String address, Role role);

    List<Company> getAllCompanies();

    Optional<Company> getCompanyById(Long id);

    Optional<Company> updateCompany(Long id,String name, String email,String password, Integer number, String description, String imageUrl, String address, Role role);

    boolean delete(Long id);
}
