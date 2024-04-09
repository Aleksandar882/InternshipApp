package com.spring.internshipapp.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Data
@Entity
@NoArgsConstructor
public class Student extends User {

    private Long index;

    private String name;

    private String surname;


    @ManyToOne
    private Internship internship;

    @ManyToOne
    private Coordinator coordinator;

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
