package com.infinite.busTicket.service;

import com.infinite.busTicket.entity.Users;
import com.infinite.busTicket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user=new Users();
        if(username.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
        {
            user= userRepository.findByEmail(username);
        }
        else
        {
            user= userRepository.findByUsername(username);
        }
        if(user!=null)
        {
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRoles())
                    .build();
        }
        throw new UsernameNotFoundException("User with username: "+username+" not found");
    }
}
