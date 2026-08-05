package com.shielldglobalgroup.admin.repository;

import com.shielldglobalgroup.admin.entity.MapPin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MapPinRepository extends JpaRepository<MapPin, Long> {
    // Find a pin by its country/city label
    Optional<MapPin> findByLabel(String label);
}
