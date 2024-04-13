package com.spring.internshipapp.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Coordinator extends User {

    private String name;

    private String surname;

    @OneToMany(mappedBy = "coordinator", fetch = FetchType.EAGER)
    private List<Student> students;

    public Coordinator(String email,String password,Role role) {
        super(email,password,role);
    }

    public Coordinator(String name, String surname,List<Student> students) {
        this.name = name;
        this.surname = surname;
        this.students = students;
    }
}
