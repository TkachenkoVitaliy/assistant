package ru.vtkachenko.assistant.filestorage.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.vtkachenko.assistant.filestorage.config.FileStorageProperties;
import ru.vtkachenko.assistant.filestorage.exception.FileStorageException;
import ru.vtkachenko.assistant.filestorage.util.FileStorageUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {
    private final Path TEMP_DIRECTORY;

    public Path getTempDirectory() {
        return this.TEMP_DIRECTORY;
    }

    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.TEMP_DIRECTORY = Paths.get(fileStorageProperties.getTempDir()).toAbsolutePath().normalize();
    }

    public Path storeFile(String folder, MultipartFile file, String fileName) {
        Path directory = createDirectoryIfNotExists(TEMP_DIRECTORY.resolve(folder).toAbsolutePath().normalize());
        Path savedFilePath = directory.resolve(fileName).toAbsolutePath().normalize();
        return FileStorageUtil.saveFileToFileSystem(file, savedFilePath);
    }

    public void removeTempSubfolder(String subfolder) {
        Path removeFolderPath = TEMP_DIRECTORY.resolve(subfolder);
        removeFolder(removeFolderPath);
    }

    private void removeFolder(Path folder) {
        if (!folder.toFile().exists()) return;

        if (!FileStorageUtil.isDirectory(folder)) {
            throw new FileStorageException(String.format("Этот путь - %s не является директорией", folder));
        }
        boolean result = FileStorageUtil.removeRecursively(folder);
        if (!result) {
            throw new FileStorageException(String.format("Директория не найдена - %s", folder));
        }
    }

    private Path createDirectoryIfNotExists(Path directory) {
        Path normalizedDirectory = directory.toAbsolutePath().normalize();
        if (normalizedDirectory.toFile().exists()) {
            return normalizedDirectory;
        }

        try {
            return Files.createDirectories(normalizedDirectory);
        } catch (IOException e) {
            throw new FileStorageException(
                    String.format("Не удалось создать директорию - %s", normalizedDirectory),
                    e
            );
        }
    }
}
