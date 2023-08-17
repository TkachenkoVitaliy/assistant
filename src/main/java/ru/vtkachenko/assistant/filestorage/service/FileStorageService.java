package ru.vtkachenko.assistant.filestorage.service;

import org.springframework.web.multipart.MultipartFile;
import ru.vtkachenko.assistant.filestorage.config.FileStorageProperties;
import ru.vtkachenko.assistant.filestorage.exception.FileStorageException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileStorageService {
    private final Path TEMP_DIRECTORY;

    public FileStorageService(FileStorageProperties fileStorageProperties) {
        Path FILE_STORAGE_LOCATION = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        this.TEMP_DIRECTORY = FILE_STORAGE_LOCATION.resolve("temp");
    }


    private Path saveFileToFileSystem(MultipartFile file, String fileNameForSave) {
        Path savedFilePath = TEMP_DIRECTORY.resolve(fileNameForSave);
        try {
            Files.copy(file.getInputStream(), savedFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException(String.format("Не удалось сохранить файл - %s", savedFilePath), e);
        }
        return savedFilePath;
    }
}
