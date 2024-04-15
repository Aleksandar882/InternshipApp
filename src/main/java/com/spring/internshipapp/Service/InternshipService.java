package com.spring.internshipapp.Service;

import com.spring.internshipapp.Model.Company;
import com.spring.internshipapp.Model.Internship;
import com.spring.internshipapp.Model.Student;

import java.util.List;
import java.util.Optional;

public interface InternshipService {
    Optional<Internship> addNewInternship(String position,String location, String description, String timePeriod, Long companyId);

    List<Internship> getAllInternships();

    Internship getInternshipById(Long id);

    Optional<Internship> updateInternship(Long id,String position,String location, String description, String timePeriod, Long companyId);

    List<Internship>getAllByCompany(String companyEmail);

    boolean delete(Long id);
}
