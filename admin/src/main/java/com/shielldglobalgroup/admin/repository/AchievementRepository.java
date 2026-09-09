package com.shielldglobalgroup.admin.repository;

import com.shielldglobalgroup.admin.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    List<Achievement> findAllByOrderByDisplayOrderAsc();

    List<Achievement> findByIsActiveTrueOrderByDisplayOrderAsc();
}