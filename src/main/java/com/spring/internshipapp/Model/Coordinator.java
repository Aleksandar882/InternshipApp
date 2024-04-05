package com.spring.internshipapp.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Coordinator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    private String email;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "coordinator", fetch = FetchType.EAGER)
    private List<Student> students;

    public Coordinator(Long id, String name, String surname, String email, String password, Role role, List<Student> students) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password=password;
        this.role = role;
        this.students = students;
    }
}
