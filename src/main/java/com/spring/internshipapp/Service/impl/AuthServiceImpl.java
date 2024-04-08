package com.spring.internshipapp.Service.impl;

import com.spring.internshipapp.Model.Exceptions.InvalidArgumentsException;
import com.spring.internshipapp.Model.Exceptions.InvalidUserCredentialsException;
import com.spring.internshipapp.Model.User;
import com.spring.internshipapp.Repository.UserRepository;
import com.spring.internshipapp.Service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String email, String password) {
        if (email==null || email.isEmpty() || password==null || password.isEmpty()) {
            throw new InvalidArgumentsException();
        }
        return userRepository.findByEmailAndPassword(email,
                password).orElseThrow(InvalidUserCredentialsException::new);
    }
}
