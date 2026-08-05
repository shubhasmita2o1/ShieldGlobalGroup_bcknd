package com.shielldglobalgroup.admin.service;

import com.shielldglobalgroup.admin.entity.AdminUser;
import com.shielldglobalgroup.admin.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminUserService {
     private final AdminUserRepository adminRepo;
    private final PasswordEncoder passwordEncoder;

    // Verify login credentials
    public Optional<AdminUser> login(String username, String rawPassword) {
        return adminRepo.findByUsername(username)
            .filter(user ->
                passwordEncoder.matches(rawPassword, user.getPasswordHash())
            );
    }

    // Create first admin (run once during setup)
    public AdminUser createAdmin(String username, String rawPassword) {
        AdminUser user = new AdminUser();
        user.setUsername(username);
        // hash the password before saving — never store plain text
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        return adminRepo.save(user);
    }

    // Update last login time
    public void updateLastLogin(String username) {
        adminRepo.findByUsername(username).ifPresent(user -> {
            user.setLastLogin(LocalDateTime.now());
            adminRepo.save(user);
        });
    }
}
