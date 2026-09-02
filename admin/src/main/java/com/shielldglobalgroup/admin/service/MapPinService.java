package com.shielldglobalgroup.admin.service;

import com.shielldglobalgroup.admin.entity.MapPin;
// import com.shielldglobalgroup.admin.exception.ResourceNotFoundException;
import com.shielldglobalgroup.admin.repository.MapPinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapPinService {
    private final MapPinRepository mapPinRepo;

    // Get all pins
    public List<MapPin> getAllPins() {
        return mapPinRepo.findAll();
    }

    // Add a new pin
    public MapPin addPin(MapPin pin) {
        return mapPinRepo.save(pin);
    }

    // Update existing pin
    public MapPin updatePin(Long id, MapPin updated) {
        MapPin pin = mapPinRepo.findById(id)
            .orElseThrow(() -> new com.shielldglobalgroup.admin.exception.ResourceNotFoundException("Pin", id));
        pin.setLabel(updated.getLabel());
        pin.setLeftPercent(updated.getLeftPercent());
        pin.setTopPercent(updated.getTopPercent());
        return mapPinRepo.save(pin);
    }

    // Delete a pin
    public void deletePin(Long id) {
        mapPinRepo.deleteById(id);
    }
}
