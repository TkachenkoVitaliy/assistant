package ru.vtkachenko.assistant.margin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/margin")
public class MarginController {

    @PostMapping
    public ResponseEntity<Resource> createMarginReport(
            @RequestParam("transitMonthly") MultipartFile transitMonthly,
            @RequestParam("stockMonthly") MultipartFile stockMonthly,
            @RequestParam("summaryMonthly") MultipartFile summaryMonthly,
            @RequestParam("transitAnnually") MultipartFile transitAnnually,
            @RequestParam("stockAnnually") MultipartFile stockAnnually,
            @RequestParam("summaryAnnually") MultipartFile summaryAnnually
    ) {
        return null;
    }
}