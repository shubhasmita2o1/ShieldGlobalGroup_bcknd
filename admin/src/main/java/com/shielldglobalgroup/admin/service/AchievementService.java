package com.shielldglobalgroup.admin.service;

import com.shielldglobalgroup.admin.entity.Achievement;
import com.shielldglobalgroup.admin.exception.ResourceNotFoundException;
import com.shielldglobalgroup.admin.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementRepository repo;

    public List<Achievement> getAll() {
        return repo.findAllByOrderByDisplayOrderAsc();
    }

    public List<Achievement> getActive() {
        return repo.findByIsActiveTrueOrderByDisplayOrderAsc();
    }

    public Achievement getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement", id));
    }

    @Transactional
    public Achievement create(Achievement incoming) {
        validate(incoming, true);
        if (incoming.getDisplayOrder() == null) {
            List<Achievement> all = getAll();
            int next = all.isEmpty() ? 1 : all.get(all.size() - 1).getDisplayOrder() + 1;
            incoming.setDisplayOrder(next);
        }
        if (incoming.getIsActive() == null) {
            incoming.setIsActive(true);
        }
        incoming.setId(null);
        return repo.save(incoming);
    }

    @Transactional
    public Achievement update(Long id, Achievement incoming) {
        Achievement existing = getById(id);
        if (incoming.getLabel() != null) existing.setLabel(incoming.getLabel());
        if (incoming.getValue() != null) existing.setValue(incoming.getValue());
        if (incoming.getSuffix() != null) existing.setSuffix(incoming.getSuffix());
        if (incoming.getDetail() != null) existing.setDetail(incoming.getDetail());
        if (incoming.getIconKey() != null) existing.setIconKey(incoming.getIconKey());
        if (incoming.getIsActive() != null) existing.setIsActive(incoming.getIsActive());
        if (incoming.getDisplayOrder() != null) existing.setDisplayOrder(incoming.getDisplayOrder());
        validate(existing, false);
        return repo.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Achievement entity = getById(id);
        repo.delete(entity);
        normalizeOrder();
    }

    @Transactional
    public List<Achievement> reorder(List<Long> orderedIds) {
        if (orderedIds == null || orderedIds.isEmpty()) {
            throw new IllegalArgumentException("orderedIds must not be empty");
        }
        List<Achievement> all = getAll();
        Map<Long, Achievement> byId = new HashMap<>();
        for (Achievement e : all) {
            byId.put(e.getId(), e);
        }
        if (orderedIds.size() != all.size()) {
            throw new IllegalArgumentException(
                    "orderedIds size must match total items (" + all.size() + ")");
        }
        for (Long oid : orderedIds) {
            if (!byId.containsKey(oid)) {
                throw new IllegalArgumentException("Unknown id: " + oid);
            }
        }
        for (int i = 0; i < orderedIds.size(); i++) {
            Achievement e = byId.get(orderedIds.get(i));
            e.setDisplayOrder(10_000 + i);
            repo.save(e);
        }
        repo.flush();
        for (int i = 0; i < orderedIds.size(); i++) {
            Achievement e = byId.get(orderedIds.get(i));
            e.setDisplayOrder(i + 1);
            repo.save(e);
        }
        return getAll();
    }

    private void normalizeOrder() {
        List<Achievement> all = getAll();
        for (int i = 0; i < all.size(); i++) {
            Achievement e = all.get(i);
            int expected = i + 1;
            if (e.getDisplayOrder() == null || e.getDisplayOrder() != expected) {
                e.setDisplayOrder(expected);
                repo.save(e);
            }
        }
    }

    private void validate(Achievement e, boolean creating) {
        if (e.getLabel() == null || e.getLabel().isBlank()) {
            throw new IllegalArgumentException("label is required");
        }
        if (e.getValue() == null) {
            throw new IllegalArgumentException("value is required");
        }
    }
}