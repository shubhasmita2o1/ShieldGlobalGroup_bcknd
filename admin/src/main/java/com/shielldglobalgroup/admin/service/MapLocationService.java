package com.shielldglobalgroup.admin.service;

import com.shielldglobalgroup.admin.entity.MapLocation;
import com.shielldglobalgroup.admin.exception.ResourceNotFoundException;
import com.shielldglobalgroup.admin.repository.MapLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MapLocationService {

    public static final Set<String> ALLOWED_REGIONS = Set.of(
            "South Asia",
            "Southeast Asia",
            "Middle East",
            "Africa",
            "Europe",
            "North America"
    );

    public static final Set<String> ALLOWED_KINDS = Set.of(
            "office",
            "local-recruitment",
            "recruitment-associate"
    );

    private final MapLocationRepository locationRepo;

    public List<MapLocation> getAll() {
        return locationRepo.findAllByOrderByDisplayOrderAsc();
    }

    public List<MapLocation> getActive() {
        return locationRepo.findByIsActiveTrueOrderByDisplayOrderAsc();
    }

    public MapLocation getById(Long id) {
        return locationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Map location", id));
    }

    public MapLocation getActiveById(Long id) {
        MapLocation loc = getById(id);
        if (!Boolean.TRUE.equals(loc.getIsActive())) {
            throw new ResourceNotFoundException("Map location", id);
        }
        return loc;
    }

    @Transactional
    public MapLocation create(MapLocation incoming) {
        validate(incoming);

        if (incoming.getDisplayOrder() == null) {
            List<MapLocation> all = getAll();
            int next = all.isEmpty() ? 1 : all.get(all.size() - 1).getDisplayOrder() + 1;
            incoming.setDisplayOrder(next);
        }
        if (incoming.getIsActive() == null) {
            incoming.setIsActive(true);
        }
        incoming.setId(null);
        return locationRepo.save(incoming);
    }

    @Transactional
    public MapLocation update(Long id, MapLocation incoming) {
        MapLocation existing = getById(id);

        if (incoming.getName() != null) existing.setName(incoming.getName());
        if (incoming.getCountry() != null) existing.setCountry(incoming.getCountry());
        if (incoming.getLatitude() != null) existing.setLatitude(incoming.getLatitude());
        if (incoming.getLongitude() != null) existing.setLongitude(incoming.getLongitude());
        if (incoming.getRegion() != null) existing.setRegion(incoming.getRegion());
        if (incoming.getKind() != null) existing.setKind(incoming.getKind());
        if (incoming.getIsActive() != null) existing.setIsActive(incoming.getIsActive());
        if (incoming.getDisplayOrder() != null) existing.setDisplayOrder(incoming.getDisplayOrder());

        validate(existing);
        return locationRepo.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        MapLocation loc = getById(id);
        locationRepo.delete(loc);
        normalizeOrder();
    }

    @Transactional
    public List<MapLocation> reorder(List<Long> orderedIds) {
        if (orderedIds == null || orderedIds.isEmpty()) {
            throw new IllegalArgumentException("orderedIds must not be empty");
        }

        List<MapLocation> all = getAll();
        Map<Long, MapLocation> byId = new HashMap<>();
        for (MapLocation loc : all) {
            byId.put(loc.getId(), loc);
        }

        if (orderedIds.size() != all.size()) {
            throw new IllegalArgumentException(
                    "orderedIds size must match total locations (" + all.size() + ")");
        }
        for (Long id : orderedIds) {
            if (!byId.containsKey(id)) {
                throw new IllegalArgumentException("Unknown map location id: " + id);
            }
        }

        for (int i = 0; i < orderedIds.size(); i++) {
            MapLocation loc = byId.get(orderedIds.get(i));
            loc.setDisplayOrder(10_000 + i);
            locationRepo.save(loc);
        }
        locationRepo.flush();

        for (int i = 0; i < orderedIds.size(); i++) {
            MapLocation loc = byId.get(orderedIds.get(i));
            loc.setDisplayOrder(i + 1);
            locationRepo.save(loc);
        }

        return getAll();
    }

    private void normalizeOrder() {
        List<MapLocation> all = getAll();
        for (int i = 0; i < all.size(); i++) {
            MapLocation loc = all.get(i);
            int expected = i + 1;
            if (loc.getDisplayOrder() == null || loc.getDisplayOrder() != expected) {
                loc.setDisplayOrder(expected);
                locationRepo.save(loc);
            }
        }
    }

    private void validate(MapLocation loc) {
        if (loc.getName() == null || loc.getName().isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (loc.getLatitude() == null) {
            throw new IllegalArgumentException("latitude is required");
        }
        if (loc.getLongitude() == null) {
            throw new IllegalArgumentException("longitude is required");
        }
        if (loc.getLatitude() < -90 || loc.getLatitude() > 90) {
            throw new IllegalArgumentException("latitude must be between -90 and 90");
        }
        if (loc.getLongitude() < -180 || loc.getLongitude() > 180) {
            throw new IllegalArgumentException("longitude must be between -180 and 180");
        }
        if (loc.getRegion() == null || loc.getRegion().isBlank()) {
            throw new IllegalArgumentException("region is required");
        }
        if (!ALLOWED_REGIONS.contains(loc.getRegion())) {
            throw new IllegalArgumentException(
                    "region must be one of: " + String.join(", ", ALLOWED_REGIONS));
        }
        if (loc.getKind() == null || loc.getKind().isBlank()) {
            throw new IllegalArgumentException("kind is required");
        }
        if (!ALLOWED_KINDS.contains(loc.getKind())) {
            throw new IllegalArgumentException(
                    "kind must be one of: " + String.join(", ", ALLOWED_KINDS));
        }
    }
}
