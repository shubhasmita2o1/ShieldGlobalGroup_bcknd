package com.shielldglobalgroup.admin.repository;

import com.shielldglobalgroup.admin.entity.ContentBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ContentBlockRepository extends JpaRepository<ContentBlock, Long> {
    
    // Get all blocks for a page e.g. all "about" page content
    List<ContentBlock> findByPageKey(String pageKey);

    // Get all blocks for a specific section e.g. all "esg_ethical" fields
    List<ContentBlock> findByPageKeyAndSectionKey(String pageKey, String sectionKey);

    // Get one specific field e.g. page="about", section="founders", field="para_1"
    Optional<ContentBlock> findByPageKeyAndSectionKeyAndField(
        String pageKey, String sectionKey, String field
    );
}
