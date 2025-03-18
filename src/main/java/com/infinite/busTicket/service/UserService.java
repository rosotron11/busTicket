package com.infinite.busTicket.service;

import com.infinite.busTicket.entity.Users;
import com.infinite.busTicket.entity.request.ChangePasswordRequest;
import com.infinite.busTicket.entity.request.ProfileUpdateRequest;
import com.infinite.busTicket.entity.request.RegisterRequest;
import com.infinite.busTicket.entity.response.UserResponse;

import java.util.List;

public interface UserService {
    boolean saveUser(RegisterRequest req);

    List<UserResponse> getAll();

    void deleteUser(Long id);

    UserResponse getByUsername(String username);

    UserResponse getById(Long id);

    boolean updateUser(Long id, ProfileUpdateRequest profile);

    boolean changePassword(Long id, ChangePasswordRequest req);
}
