package com.infinite.busTicket.service;

import com.infinite.busTicket.config.SecurityConfig;
import com.infinite.busTicket.entity.Users;
import com.infinite.busTicket.entity.request.ChangePasswordRequest;
import com.infinite.busTicket.entity.request.RegisterRequest;
import com.infinite.busTicket.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfig securityConfig;

    @Override
    public boolean saveUser(RegisterRequest req) {
        try{
            if(userRepository.findByUsername(req.getUsername())==null)
            {
                Users user=new Users();
                user.setUsername(req.getUsername());
                user.setPassword(securityConfig.passwordEncoder().encode(req.getPassword()));
                user.setRoles(req.getRoles());
                user.setEmail(req.getEmail());
                userRepository.save(user);
                return true;
            }
            return false;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Users> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Users getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Users getById(Long id) {
        return userRepository.findById(id).orElse(new Users());
    }

    @Override
    public boolean updateUser(Long id, Users user) {
        Users newUser=userRepository.findById(id).orElse(new Users());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(securityConfig.passwordEncoder().encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.setRoles(user.getRoles());
        userRepository.save(newUser);
        return true;
    }

    @Override
    public boolean changePassword(Long id, ChangePasswordRequest req) {
        Users user=userRepository.findById(id).orElse(new Users());
        if(securityConfig.passwordEncoder().matches(
                req.getPassword(),
                user.getPassword()
        ))
        {
            user.setPassword(securityConfig.passwordEncoder().encode(req.getNewPassword()));
            return true;
        }
        return false;
    }
}
