package com.spring.internshipapp.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Data
@Entity
@EqualsAndHashCode(callSuper = true, exclude = {"internship", "coordinator", "journalEntry"}) // Exclude from equals/hashcode
@ToString(callSuper = true, exclude = {"internship", "coordinator", "journalEntry"})
@NoArgsConstructor
public class Student extends User {

    private Long index;

    private String name;

    private String surname;


    @ManyToOne
    private Internship internship;

    @ManyToOne
    private Coordinator coordinator;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private JournalEntry journalEntry;

    public Student(String email,String password,Role role) {
        super(email,password,role);
    }


    public Student(Long index, String email, String password, String name, String surname) {
        this.index = index;
        this.name = name;
        this.surname = surname;
    }

    public Long getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
