package com.spring.internshipapp.Repository;

import com.spring.internshipapp.Model.ApplicationStatus;
import com.spring.internshipapp.Model.Student;
import com.spring.internshipapp.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {

    Optional<Student> findByIndex(Long index);

    Optional<Student> findByEmail(String email);

    @Query(value = " select users.*,student.name,student.coordinator_id,student.internship_id,student.surname,student.index from student left join internship on student.internship_id = internship.id left join company on company.id = internship.company_id left join users on company.id = users.id where users.email = :mail", nativeQuery = true)
    List<Student>findAllByInternshipCompany(@Param("mail")String companyEmail);

    List<Student>findAllByInternshipCompanyEmail(String companyEmail);

    List<Student>findAllByCoordinatorEmail(String coordinatorEmail);

    List<Student>findAllByCoordinatorEmailAndApplicationStatus(String coordinatorEmail, ApplicationStatus status);

    Optional<Student> findByIdAndInternship_Company_IdAndApplicationStatus(Long studentId, Long companyId, ApplicationStatus status);

    List<Student> findByInternship_Company_IdAndApplicationStatus(Long companyId, ApplicationStatus status);

}
