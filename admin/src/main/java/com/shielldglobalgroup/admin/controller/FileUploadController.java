package com.shielldglobalgroup.admin.controller;

import com.shielldglobalgroup.admin.dto.ApiResponseDTO;
import com.shielldglobalgroup.admin.service.ContentService;
import com.shielldglobalgroup.admin.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin/upload")
@RequiredArgsConstructor
// @CrossOrigin(origins = "*")
public class FileUploadController {

    private final FileUploadService fileUploadService;
    private final ContentService contentService;

    // POST /api/admin/upload/image
    // form-data: file, pageKey, sectionKey, field
    // example: uploading new founder photo
    //   file = founder_new.jpg
    //   pageKey = about
    //   sectionKey = founders
    //   field = image_path
    @PostMapping("/image")
    public ResponseEntity<ApiResponseDTO<String>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("pageKey") String pageKey,
            @RequestParam("sectionKey") String sectionKey,
            @RequestParam("field") String field) {
        try {
            // save file to disk → get path
            String path = fileUploadService.uploadFile(file, "images");
            // save path to content_blocks table
            contentService.saveField(pageKey, sectionKey, field, path);

            return ResponseEntity.ok(
                ApiResponseDTO.ok("Image uploaded successfully", path)
            );
        } catch (IOException e) {
            return ResponseEntity.status(500).body(
                ApiResponseDTO.error("Upload failed: " + e.getMessage())
            );
        }
    }

    // POST /api/admin/upload/video
    // form-data: file, pageKey, sectionKey, field
    // example: uploading new hero video
    //   file = HV2_new.mp4
    //   pageKey = home
    //   sectionKey = hero_slide_2
    //   field = video_path
    @PostMapping("/video")
    public ResponseEntity<ApiResponseDTO<String>> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("pageKey") String pageKey,
            @RequestParam("sectionKey") String sectionKey,
            @RequestParam("field") String field) {
        try {
            String path = fileUploadService.uploadFile(file, "videos");
            contentService.saveField(pageKey, sectionKey, field, path);

            return ResponseEntity.ok(
                ApiResponseDTO.ok("Video uploaded successfully", path)
            );
        } catch (IOException e) {
            return ResponseEntity.status(500).body(
                ApiResponseDTO.error("Upload failed: " + e.getMessage())
            );
        }
    }
}