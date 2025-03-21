package com.infinite.busTicket.service;

import com.infinite.busTicket.entity.request.ChangePasswordRequest;
import com.infinite.busTicket.entity.request.ProfileUpdateRequest;
import com.infinite.busTicket.entity.request.RegisterRequest;
import com.infinite.busTicket.entity.dto.UserDTO;

import java.util.List;

public interface UserService {
    boolean saveUser(RegisterRequest req);

    List<UserDTO> getAll();

    void deleteUser(Long id);

    UserDTO getByUsername(String username);

    UserDTO getById(Long id);

    boolean updateUser(Long id, ProfileUpdateRequest profile);

    boolean changePassword(Long id, ChangePasswordRequest req);

    UserDTO getByEmail(String email);
}
