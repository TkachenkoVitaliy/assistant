package ru.vtkachenko.assistant.filestorage.dto;

import org.springframework.web.multipart.MultipartFile;

public record FileDto(MultipartFile file, String nameForSave) {
}
