package com.shielldglobalgroup.admin.repository;

import com.shielldglobalgroup.admin.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    List<Partner> findAllByOrderByDisplayOrderAsc();

    List<Partner> findByIsActiveTrueOrderByDisplayOrderAsc();
}