package com.spring.internshipapp.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Data
@Entity
@NoArgsConstructor
public class Student {
    @Id
    private String index;

    private String email;

    private String password;

    private String name;

    private String surname;

    public Student(String index, String email, String password, String name, String surname) {
        this.index = index;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

    public String getIndex() {
        return index;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
