package com.spring.internshipapp.Service.impl;

import com.spring.internshipapp.Model.Company;
import com.spring.internshipapp.Model.Exceptions.CompanyNotFound;
import com.spring.internshipapp.Model.Role;
import com.spring.internshipapp.Repository.CompanyRepository;
import com.spring.internshipapp.Service.CompanyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }


    @Override
    public Optional<Company> addNewCompany(String name, String email, String password, Integer number, String description, String imageUrl, String address, Role role) {
        Company company = new Company(name,email,password,number,description,imageUrl,address,role);
        return Optional.of(this.companyRepository.save(company));
    }

    @Override
    public List<Company> getAllCompanies() {
        return this.companyRepository.findAll();
    }

    @Override
    public Company getCompanyById(Long id) {
        return this.companyRepository.findById(id).orElseThrow(CompanyNotFound::new);
    }

    @Override
    public Optional<Company> updateCompany(Long id, String name, String email, String password, Integer number, String description, String imageUrl, String address, Role role) {
        Company company=this.companyRepository.findById(id).orElseThrow(CompanyNotFound::new);
        company.setName(name);
        company.setEmail(email);
        company.setPassword(password);
        company.setNumber(number);
        company.setDescription(description);
        company.setImageUrl(imageUrl);
        company.setAddress(address);
        company.setRole(role);
        return Optional.of(this.companyRepository.save(company));
    }

    @Override
    public boolean delete(Long id) {
        this.companyRepository.deleteById(id);
        return this.companyRepository.findById(id).isEmpty();
    }
}
