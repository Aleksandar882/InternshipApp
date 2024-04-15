package com.spring.internshipapp.Service.impl;

import com.spring.internshipapp.Model.Company;
import com.spring.internshipapp.Model.Exceptions.CompanyNotFound;
import com.spring.internshipapp.Model.Exceptions.InternshipNotFound;
import com.spring.internshipapp.Model.Internship;
import com.spring.internshipapp.Repository.CompanyRepository;
import com.spring.internshipapp.Repository.InternshipRepository;
import com.spring.internshipapp.Service.InternshipService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InternshipServiceImpl implements InternshipService {

    private final InternshipRepository internshipRepository;
    private final CompanyRepository companyRepository;

    public InternshipServiceImpl(InternshipRepository internshipRepository, CompanyRepository companyRepository) {
        this.internshipRepository = internshipRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public Optional<Internship> addNewInternship(String position, String location, String description, String timePeriod, Long companyId) {
        Company company = this.companyRepository.findById(companyId).orElseThrow(CompanyNotFound::new);
        Internship internship = new Internship(position,location,description,timePeriod,company);
        return Optional.of(this.internshipRepository.save(internship));
    }

    @Override
    public List<Internship> getAllInternships() {
        return this.internshipRepository.findAll();
    }

    @Override
    public Internship getInternshipById(Long id) {
        return this.internshipRepository.findById(id).orElseThrow(InternshipNotFound::new);
    }

    @Override
    public Optional<Internship> updateInternship(Long id, String position, String location, String description, String timePeriod, Long companyId) {
        Company company = this.companyRepository.findById(companyId).orElseThrow(CompanyNotFound::new);
        Internship internship=this.internshipRepository.findById(id).orElseThrow(InternshipNotFound::new);
        internship.setPosition(position);
        internship.setLocation(location);
        internship.setDescription(description);
        internship.setTimePeriod(timePeriod);
        internship.setCompany(company);
        return Optional.of(this.internshipRepository.save(internship));
    }

    @Override
    public List<Internship> getAllByCompany(String companyEmail) {
        return this.internshipRepository.findAllByCompanyEmail(companyEmail);
    }

    @Override
    public boolean delete(Long id) {
        this.internshipRepository.deleteById(id);
        return this.internshipRepository.findById(id).isEmpty();
    }
}
