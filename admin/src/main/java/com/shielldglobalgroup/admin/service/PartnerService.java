package com.shielldglobalgroup.admin.service;

import com.shielldglobalgroup.admin.entity.Partner;
import com.shielldglobalgroup.admin.exception.ResourceNotFoundException;
import com.shielldglobalgroup.admin.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerRepository repo;

    public List<Partner> getAll() {
        return repo.findAllByOrderByDisplayOrderAsc();
    }

    public List<Partner> getActive() {
        return repo.findByIsActiveTrueOrderByDisplayOrderAsc();
    }

    public Partner getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", id));
    }

    @Transactional
    public Partner create(Partner incoming) {
        validate(incoming, true);
        if (incoming.getDisplayOrder() == null) {
            List<Partner> all = getAll();
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
    public Partner update(Long id, Partner incoming) {
        Partner existing = getById(id);
        if (incoming.getName() != null) existing.setName(incoming.getName());
        if (incoming.getLogoUrl() != null) existing.setLogoUrl(incoming.getLogoUrl());
        if (incoming.getWebsiteUrl() != null) existing.setWebsiteUrl(incoming.getWebsiteUrl());
        if (incoming.getCategory() != null) existing.setCategory(incoming.getCategory());
        if (incoming.getIsActive() != null) existing.setIsActive(incoming.getIsActive());
        if (incoming.getDisplayOrder() != null) existing.setDisplayOrder(incoming.getDisplayOrder());
        validate(existing, false);
        return repo.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Partner entity = getById(id);
        repo.delete(entity);
        normalizeOrder();
    }

    @Transactional
    public List<Partner> reorder(List<Long> orderedIds) {
        if (orderedIds == null || orderedIds.isEmpty()) {
            throw new IllegalArgumentException("orderedIds must not be empty");
        }
        List<Partner> all = getAll();
        Map<Long, Partner> byId = new HashMap<>();
        for (Partner e : all) {
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
            Partner e = byId.get(orderedIds.get(i));
            e.setDisplayOrder(10_000 + i);
            repo.save(e);
        }
        repo.flush();
        for (int i = 0; i < orderedIds.size(); i++) {
            Partner e = byId.get(orderedIds.get(i));
            e.setDisplayOrder(i + 1);
            repo.save(e);
        }
        return getAll();
    }

    private void normalizeOrder() {
        List<Partner> all = getAll();
        for (int i = 0; i < all.size(); i++) {
            Partner e = all.get(i);
            int expected = i + 1;
            if (e.getDisplayOrder() == null || e.getDisplayOrder() != expected) {
                e.setDisplayOrder(expected);
                repo.save(e);
            }
        }
    }

    private void validate(Partner e, boolean creating) {
        if (e.getName() == null || e.getName().isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
    }
}