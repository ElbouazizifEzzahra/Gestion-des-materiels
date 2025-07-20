package com.rouandi.rouandi.services.Query.User;

import com.rouandi.rouandi.entities.Role;
import com.rouandi.rouandi.entities.User;
import com.rouandi.rouandi.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceQueryImp implements UserServiceQuery {

    private UserRepository userRepository;

    public UserServiceQueryImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> users() {
        return userRepository.findAll();
    }

    @Override
    public List<User> filterByRole(Role role) {
        return userRepository.findByRole(role);
    }
}
