package com.shielldglobalgroup.admin.service;

import com.shielldglobalgroup.admin.entity.Company;
import com.shielldglobalgroup.admin.exception.ResourceNotFoundException;
import com.shielldglobalgroup.admin.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository repo;

    public List<Company> getAll() {
        return repo.findAllByOrderByDisplayOrderAsc();
    }

    public List<Company> getActive() {
        return repo.findByIsActiveTrueOrderByDisplayOrderAsc();
    }

    public Company getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company", id));
    }

    public Company getActiveBySlug(String slug) {
        return repo.findBySlugAndIsActiveTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found: " + slug));
    }

    @Transactional
    public Company create(Company incoming) {
        if (incoming.getSlug() == null || incoming.getSlug().isBlank()) {
            incoming.setSlug(slugify(incoming.getName()));
        } else {
            incoming.setSlug(slugify(incoming.getSlug()));
        }
        validate(incoming, null);
        if (incoming.getDisplayOrder() == null) {
            List<Company> all = getAll();
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
    public Company update(Long id, Company incoming) {
        Company existing = getById(id);
        if (incoming.getName() != null) existing.setName(incoming.getName());
        if (incoming.getSlug() != null) existing.setSlug(slugify(incoming.getSlug()));
        if (incoming.getLogoUrl() != null) existing.setLogoUrl(incoming.getLogoUrl());
        if (incoming.getShortDescription() != null) existing.setShortDescription(incoming.getShortDescription());
        if (incoming.getDescription() != null) existing.setDescription(incoming.getDescription());
        if (incoming.getWebsiteUrl() != null) existing.setWebsiteUrl(incoming.getWebsiteUrl());
        if (incoming.getLocation() != null) existing.setLocation(incoming.getLocation());
        if (incoming.getEmail() != null) existing.setEmail(incoming.getEmail());
        if (incoming.getPhone() != null) existing.setPhone(incoming.getPhone());
        if (incoming.getImageUrl() != null) existing.setImageUrl(incoming.getImageUrl());
        if (incoming.getIsActive() != null) existing.setIsActive(incoming.getIsActive());
        if (incoming.getDisplayOrder() != null) existing.setDisplayOrder(incoming.getDisplayOrder());
        validate(existing, id);
        return repo.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Company entity = getById(id);
        repo.delete(entity);
        normalizeOrder();
    }

    @Transactional
    public List<Company> reorder(List<Long> orderedIds) {
        if (orderedIds == null || orderedIds.isEmpty()) {
            throw new IllegalArgumentException("orderedIds must not be empty");
        }
        List<Company> all = getAll();
        Map<Long, Company> byId = new HashMap<>();
        for (Company e : all) {
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
            Company e = byId.get(orderedIds.get(i));
            e.setDisplayOrder(10_000 + i);
            repo.save(e);
        }
        repo.flush();
        for (int i = 0; i < orderedIds.size(); i++) {
            Company e = byId.get(orderedIds.get(i));
            e.setDisplayOrder(i + 1);
            repo.save(e);
        }
        return getAll();
    }

    private void normalizeOrder() {
        List<Company> all = getAll();
        for (int i = 0; i < all.size(); i++) {
            Company e = all.get(i);
            int expected = i + 1;
            if (e.getDisplayOrder() == null || e.getDisplayOrder() != expected) {
                e.setDisplayOrder(expected);
                repo.save(e);
            }
        }
    }

    private void validate(Company e, Long excludeId) {
        if (e.getName() == null || e.getName().isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (e.getSlug() == null || e.getSlug().isBlank()) {
            throw new IllegalArgumentException("slug is required");
        }
        boolean taken = excludeId == null
                ? repo.existsBySlug(e.getSlug())
                : repo.existsBySlugAndIdNot(e.getSlug(), excludeId);
        if (taken) {
            throw new IllegalArgumentException("slug already exists: " + e.getSlug());
        }
    }

    private static String slugify(String input) {
        if (input == null) return "";
        return input.trim().toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }
}
