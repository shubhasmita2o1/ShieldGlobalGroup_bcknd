package com.shielldglobalgroup.admin.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileUploadService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    // Save file and return its accessible path
    public String uploadFile(MultipartFile file, String subfolder) throws IOException {

        // create folder if it doesn't exist
        // e.g. uploads/images/ or uploads/videos/
        Path folder = Paths.get(uploadDir, subfolder);
        Files.createDirectories(folder);

        // generate unique filename to avoid overwriting
        // e.g. "founder.jpg" → "a3f9b2c1-founder.jpg"
        String uniqueName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        Path targetPath = folder.resolve(uniqueName);

        // save file to disk
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        // return the URL path that frontend will use
        // e.g. "/uploads/images/a3f9b2c1-founder.jpg"
        return "/uploads/" + subfolder + "/" + uniqueName;
    }
}
