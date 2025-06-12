package com.spring.internshipapp.Service.impl;

import com.spring.internshipapp.Model.Company;
import com.spring.internshipapp.Model.Exceptions.CompanyNotFound;
import com.spring.internshipapp.Model.Role;
import com.spring.internshipapp.Repository.CompanyRepository;
import com.spring.internshipapp.Service.CompanyService;
import jakarta.transaction.Transactional;
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
    public Optional<Company> addNewCompany(String name,Integer number, String description, String imageUrl, String address) {
        Company company = new Company(name,number,description,imageUrl,address);
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
    @Transactional
    public Company getCompanyByName(String companyEmail) {
        return this.companyRepository.findByEmail(companyEmail);
    }

    @Override
    @Transactional
    public Optional<Company> updateCompany(Long id, String name,Integer number, String description, String imageUrl, String address) {
        Company company=this.companyRepository.findById(id).orElseThrow(CompanyNotFound::new);
        company.setName(name);
        company.setNumber(number);
        company.setDescription(description);
        company.setImageUrl(imageUrl);
        company.setAddress(address);
        return Optional.of(this.companyRepository.save(company));
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        this.companyRepository.deleteById(id);
        return this.companyRepository.findById(id).isEmpty();
    }
}
