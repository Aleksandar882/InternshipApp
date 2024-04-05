package com.spring.internshipapp.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Internship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String position;

    private String location;

    @Column(length = 500)
    private String description;

    private String timePeriod;

    @ManyToOne
    private Company company;

    @OneToMany(mappedBy = "internship", fetch = FetchType.EAGER)
    private List<Student> students;

    public Internship(Long id, String position, String location, String description, String timePeriod, Company company) {
        this.id = id;
        this.position = position;
        this.location = location;
        this.description = description;
        this.timePeriod = timePeriod;
        this.company = company;
    }
}
