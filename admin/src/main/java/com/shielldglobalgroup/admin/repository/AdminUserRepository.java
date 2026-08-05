package com.shielldglobalgroup.admin.repository;

import com.shielldglobalgroup.admin.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

    // Find admin by username — used during login
    Optional<AdminUser> findByUsername(String username);
}
