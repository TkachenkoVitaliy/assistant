package ru.vtkachenko.assistant.filestorage.service;

import org.springframework.stereotype.Service;
import ru.vtkachenko.assistant.filestorage.config.FileStorageProperties;
import ru.vtkachenko.assistant.filestorage.dto.FileDto;
import ru.vtkachenko.assistant.filestorage.exception.FileStorageException;
import ru.vtkachenko.assistant.filestorage.util.FileStorageUtil;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileStorageService {
    private final Path TEMP_DIRECTORY;

    public FileStorageService(FileStorageProperties fileStorageProperties) {
        Path FILE_STORAGE_LOCATION = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        this.TEMP_DIRECTORY = FILE_STORAGE_LOCATION.resolve("temp");
    }

    public List<Path> storeFiles(String folder, List<FileDto> fileDtos) {
        return fileDtos.stream()
                .map(fileDto -> storeFile(folder, fileDto))
                .collect(Collectors.toList());
    }

    public Path storeFile(String folder, FileDto fileDto) {
        Path savedFilePath = TEMP_DIRECTORY.resolve(folder).resolve(fileDto.nameForSave());
        return FileStorageUtil.saveFileToFileSystem(fileDto.file(), savedFilePath);
    }

    public void removeTempSubfolder(String subfolder) {
        Path removeFolderPath = TEMP_DIRECTORY.resolve(subfolder);
        removeFolder(removeFolderPath);
    }

    private void removeFolder(Path folder) {
        if (!FileStorageUtil.isDirectory(folder)) {
            throw new FileStorageException(String.format("Этот путь - %s не является директорией", folder));
        }
        boolean result = FileStorageUtil.removeRecursively(folder);
        if (!result) {
            throw new FileStorageException(String.format("Директория не найдена - %s", folder));
        }
    }
}
