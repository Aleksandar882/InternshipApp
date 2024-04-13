package com.spring.internshipapp.Service;

import com.spring.internshipapp.Model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User register(Long index,String name, String surname,String email, String password, String repeatPassword);

    User registerCompany(String name, String description, String address, Integer number,String image_url, String email, String password, String repeatPassword);

    User registerCoordinator(String name, String surname,String email, String password, String repeatPassword);

    User FindByEmail(String email);

}
