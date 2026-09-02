package com.shielldglobalgroup.admin.controller;

import com.shielldglobalgroup.admin.dto.ApiResponseDTO;
import com.shielldglobalgroup.admin.dto.LoginRequestDTO;
import com.shielldglobalgroup.admin.dto.LoginResponseDTO;
import com.shielldglobalgroup.admin.entity.AdminUser;
import com.shielldglobalgroup.admin.repository.AdminUserRepository;
import com.shielldglobalgroup.admin.security.JwtUtil;
import com.shielldglobalgroup.admin.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AdminUserService adminUserService;
    private final AdminUserRepository adminUserRepository;
    private final JwtUtil jwtUtil;

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

    @GetMapping("/me")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getPrincipal() == null
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(401).body(
                    ApiResponseDTO.error("Unauthorized — valid JWT required")
            );
        }

        String username = authentication.getPrincipal().toString();

        Optional<AdminUser> userOpt = adminUserRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(
                    ApiResponseDTO.error("User not found")
            );
        }

        AdminUser user = userOpt.get();
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("role", user.getRole());
        data.put("lastLogin", user.getLastLogin());
        data.put("createdAt", user.getCreatedAt());

        return ResponseEntity.ok(ApiResponseDTO.ok("Current admin", data));
    }

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