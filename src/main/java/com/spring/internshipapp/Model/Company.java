package com.spring.internshipapp.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private Integer number;

    @Column(length = 500)
    private String description;

    private String imageUrl;

    private String address;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER)
    private List<Internship> internships;

    public Company(String name, String email, String password, Integer number, String description, String imageUrl, String address, Role role) {
        this.name = name;
        this.email=email;
        this.password=password;
        this.number=number;
        this.description = description;
        this.imageUrl = imageUrl;
        this.address=address;
        this.role = role;
    }
}
