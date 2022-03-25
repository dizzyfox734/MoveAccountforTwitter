package com.my.MoveAccountforTwitter.springboot.service;

import com.my.MoveAccountforTwitter.springboot.dto.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


// Don't use but only for securityconfig
@Service
public class UserService implements UserDetailsService {

    private User user;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
