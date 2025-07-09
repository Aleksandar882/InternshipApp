package com.spring.internshipapp.Service.impl;

import com.spring.internshipapp.Model.*;
import com.spring.internshipapp.Model.Exceptions.*;
import com.spring.internshipapp.Repository.CompanyRepository;
import com.spring.internshipapp.Repository.CoordinatorRepository;
import com.spring.internshipapp.Repository.StudentRepository;
import com.spring.internshipapp.Repository.UserRepository;
import com.spring.internshipapp.Service.UserService;
import jakarta.transaction.Transactional;
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
    private final CoordinatorRepository coordinatorRepository;

    public UserServiceImpl(UserRepository userRepository, StudentRepository studentRepository, PasswordEncoder passwordEncoder, CompanyRepository companyRepository, CoordinatorRepository coordinatorRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyRepository = companyRepository;
        this.coordinatorRepository = coordinatorRepository;
    }

    @Override
    public User register(Long index,String name,String surname,String email, String password, String repeatPassword) {
        if (email==null || email.isEmpty()  || password==null || password.isEmpty())
            throw new InvalidEmailOrPasswordException();
        if (!password.equals(repeatPassword))
            throw new PasswordsDoNotMatchException();
        if(this.userRepository.findByEmail(email).isPresent())
            throw new EmailAlreadyExistsException();
        if(this.studentRepository.findByIndex(index).isPresent())
            throw new IndexAlreadyExistsException();
        User user = new User();
        Student student= new Student();
        Role role=Role.ROLE_STUDENT;
        student.setRole(role);
        student.setIndex(index);
        student.setName(name);
        student.setSurname(surname);
        student.setEmail(email);
        student.setPassword(passwordEncoder.encode(password));
        long lastDigit=index % 10;
        Coordinator coordinator;
        if(lastDigit==0 || lastDigit==1 || lastDigit==2 || lastDigit==3 || lastDigit==4) {
            coordinator = this.coordinatorRepository.findById(6L).orElseThrow(CoordinatorNotFound::new);
        }else {
            coordinator = this.coordinatorRepository.findById(8L).orElseThrow(CoordinatorNotFound::new);
        }
        student.setCoordinator(coordinator);
        return studentRepository.save(student);
    }

    @Override
    public User registerCompany(String name, String description, String address, Integer number, String image_url,String email, String password, String repeatPassword) {
        if (email==null || email.isEmpty()  || password==null || password.isEmpty())
            throw new InvalidEmailOrPasswordException();
        if (!password.equals(repeatPassword))
            throw new PasswordsDoNotMatchException();
        if(this.userRepository.findByEmail(email).isPresent())
            throw new EmailAlreadyExistsException();
        User user = new User();
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
    public User registerCoordinator(String name, String surname, String email, String password, String repeatPassword) {
        if (email==null || email.isEmpty()  || password==null || password.isEmpty())
            throw new InvalidEmailOrPasswordException();
        if (!password.equals(repeatPassword))
            throw new PasswordsDoNotMatchException();
        if(this.userRepository.findByEmail(email).isPresent())
            throw new EmailAlreadyExistsException();
        User user = new User();
        Coordinator coordinator= new Coordinator();
        Role role=Role.ROLE_COORDINATOR;
        coordinator.setRole(role);
        coordinator.setName(name);
        coordinator.setSurname(surname);
        coordinator.setEmail(email);
        coordinator.setPassword(passwordEncoder.encode(password));
        return coordinatorRepository.save(coordinator);
    }

    @Override
    public User FindByEmail(String email) {
        User user=  this.userRepository.findByEmail(email).orElseThrow(InvalidEmailException::new);
        return user;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
