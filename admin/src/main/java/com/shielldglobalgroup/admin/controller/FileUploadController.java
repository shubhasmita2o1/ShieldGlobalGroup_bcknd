package com.shielldglobalgroup.admin.controller;

import com.shielldglobalgroup.admin.dto.ApiResponseDTO;
import com.shielldglobalgroup.admin.service.ContentService;
import com.shielldglobalgroup.admin.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/upload")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;
    private final ContentService contentService;

    /**
     * POST /api/admin/upload
     * multipart: file
     * returns fileName, originalName, url, size, contentType
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> upload(
            @RequestParam("file") MultipartFile file) throws IOException {
        Map<String, Object> result = fileUploadService.upload(file);
        return ResponseEntity.ok(ApiResponseDTO.ok("File uploaded", result));
    }

    /** Legacy: upload image and save path into a content field. */
    @PostMapping("/image")
    public ResponseEntity<ApiResponseDTO<String>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("pageKey") String pageKey,
            @RequestParam("sectionKey") String sectionKey,
            @RequestParam("field") String field) throws IOException {
        String path = fileUploadService.uploadFile(file, "images");
        contentService.saveField(pageKey, sectionKey, field, path);
        return ResponseEntity.ok(ApiResponseDTO.ok("Image uploaded successfully", path));
    }

    /** Legacy: upload video and save path into a content field. */
    @PostMapping("/video")
    public ResponseEntity<ApiResponseDTO<String>> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("pageKey") String pageKey,
            @RequestParam("sectionKey") String sectionKey,
            @RequestParam("field") String field) throws IOException {
        String path = fileUploadService.uploadFile(file, "videos");
        contentService.saveField(pageKey, sectionKey, field, path);
        return ResponseEntity.ok(ApiResponseDTO.ok("Video uploaded successfully", path));
    }
}
