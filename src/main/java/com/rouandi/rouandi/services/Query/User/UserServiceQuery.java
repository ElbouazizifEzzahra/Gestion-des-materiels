package com.rouandi.rouandi.services.Query.User;

import com.rouandi.rouandi.entities.Role;
import com.rouandi.rouandi.entities.User;

import java.util.List;

public interface UserServiceQuery {
    public List<User> users();
    public List<User> filterByRole(Role role);
}
