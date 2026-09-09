package com.shielldglobalgroup.admin.repository;

import com.shielldglobalgroup.admin.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findAllByOrderByDisplayOrderAsc();

    List<Company> findByIsActiveTrueOrderByDisplayOrderAsc();

    Optional<Company> findBySlug(String slug);

    Optional<Company> findBySlugAndIsActiveTrue(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);
}