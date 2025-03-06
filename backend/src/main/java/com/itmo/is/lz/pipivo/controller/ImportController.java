package com.itmo.is.lz.pipivo.controller;

import com.itmo.is.lz.pipivo.service.ImportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin")
public class ImportController {

    private final ImportService importService;

    public ImportController(ImportService importService) {
        this.importService = importService;
    }

    @PostMapping("/import")
    public ResponseEntity<String> importBeers(
            @RequestParam("file") MultipartFile excelFile,
            @RequestParam("photos") MultipartFile zipFile) throws Exception {

        importService.importBeerData(excelFile, zipFile);
        return ResponseEntity.ok("success");
    }
}
