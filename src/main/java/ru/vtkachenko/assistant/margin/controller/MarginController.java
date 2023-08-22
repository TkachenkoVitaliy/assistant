package ru.vtkachenko.assistant.margin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.vtkachenko.assistant.filedownload.FileDownloadService;
import ru.vtkachenko.assistant.margin.service.MarginService;

import java.io.File;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/margin")
public class MarginController {

    private final MarginService marginService;
    private final FileDownloadService fileDownloadService;

    @PostMapping
    @CrossOrigin("*")
    public ResponseEntity<Resource> createMarginReport(
            @RequestParam("transitMonthly") MultipartFile transitMonthly,
            @RequestParam("stockMonthly") MultipartFile stockMonthly,
            @RequestParam("summaryMonthly") MultipartFile summaryMonthly,
            @RequestParam("transitAnnually") MultipartFile transitAnnually,
            @RequestParam("stockAnnually") MultipartFile stockAnnually,
            @RequestParam("summaryAnnually") MultipartFile summaryAnnually
    ) {
        File marginReport = marginService.createMarginReport(transitMonthly, stockMonthly, summaryMonthly,
                transitAnnually, stockAnnually, summaryAnnually);
        return fileDownloadService.getFileAsResource(marginReport);
    }
}
