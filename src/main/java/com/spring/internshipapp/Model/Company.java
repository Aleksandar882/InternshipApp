package com.spring.internshipapp.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Company extends User {


    private String name;

    private Integer number;

    @Column(length = 500)
    private String description;

    private String imageUrl;

    private String address;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER)
    private List<Internship> internships;

    public Company(String email,String password) {
        super(email,password);
    }

    public Company(String name,Integer number, String description, String imageUrl, String address, Role role) {
        this.name = name;
        this.number=number;
        this.description = description;
        this.imageUrl = imageUrl;
        this.address=address;
        this.role = role;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Internship> getInternships() {
        return internships;
    }

    public void setInternships(List<Internship> internships) {
        this.internships = internships;
    }
}
