package com.infinite.busTicket.service;

import com.infinite.busTicket.config.SecurityConfig;
import com.infinite.busTicket.entity.Role;
import com.infinite.busTicket.entity.Users;
import com.infinite.busTicket.entity.request.ChangePasswordRequest;
import com.infinite.busTicket.entity.request.ProfileUpdateRequest;
import com.infinite.busTicket.entity.request.RegisterRequest;
import com.infinite.busTicket.entity.dto.UserDTO;
import com.infinite.busTicket.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
                user.setRegisteredOn(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
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
    public List<UserDTO> getAll() {
        List<Users> user=userRepository.findAll();
        List<UserDTO> userDTOList =new ArrayList<>();
        user.forEach(
                x->{
                    userDTOList.add(modelMapper.map(x, UserDTO.class));
                }
        );
        return userDTOList;
    }

    @Override
    public void deleteUser(Long id) {
        Users user=userRepository.findById(id).orElse(new Users());
        if(!user.getRoles().equals("admin")) {
            userRepository.deleteById(id);
        }
    }

    @Override
    public UserDTO getByUsername(String username) {
        Users user=userRepository.findByUsername(username);
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO getById(Long id) {
        Users user=userRepository.findById(id).orElse(new Users());
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public boolean updateUser(Long id, ProfileUpdateRequest profile) {
        Users user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return false;
        }

        boolean usernameExists = userRepository.existsByUsername(profile.getUsername()) && !user.getUsername().equals(profile.getUsername());
        boolean emailExists = userRepository.existsByEmail(profile.getEmail()) && !user.getEmail().equals(profile.getEmail());

        if (!usernameExists && !emailExists) {
            user.setUsername(profile.getUsername());
            user.setEmail(profile.getEmail());
            userRepository.save(user);
            return true;
        }
        return false;
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

    @Override
    public UserDTO getByEmail(String email) {
        Users user=userRepository.findByEmail(email);
        return modelMapper.map(user,UserDTO.class);
    }
}
