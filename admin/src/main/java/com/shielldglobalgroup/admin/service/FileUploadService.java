package com.shielldglobalgroup.admin.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class FileUploadService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "jpg", "jpeg", "png", "webp", "gif", "mp4"
    );

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp", "image/gif", "video/mp4"
    );

    @Value("${file.upload-dir}")
    private String uploadDir;

    public Map<String, Object> upload(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }

        String originalName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file";
        String sanitizedOriginal = sanitizeFilename(originalName);
        String ext = extensionOf(sanitizedOriginal);

        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new IllegalArgumentException(
                    "File type not allowed. Allowed: jpg, jpeg, png, webp, gif, mp4");
        }

        String contentType = file.getContentType();
        if (contentType != null && !contentType.isBlank()
                && !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase(Locale.ROOT))
                && !contentType.equalsIgnoreCase("application/octet-stream")) {
            throw new IllegalArgumentException("Content type not allowed: " + contentType);
        }

        String subfolder = "mp4".equals(ext) ? "videos" : "images";
        Path folder = Paths.get(uploadDir, subfolder);
        Files.createDirectories(folder);

        String storedName = UUID.randomUUID() + "-" + sanitizedOriginal;
        Path target = folder.resolve(storedName);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        String url = "/uploads/" + subfolder + "/" + storedName;

        return Map.of(
                "fileName", storedName,
                "originalName", sanitizedOriginal,
                "url", url,
                "size", file.getSize(),
                "contentType", contentType != null ? contentType : guessContentType(ext)
        );
    }

    /** Legacy helper used by older image/video endpoints. */
    public String uploadFile(MultipartFile file, String subfolder) throws IOException {
        Map<String, Object> result = upload(file);
        return (String) result.get("url");
    }

    private static String sanitizeFilename(String name) {
        String base = Paths.get(name).getFileName().toString();
        base = base.replaceAll("[^a-zA-Z0-9._-]", "_");
        if (base.isBlank() || base.equals(".") || base.equals("..")) {
            base = "file";
        }
        if (base.length() > 120) {
            String ext = extensionOf(base);
            String stem = base.substring(0, Math.min(100, base.length()));
            base = stem + (ext.isEmpty() ? "" : "." + ext);
        }
        return base;
    }

    private static String extensionOf(String name) {
        int dot = name.lastIndexOf('.');
        if (dot < 0 || dot == name.length() - 1) {
            return "";
        }
        return name.substring(dot + 1).toLowerCase(Locale.ROOT);
    }

    private static String guessContentType(String ext) {
        return switch (ext) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "webp" -> "image/webp";
            case "gif" -> "image/gif";
            case "mp4" -> "video/mp4";
            default -> "application/octet-stream";
        };
    }
}