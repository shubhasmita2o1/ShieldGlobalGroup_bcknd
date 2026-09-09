package com.shielldglobalgroup.admin.service;

import com.shielldglobalgroup.admin.entity.JourneyMilestone;
import com.shielldglobalgroup.admin.exception.ResourceNotFoundException;
import com.shielldglobalgroup.admin.repository.JourneyMilestoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JourneyMilestoneService {

    private final JourneyMilestoneRepository repo;

    public List<JourneyMilestone> getAll() {
        return repo.findAllByOrderByDisplayOrderAsc();
    }

    public List<JourneyMilestone> getActive() {
        return repo.findByIsActiveTrueOrderByDisplayOrderAsc();
    }

    public JourneyMilestone getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Journey milestone", id));
    }

    @Transactional
    public JourneyMilestone create(JourneyMilestone incoming) {
        validate(incoming, true);
        if (incoming.getDisplayOrder() == null) {
            List<JourneyMilestone> all = getAll();
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
    public JourneyMilestone update(Long id, JourneyMilestone incoming) {
        JourneyMilestone existing = getById(id);
        if (incoming.getYear() != null) existing.setYear(incoming.getYear());
        if (incoming.getIndexLabel() != null) existing.setIndexLabel(incoming.getIndexLabel());
        if (incoming.getName() != null) existing.setName(incoming.getName());
        if (incoming.getSubtitle() != null) existing.setSubtitle(incoming.getSubtitle());
        if (incoming.getDescription() != null) existing.setDescription(incoming.getDescription());
        if (incoming.getTag() != null) existing.setTag(incoming.getTag());
        if (incoming.getFlag() != null) existing.setFlag(incoming.getFlag());
        if (incoming.getIsActive() != null) existing.setIsActive(incoming.getIsActive());
        if (incoming.getDisplayOrder() != null) existing.setDisplayOrder(incoming.getDisplayOrder());
        validate(existing, false);
        return repo.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        JourneyMilestone entity = getById(id);
        repo.delete(entity);
        normalizeOrder();
    }

    @Transactional
    public List<JourneyMilestone> reorder(List<Long> orderedIds) {
        if (orderedIds == null || orderedIds.isEmpty()) {
            throw new IllegalArgumentException("orderedIds must not be empty");
        }
        List<JourneyMilestone> all = getAll();
        Map<Long, JourneyMilestone> byId = new HashMap<>();
        for (JourneyMilestone e : all) {
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
            JourneyMilestone e = byId.get(orderedIds.get(i));
            e.setDisplayOrder(10_000 + i);
            repo.save(e);
        }
        repo.flush();
        for (int i = 0; i < orderedIds.size(); i++) {
            JourneyMilestone e = byId.get(orderedIds.get(i));
            e.setDisplayOrder(i + 1);
            repo.save(e);
        }
        return getAll();
    }

    private void normalizeOrder() {
        List<JourneyMilestone> all = getAll();
        for (int i = 0; i < all.size(); i++) {
            JourneyMilestone e = all.get(i);
            int expected = i + 1;
            if (e.getDisplayOrder() == null || e.getDisplayOrder() != expected) {
                e.setDisplayOrder(expected);
                repo.save(e);
            }
        }
    }

    private void validate(JourneyMilestone e, boolean creating) {
        if (e.getYear() == null || e.getYear().isBlank()) {
            throw new IllegalArgumentException("year is required");
        }
        if (e.getName() == null || e.getName().isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (e.getDescription() == null || e.getDescription().isBlank()) {
            throw new IllegalArgumentException("description is required");
        }
    }
}
