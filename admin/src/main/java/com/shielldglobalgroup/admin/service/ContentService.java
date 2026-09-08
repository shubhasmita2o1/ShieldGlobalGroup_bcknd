package com.shielldglobalgroup.admin.service;

import com.shielldglobalgroup.admin.entity.ContentBlock;
import com.shielldglobalgroup.admin.entity.ContentListItem;
import com.shielldglobalgroup.admin.exception.ResourceNotFoundException;
import com.shielldglobalgroup.admin.repository.ContentBlockRepository;
import com.shielldglobalgroup.admin.repository.ContentListItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentBlockRepository blockRepo;
    private final ContentListItemRepository listRepo;

    public Map<String, String> getPageContent(String pageKey) {
        List<ContentBlock> blocks = blockRepo.findByPageKey(pageKey);
        return blocks.stream().collect(Collectors.toMap(
                b -> b.getSectionKey() + "." + b.getField(),
                b -> b.getValue() != null ? b.getValue() : "",
                (a, b) -> b
        ));
    }

    public Map<String, String> getSectionContent(String pageKey, String sectionKey) {
        List<ContentBlock> blocks = blockRepo.findByPageKeyAndSectionKey(pageKey, sectionKey);
        return blocks.stream().collect(Collectors.toMap(
                ContentBlock::getField,
                b -> b.getValue() != null ? b.getValue() : "",
                (a, b) -> b
        ));
    }

    public String getField(String pageKey, String sectionKey, String field) {
        return blockRepo
                .findByPageKeyAndSectionKeyAndField(pageKey, sectionKey, field)
                .map(ContentBlock::getValue)
                .orElse("");
    }

    @Transactional
    public ContentBlock saveField(String pageKey, String sectionKey, String field, String value) {
        ContentBlock block = blockRepo
                .findByPageKeyAndSectionKeyAndField(pageKey, sectionKey, field)
                .orElse(new ContentBlock());

        block.setPageKey(pageKey);
        block.setSectionKey(sectionKey);
        block.setField(field);
        block.setValue(value);

        return blockRepo.save(block);
    }

    @Transactional
    public void saveSectionFields(String pageKey, String sectionKey, Map<String, String> fields) {
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("fields map must not be empty");
        }
        fields.forEach((field, value) -> saveField(pageKey, sectionKey, field, value));
    }

    @Transactional
    public void deleteField(String pageKey, String sectionKey, String field) {
        ContentBlock block = blockRepo
                .findByPageKeyAndSectionKeyAndField(pageKey, sectionKey, field)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Field not found: " + pageKey + "/" + sectionKey + "/" + field));
        blockRepo.delete(block);
    }

    public List<ContentListItem> getListItems(String pageKey, String sectionKey, String listType) {
        return listRepo.findByPageKeyAndSectionKeyAndListTypeOrderByOrderIndexAsc(
                pageKey, sectionKey, listType);
    }

    public List<ContentListItem> getAllListItemsForSection(String pageKey, String sectionKey) {
        return listRepo.findByPageKeyAndSectionKeyOrderByOrderIndexAsc(pageKey, sectionKey);
    }

    @Transactional
    public void saveListItems(String pageKey, String sectionKey, String listType, List<String> items) {
        if (items == null) {
            throw new IllegalArgumentException("items must not be null");
        }
        listRepo.deleteByPageKeyAndSectionKeyAndListType(pageKey, sectionKey, listType);

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

    @Transactional
    public ContentListItem appendListItem(String pageKey, String sectionKey, String listType, String itemText) {
        List<ContentListItem> existing = getListItems(pageKey, sectionKey, listType);
        int nextIndex = existing.isEmpty() ? 1 : existing.get(existing.size() - 1).getOrderIndex() + 1;

        ContentListItem item = new ContentListItem();
        item.setPageKey(pageKey);
        item.setSectionKey(sectionKey);
        item.setListType(listType);
        item.setOrderIndex(nextIndex);
        item.setItemText(itemText);
        return listRepo.save(item);
    }

    @Transactional
    public ContentListItem updateListItemText(Long id, String itemText) {
        ContentListItem item = listRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("List item", id));
        item.setItemText(itemText);
        return listRepo.save(item);
    }

    @Transactional
    public void deleteListItem(Long id) {
        ContentListItem item = listRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("List item", id));

        String pageKey = item.getPageKey();
        String sectionKey = item.getSectionKey();
        String listType = item.getListType();

        listRepo.delete(item);
        normalizeOrderIndex(pageKey, sectionKey, listType);
    }

    @Transactional
    public void reorderListItems(String pageKey, String sectionKey, String listType, List<Long> orderedIds) {
        if (orderedIds == null || orderedIds.isEmpty()) {
            throw new IllegalArgumentException("orderedIds must not be empty");
        }

        List<ContentListItem> existing = getListItems(pageKey, sectionKey, listType);
        Map<Long, ContentListItem> byId = new HashMap<>();
        for (ContentListItem item : existing) {
            byId.put(item.getId(), item);
        }

        if (orderedIds.size() != existing.size()) {
            throw new IllegalArgumentException(
                    "orderedIds size must match list size (" + existing.size() + ")");
        }

        for (Long id : orderedIds) {
            if (!byId.containsKey(id)) {
                throw new IllegalArgumentException("List item id not in list: " + id);
            }
        }

        for (int i = 0; i < orderedIds.size(); i++) {
            ContentListItem item = byId.get(orderedIds.get(i));
            item.setOrderIndex(i + 1);
            listRepo.save(item);
        }
    }

    private void normalizeOrderIndex(String pageKey, String sectionKey, String listType) {
        List<ContentListItem> items = getListItems(pageKey, sectionKey, listType);
        for (int i = 0; i < items.size(); i++) {
            ContentListItem item = items.get(i);
            int expected = i + 1;
            if (item.getOrderIndex() == null || item.getOrderIndex() != expected) {
                item.setOrderIndex(expected);
                listRepo.save(item);
            }
        }
    }
}
