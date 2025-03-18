package com.infinite.busTicket.service;

import com.infinite.busTicket.config.SecurityConfig;
import com.infinite.busTicket.entity.Users;
import com.infinite.busTicket.entity.request.ChangePasswordRequest;
import com.infinite.busTicket.entity.request.ProfileUpdateRequest;
import com.infinite.busTicket.entity.request.RegisterRequest;
import com.infinite.busTicket.entity.response.UserResponse;
import com.infinite.busTicket.repository.UserRepository;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private ModelMapper modelMapper;

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
    public List<UserResponse> getAll() {
        List<Users> user=userRepository.findAll();
        List<UserResponse> userResponseList=new ArrayList<>();
        user.forEach(
                x->{
                    userResponseList.add(modelMapper.map(x,UserResponse.class));
                }
        );
        return userResponseList;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponse getByUsername(String username) {
        Users user=userRepository.findByUsername(username);
        return modelMapper.map(user,UserResponse.class);
    }

    @Override
    public UserResponse getById(Long id) {
        Users user=userRepository.findById(id).orElse(new Users());
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public boolean updateUser(Long id, ProfileUpdateRequest profile) {
        Users user=userRepository.findById(id).orElse(new Users());
        user.setUsername(profile.getUsername());
        user.setEmail(profile.getEmail());
        user.setRoles(profile.getRoles());
        userRepository.save(user);
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
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
