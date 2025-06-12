package com.spring.internshipapp.Service.impl;

import com.spring.internshipapp.Model.JournalEntry;
import com.spring.internshipapp.Model.Student;
import com.spring.internshipapp.Repository.JournalEntryRepository;
import com.spring.internshipapp.Repository.StudentRepository;
import com.spring.internshipapp.Service.JournalService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JournalServiceImpl implements JournalService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    @Transactional
    public Optional<JournalEntry> getJournalForStudent(Student student) {
        if (student == null) {
            return Optional.empty();
        }
        return journalEntryRepository.findByStudent(student);
    }

    @Override
    @Transactional
    public JournalEntry saveOrUpdateJournal(String content, Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }

        Optional<JournalEntry> existingJournalOpt = journalEntryRepository.findByStudentId(student.getId());

        JournalEntry journalEntry;
        if (existingJournalOpt.isPresent()) {
            journalEntry = existingJournalOpt.get();
            journalEntry.setContent(content);
        } else {
            journalEntry = new JournalEntry(content, student);
        }
        return journalEntryRepository.save(journalEntry);
    }

    @Override
    public Student findStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Student not found with email: " + email));
    }

    @Transactional
    @Override
    public Optional<JournalEntry> getJournalByStudentId(Long studentId) {
        return journalEntryRepository.findByStudentId(studentId);
    }
}
