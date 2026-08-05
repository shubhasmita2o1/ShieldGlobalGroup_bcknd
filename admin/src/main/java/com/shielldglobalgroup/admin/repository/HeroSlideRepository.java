package com.shielldglobalgroup.admin.repository;

import com.shielldglobalgroup.admin.entity.HeroSlide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeroSlideRepository extends JpaRepository<HeroSlide, Long> {

    // Get slides in order (1, 2, 3, 4, 5)
    List<HeroSlide> findAllByOrderBySlideOrderAsc();

    // Get one specific slide by its order number
    Optional<HeroSlide> findBySlideOrder(Integer slideOrder);
}
