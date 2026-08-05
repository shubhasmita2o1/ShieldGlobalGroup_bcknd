package com.shielldglobalgroup.admin.repository;

import com.shielldglobalgroup.admin.entity.ContentListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentListItemRepository extends JpaRepository<ContentListItem, Long> {
    // Get all bullet points for a section+list
    // e.g. page="about", section="esg_ethical", listType="principles"
    
    List<ContentListItem> findByPageKeyAndSectionKeyAndListTypeOrderByOrderIndexAsc(
        String pageKey, String sectionKey, String listType
    );

    // Get all lists for a section (all list types together)
    List<ContentListItem> findByPageKeyAndSectionKeyOrderByOrderIndexAsc(
        String pageKey, String sectionKey
    );

    // Delete all items of a list (used when admin re-saves a bullet list)
    void deleteByPageKeyAndSectionKeyAndListType(
        String pageKey, String sectionKey, String listType
    );
}
