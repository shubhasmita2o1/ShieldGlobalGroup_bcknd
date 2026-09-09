package com.shielldglobalgroup.admin.mapper;

import com.shielldglobalgroup.admin.dto.*;
import com.shielldglobalgroup.admin.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContentMapper {

    public ContentBlockDTO toDTO(ContentBlock entity) {
        return new ContentBlockDTO(
            entity.getPageKey(),
            entity.getSectionKey(),
            entity.getField(),
            entity.getValue()
        );
    }

    public ContentBlock toEntity(ContentBlockDTO dto) {
        ContentBlock entity = new ContentBlock();
        entity.setPageKey(dto.getPageKey());
        entity.setSectionKey(dto.getSectionKey());
        entity.setField(dto.getField());
        entity.setValue(dto.getValue());
        return entity;
    }

    public ContentListItemDTO toDTO(ContentListItem entity) {
        return new ContentListItemDTO(
            entity.getId(),
            entity.getPageKey(),
            entity.getSectionKey(),
            entity.getListType(),
            entity.getOrderIndex(),
            entity.getItemText()
        );
    }

    public HeroSlideDTO toDTO(HeroSlide entity) {
        return new HeroSlideDTO(
            entity.getId(),
            entity.getSlideOrder(),
            entity.getTitle(),
            entity.getSubtitle(),
            entity.getVideoPath(),
            entity.getTabLabel(),
            entity.getSlideType()
        );
    }

    public HeroSlide toEntity(HeroSlideDTO dto) {
        HeroSlide entity = new HeroSlide();
        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        entity.setSlideOrder(dto.getSlideOrder());
        entity.setTitle(dto.getTitle());
        entity.setSubtitle(dto.getSubtitle());
        entity.setVideoPath(dto.getVideoPath());
        entity.setTabLabel(dto.getTabLabel());
        entity.setSlideType(dto.getSlideType());
        return entity;
    }

    public MapPinDTO toDTO(MapPin entity) {
        return new MapPinDTO(
            entity.getId(),
            entity.getLabel(),
            entity.getLeftPercent(),
            entity.getTopPercent()
        );
    }

    public MapPin toEntity(MapPinDTO dto) {
        MapPin entity = new MapPin();
        entity.setLabel(dto.getLabel());
        entity.setLeftPercent(dto.getLeftPercent());
        entity.setTopPercent(dto.getTopPercent());
        return entity;
    }

    public ContactMessageDTO toDTO(ContactMessage entity) {
        return new ContactMessageDTO(
            entity.getId(),
            entity.getName(),
            entity.getEmail(),
            entity.getPhone(),
            entity.getMessage(),
            entity.getSubmittedAt(),
            entity.getIsRead()
        );
    }

    public List<HeroSlideDTO> toHeroDTOList(List<HeroSlide> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<MapPinDTO> toMapPinDTOList(List<MapPin> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ContactMessageDTO> toMessageDTOList(List<ContactMessage> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ContentListItemDTO> toContentListItemDTOList(List<ContentListItem> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public MapLocationDTO toDTO(MapLocation entity) {
        return new MapLocationDTO(
            entity.getId(),
            entity.getName(),
            entity.getCountry(),
            entity.getLatitude(),
            entity.getLongitude(),
            entity.getRegion(),
            entity.getKind(),
            entity.getIsActive(),
            entity.getDisplayOrder(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public MapLocation toEntity(MapLocationDTO dto) {
        MapLocation entity = new MapLocation();
        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        entity.setName(dto.getName());
        entity.setCountry(dto.getCountry());
        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());
        entity.setRegion(dto.getRegion());
        entity.setKind(dto.getKind());
        entity.setIsActive(dto.getIsActive());
        entity.setDisplayOrder(dto.getDisplayOrder());
        return entity;
    }

    public List<MapLocationDTO> toMapLocationDTOList(List<MapLocation> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // ── Phase 1.5 modules ─────────────────────────────────────────────────

    public JourneyMilestoneDTO toDTO(JourneyMilestone entity) {
        return new JourneyMilestoneDTO(
            entity.getId(), entity.getYear(), entity.getIndexLabel(), entity.getName(),
            entity.getSubtitle(), entity.getDescription(), entity.getTag(), entity.getFlag(),
            entity.getIsActive(), entity.getDisplayOrder(), entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }

    public JourneyMilestone toEntity(JourneyMilestoneDTO dto) {
        JourneyMilestone e = new JourneyMilestone();
        if (dto.getId() != null) e.setId(dto.getId());
        e.setYear(dto.getYear());
        e.setIndexLabel(dto.getIndexLabel());
        e.setName(dto.getName());
        e.setSubtitle(dto.getSubtitle());
        e.setDescription(dto.getDescription());
        e.setTag(dto.getTag());
        e.setFlag(dto.getFlag());
        e.setIsActive(dto.getIsActive());
        e.setDisplayOrder(dto.getDisplayOrder());
        return e;
    }

    public List<JourneyMilestoneDTO> toJourneyMilestoneDTOList(List<JourneyMilestone> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public AchievementDTO toDTO(Achievement entity) {
        return new AchievementDTO(
            entity.getId(), entity.getLabel(), entity.getValue(), entity.getSuffix(),
            entity.getDetail(), entity.getIconKey(), entity.getIsActive(),
            entity.getDisplayOrder(), entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }

    public Achievement toEntity(AchievementDTO dto) {
        Achievement e = new Achievement();
        if (dto.getId() != null) e.setId(dto.getId());
        e.setLabel(dto.getLabel());
        e.setValue(dto.getValue());
        e.setSuffix(dto.getSuffix());
        e.setDetail(dto.getDetail());
        e.setIconKey(dto.getIconKey());
        e.setIsActive(dto.getIsActive());
        e.setDisplayOrder(dto.getDisplayOrder());
        return e;
    }

    public List<AchievementDTO> toAchievementDTOList(List<Achievement> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public TestimonialDTO toDTO(Testimonial entity) {
        return new TestimonialDTO(
            entity.getId(), entity.getName(), entity.getDesignation(), entity.getCompany(),
            entity.getMessage(), entity.getPhotoUrl(), entity.getRating(), entity.getIsActive(),
            entity.getDisplayOrder(), entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }

    public Testimonial toEntity(TestimonialDTO dto) {
        Testimonial e = new Testimonial();
        if (dto.getId() != null) e.setId(dto.getId());
        e.setName(dto.getName());
        e.setDesignation(dto.getDesignation());
        e.setCompany(dto.getCompany());
        e.setMessage(dto.getMessage());
        e.setPhotoUrl(dto.getPhotoUrl());
        e.setRating(dto.getRating());
        e.setIsActive(dto.getIsActive());
        e.setDisplayOrder(dto.getDisplayOrder());
        return e;
    }

    public List<TestimonialDTO> toTestimonialDTOList(List<Testimonial> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public PartnerDTO toDTO(Partner entity) {
        return new PartnerDTO(
            entity.getId(), entity.getName(), entity.getLogoUrl(), entity.getWebsiteUrl(),
            entity.getCategory(), entity.getIsActive(), entity.getDisplayOrder(),
            entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }

    public Partner toEntity(PartnerDTO dto) {
        Partner e = new Partner();
        if (dto.getId() != null) e.setId(dto.getId());
        e.setName(dto.getName());
        e.setLogoUrl(dto.getLogoUrl());
        e.setWebsiteUrl(dto.getWebsiteUrl());
        e.setCategory(dto.getCategory());
        e.setIsActive(dto.getIsActive());
        e.setDisplayOrder(dto.getDisplayOrder());
        return e;
    }

    public List<PartnerDTO> toPartnerDTOList(List<Partner> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public CompanyDTO toDTO(Company entity) {
        return new CompanyDTO(
            entity.getId(), entity.getName(), entity.getSlug(), entity.getLogoUrl(),
            entity.getShortDescription(), entity.getDescription(), entity.getWebsiteUrl(),
            entity.getLocation(), entity.getEmail(), entity.getPhone(), entity.getImageUrl(),
            entity.getIsActive(), entity.getDisplayOrder(), entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }

    public Company toEntity(CompanyDTO dto) {
        Company e = new Company();
        if (dto.getId() != null) e.setId(dto.getId());
        e.setName(dto.getName());
        e.setSlug(dto.getSlug());
        e.setLogoUrl(dto.getLogoUrl());
        e.setShortDescription(dto.getShortDescription());
        e.setDescription(dto.getDescription());
        e.setWebsiteUrl(dto.getWebsiteUrl());
        e.setLocation(dto.getLocation());
        e.setEmail(dto.getEmail());
        e.setPhone(dto.getPhone());
        e.setImageUrl(dto.getImageUrl());
        e.setIsActive(dto.getIsActive());
        e.setDisplayOrder(dto.getDisplayOrder());
        return e;
    }

    public List<CompanyDTO> toCompanyDTOList(List<Company> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
