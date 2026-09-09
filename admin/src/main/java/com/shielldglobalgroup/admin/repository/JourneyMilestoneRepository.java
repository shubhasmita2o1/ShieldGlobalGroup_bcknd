package com.shielldglobalgroup.admin.repository;

import com.shielldglobalgroup.admin.entity.JourneyMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JourneyMilestoneRepository extends JpaRepository<JourneyMilestone, Long> {

    List<JourneyMilestone> findAllByOrderByDisplayOrderAsc();

    List<JourneyMilestone> findByIsActiveTrueOrderByDisplayOrderAsc();
}