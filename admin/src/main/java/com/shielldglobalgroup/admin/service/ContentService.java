package com.shielldglobalgroup.admin.service;

import com.shielldglobalgroup.admin.entity.ContentBlock;
import com.shielldglobalgroup.admin.entity.ContentListItem;
import com.shielldglobalgroup.admin.repository.ContentBlockRepository;
import com.shielldglobalgroup.admin.repository.ContentListItemRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;  
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentService {
    
    private final ContentBlockRepository blockRepo;
    private final ContentListItemRepository listRepo;

     // ── GET: all blocks for a page as a flat Map ──────────────────────────
    // returns { "section_key.field" : "value" }
    // e.g. { "founders.para_1": "At Shield Global...", "founders.image_path": "/uploads/founder.jpg" }
    public Map<String, String> getPageContent(String pageKey) {
        List<ContentBlock> blocks = blockRepo.findByPageKey(pageKey);
        return blocks.stream().collect(Collectors.toMap(
            b -> b.getSectionKey() + "." + b.getField(),
            b -> b.getValue() != null ? b.getValue() : ""
        ));
    }


    // ── GET: blocks for one section only ──────────────────────────────────
    public Map<String, String> getSectionContent(String pageKey, String sectionKey) {
        List<ContentBlock> blocks = blockRepo.findByPageKeyAndSectionKey(pageKey, sectionKey);
        return blocks.stream().collect(Collectors.toMap(
            ContentBlock::getField,
            b -> b.getValue() != null ? b.getValue() : ""
        ));
    }

    // ── GET: one specific field value ─────────────────────────────────────
    public String getField(String pageKey, String sectionKey, String field) {
        return blockRepo
            .findByPageKeyAndSectionKeyAndField(pageKey, sectionKey, field)
            .map(ContentBlock::getValue)
            .orElse("");
    }

    // ── SAVE or UPDATE: one field ─────────────────────────────────────────
    // if row exists → update value
    // if row doesn't exist → create new row
    @Transactional
    public ContentBlock saveField(String pageKey, String sectionKey,
                                   String field, String value) {
        ContentBlock block = blockRepo
            .findByPageKeyAndSectionKeyAndField(pageKey, sectionKey, field)
            .orElse(new ContentBlock());

        block.setPageKey(pageKey);
        block.setSectionKey(sectionKey);
        block.setField(field);
        block.setValue(value);

        return blockRepo.save(block);
    }

    // ── SAVE: multiple fields at once (admin saves whole section) ─────────
    // input map: { "field": "value", "field2": "value2" }
    @Transactional
    public void saveSectionFields(String pageKey, String sectionKey,
                                   Map<String, String> fields) {
        fields.forEach((field, value) ->
            saveField(pageKey, sectionKey, field, value)
        );
    }

    // ── GET: bullet list items ────────────────────────────────────────────
    public List<ContentListItem> getListItems(String pageKey,
                                               String sectionKey,
                                               String listType) {
        return listRepo.findByPageKeyAndSectionKeyAndListTypeOrderByOrderIndexAsc(
            pageKey, sectionKey, listType
        );
    }

    // ── GET: all list items for a section (all listTypes) ─────────────────
    public List<ContentListItem> getAllListItemsForSection(String pageKey,
                                                            String sectionKey) {
        return listRepo.findByPageKeyAndSectionKeyOrderByOrderIndexAsc(
            pageKey, sectionKey
        );
    }

    // ── SAVE: bullet list (delete old, insert new) ────────────────────────
    @Transactional
    public void saveListItems(String pageKey, String sectionKey,
                               String listType, List<String> items) {
        // delete existing items for this list
        listRepo.deleteByPageKeyAndSectionKeyAndListType(pageKey, sectionKey, listType);

        // insert new items with order index
        for (int i = 0; i < items.size(); i++) {
            ContentListItem item = new ContentListItem();
            item.setPageKey(pageKey);
            item.setSectionKey(sectionKey);
            item.setListType(listType);
            item.setOrderIndex(i + 1);
            item.setItemText(items.get(i));
            listRepo.save(item);
        }
    }

    
}
