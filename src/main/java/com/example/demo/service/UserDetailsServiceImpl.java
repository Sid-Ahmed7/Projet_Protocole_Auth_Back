package com.example.demo.service;

import com.example.demo.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        User domainUser = userService.findByUsername(username);
        if (domainUser == null) {
            throw new UsernameNotFoundException("Could not find user with name '" + username + "'");
        }

        return domainUser;
    }

    public UserDetails loadUserByUuid(UUID uuid) throws UsernameNotFoundException, DataAccessException {
        User domainUser = userService.findByUuid(uuid);
        if (domainUser == null) {
            throw new UsernameNotFoundException("Could not find user with UUID '" + uuid + "'");
        }

        return domainUser;
    }
}
