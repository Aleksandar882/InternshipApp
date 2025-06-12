package com.spring.internshipapp.Service;

import com.spring.internshipapp.Model.JournalEntry;
import com.spring.internshipapp.Model.Student;

import java.util.Optional;

public interface JournalService {
    public Optional<JournalEntry> getJournalForStudent(Student student);
    public JournalEntry saveOrUpdateJournal(String content, Student student);
    public Student findStudentByEmail(String email);
    public Optional<JournalEntry> getJournalByStudentId(Long studentId);
}
