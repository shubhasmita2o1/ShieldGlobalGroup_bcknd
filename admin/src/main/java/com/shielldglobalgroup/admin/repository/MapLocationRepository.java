package com.shielldglobalgroup.admin.repository;

import com.shielldglobalgroup.admin.entity.MapLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapLocationRepository extends JpaRepository<MapLocation, Long> {

    List<MapLocation> findAllByOrderByDisplayOrderAsc();

    List<MapLocation> findByIsActiveTrueOrderByDisplayOrderAsc();
}
