package ru.vtkachenko.assistant.filedownload;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Path;

@Service
public class FileDownloadService {

    public ResponseEntity<Resource> getFileAsResource(File file) {
        return getFileAsResource(file.toURI());
    }

    public ResponseEntity<Resource> getFileAsResource(Path filePath) {
        return getFileAsResource(filePath.toUri());
    }

    public ResponseEntity<Resource> getFileAsResource(URI uri) {
        String headerValue = null;
        String contentType = null;
        Resource resource = null;

        try {
            resource = new UrlResource(uri);
            String fileName = resource.getFilename();
            headerValue = "attachment; filename=" + fileName;
            contentType = "application/octet-stream";
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Expose-Headers", "filename");
        responseHeaders.set("filename", resource.getFilename());
        responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .headers(responseHeaders)
                .body(resource);
    }
}
