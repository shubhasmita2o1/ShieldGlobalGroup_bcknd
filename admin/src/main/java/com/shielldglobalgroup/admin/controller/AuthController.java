package com.shielldglobalgroup.admin.controller;

import com.shielldglobalgroup.admin.dto.ApiResponseDTO;
import com.shielldglobalgroup.admin.dto.LoginRequestDTO;
import com.shielldglobalgroup.admin.dto.LoginResponseDTO;
import com.shielldglobalgroup.admin.entity.AdminUser;
import com.shielldglobalgroup.admin.security.JwtUtil;
import com.shielldglobalgroup.admin.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AdminUserService adminUserService;
    private final JwtUtil jwtUtil;

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(
            @RequestBody LoginRequestDTO request) {

        Optional<AdminUser> user = adminUserService.login(
            request.getUsername(),
            request.getPassword()
        );

        if (user.isPresent()) {
            adminUserService.updateLastLogin(request.getUsername());
            String token = jwtUtil.generateToken(request.getUsername());

            LoginResponseDTO response = new LoginResponseDTO(
                token,
                request.getUsername(),
                "Login successful"
            );

            return ResponseEntity.ok(
                ApiResponseDTO.ok("Login successful", response)
            );
        }

        return ResponseEntity.status(401).body(
            ApiResponseDTO.error("Invalid username or password")
        );
    }

    // POST /api/auth/setup
    // call this ONCE to create first admin, then never use again
    @PostMapping("/setup")
    public ResponseEntity<ApiResponseDTO<String>> setup(
            @RequestBody LoginRequestDTO request) {
        try {
            AdminUser created = adminUserService.createAdmin(
                request.getUsername(),
                request.getPassword()
            );
            return ResponseEntity.ok(
                ApiResponseDTO.ok("Admin created", created.getUsername())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                ApiResponseDTO.error("Setup failed: " + e.getMessage())
            );
        }
    }
}