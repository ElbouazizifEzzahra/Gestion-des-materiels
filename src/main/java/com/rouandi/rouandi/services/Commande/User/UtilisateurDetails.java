package com.rouandi.rouandi.services.Commande.User;

import com.rouandi.rouandi.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.rouandi.rouandi.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class UtilisateurDetails implements UserDetailsService {

@Autowired
    private UserRepository userRepository;

@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority( user.getRole().name()))

                );

}

}

