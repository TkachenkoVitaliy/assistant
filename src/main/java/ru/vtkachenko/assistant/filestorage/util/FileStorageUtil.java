package ru.vtkachenko.assistant.filestorage.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;
import ru.vtkachenko.assistant.filestorage.exception.FileStorageException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@UtilityClass
public class FileStorageUtil {
    public static Path saveFileToFileSystem(MultipartFile file, Path fileSavePath) {
        try {
            Files.copy(file.getInputStream(), fileSavePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException(String.format("Не удалось сохранить файл - %s", fileSavePath), e);
        }
        return fileSavePath;
    }
}
