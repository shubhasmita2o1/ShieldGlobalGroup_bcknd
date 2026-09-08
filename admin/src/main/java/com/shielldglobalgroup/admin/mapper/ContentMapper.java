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
}
