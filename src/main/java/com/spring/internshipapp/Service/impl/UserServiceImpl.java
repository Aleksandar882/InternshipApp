package com.spring.internshipapp.Service.impl;

import com.spring.internshipapp.Model.Company;
import com.spring.internshipapp.Model.Exceptions.InvalidEmailException;
import com.spring.internshipapp.Model.Exceptions.InvalidEmailOrPasswordException;
import com.spring.internshipapp.Model.Exceptions.PasswordsDoNotMatchException;
import com.spring.internshipapp.Model.Exceptions.EmailAlreadyExistsException;
import com.spring.internshipapp.Model.Role;
import com.spring.internshipapp.Model.Student;
import com.spring.internshipapp.Model.User;
import com.spring.internshipapp.Repository.CompanyRepository;
import com.spring.internshipapp.Repository.StudentRepository;
import com.spring.internshipapp.Repository.UserRepository;
import com.spring.internshipapp.Service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;

    public UserServiceImpl(UserRepository userRepository, StudentRepository studentRepository, PasswordEncoder passwordEncoder, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyRepository = companyRepository;
    }

    @Override
    public User register(Long index,String name,String surname,String email, String password, String repeatPassword) {
        if (email==null || email.isEmpty()  || password==null || password.isEmpty())
            throw new InvalidEmailOrPasswordException();
        if (!password.equals(repeatPassword))
            throw new PasswordsDoNotMatchException();
        if(this.userRepository.findByEmail(email).isPresent())
            throw new EmailAlreadyExistsException(email);
        User user = new User(email,passwordEncoder.encode(password));
        Student student= new Student();
        Role role=Role.ROLE_STUDENT;
        student.setRole(role);
        student.setIndex(index);
        student.setName(name);
        student.setSurname(surname);
        student.setEmail(email);
        student.setPassword(passwordEncoder.encode(password));
        return studentRepository.save(student);
    }

    @Override
    public User registerCompany(String name, String description, String address, Integer number, String image_url,String email, String password, String repeatPassword) {
        if (email==null || email.isEmpty()  || password==null || password.isEmpty())
            throw new InvalidEmailOrPasswordException();
        if (!password.equals(repeatPassword))
            throw new PasswordsDoNotMatchException();
        if(this.userRepository.findByEmail(email).isPresent())
            throw new EmailAlreadyExistsException(email);
        User user = new User(email,passwordEncoder.encode(password));
        Company company= new Company();
        Role role=Role.ROLE_COMPANY;
        company.setRole(role);
        company.setName(name);
        company.setDescription(description);
        company.setAddress(address);
        company.setNumber(number);
        company.setImageUrl(image_url);
        company.setEmail(email);
        company.setPassword(passwordEncoder.encode(password));
        return companyRepository.save(company);
    }

    @Override
    public User FindByEmail(String email) {
        User user=  this.userRepository.findByEmail(email).orElseThrow(InvalidEmailException::new);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user=  this.userRepository.findByEmail(email).orElseThrow(InvalidEmailException::new);
        UserDetails userDetails= new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(),user.getAuthorities());
        return userDetails;
    }
}
