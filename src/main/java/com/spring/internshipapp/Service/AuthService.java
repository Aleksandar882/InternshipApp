package com.spring.internshipapp.Service;

import com.spring.internshipapp.Model.User;

public interface AuthService {

    User login(String email, String password);

}
