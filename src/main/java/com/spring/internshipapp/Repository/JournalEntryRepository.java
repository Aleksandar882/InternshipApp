package com.spring.internshipapp.Repository;

import com.spring.internshipapp.Model.JournalEntry;
import com.spring.internshipapp.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    Optional<JournalEntry> findByStudent(Student student);
    Optional<JournalEntry> findByStudentId(Integer studentId);
}
