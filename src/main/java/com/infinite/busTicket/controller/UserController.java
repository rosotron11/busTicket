package com.infinite.busTicket.controller;

import com.infinite.busTicket.config.JwtUtil;
import com.infinite.busTicket.entity.request.ChangePasswordRequest;
import com.infinite.busTicket.entity.request.LoginRequest;
import com.infinite.busTicket.entity.request.RegisterRequest;
import com.infinite.busTicket.entity.response.LoginResponse;
import com.infinite.busTicket.entity.Users;
import com.infinite.busTicket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<?> createPassenger(@RequestBody RegisterRequest request)
    {
        boolean registerCheck= userService.saveUser(request);
        if(registerCheck) {
            LoginRequest res = new LoginRequest(request.getUsername(),
                    request.getPassword(),
                    request.getEmail());
            return new ResponseEntity<>(login(res), HttpStatus.OK);
        }
        return new ResponseEntity<>("Username or email exists",HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUser()
    {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id)
    {
        userService.deleteUser(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Users user)
    {
        userService.updateUser(id,user);
        return new ResponseEntity<>("Updated", HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id)
    {
        return new ResponseEntity<>(userService.getById(id),HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request)
    {
        try
        {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            UserDetails userDetails=userDetailsService.loadUserByUsername(request.getUsername());
            Users user=userService.getByUsername(request.getUsername());
            String jwt=jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(new LoginResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRoles(),
                    jwt
            ),HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Please Enter Correct Details", HttpStatus.OK);
        }
    }

    @PostMapping("/users/{id}/changepassword")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody ChangePasswordRequest req)
    {
        if(id== req.getId()) {
            if (userService.changePassword(id, req)) {
                return new ResponseEntity<>("Password Changed", HttpStatus.OK);
            }
            return new ResponseEntity<>("Enter the correct current password", HttpStatus.OK);
        }
        return new ResponseEntity<>("ID Mismatch", HttpStatus.BAD_REQUEST);
    }
}
