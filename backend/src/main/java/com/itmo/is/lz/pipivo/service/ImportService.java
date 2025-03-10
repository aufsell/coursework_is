package com.itmo.is.lz.pipivo.service;

import com.itmo.is.lz.pipivo.model.Beer;
import com.itmo.is.lz.pipivo.repository.BeerRepository;
import com.itmo.is.lz.pipivo.repository.FermentationTypeRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class ImportService {
    private final MinioService minioService;
    private final BeerRepository beerRepository;
    private final String imageFolderPath;
    private final String avatarFolderPath;
    private final FermentationTypeRepository fermentationTypeRepository;

    public ImportService(MinioService minioService, BeerRepository beerRepository,
                         @Value("${beer.image.folder}") String imageFolderPath,@Value("${avatar.image.folder}") String avatarFolderPath, FermentationTypeRepository fermentationTypeRepository) {
        this.minioService = minioService;
        this.beerRepository = beerRepository;
        this.imageFolderPath = imageFolderPath;
        this.avatarFolderPath = avatarFolderPath;
        this.fermentationTypeRepository = fermentationTypeRepository;
    }

    public void importBeerData(MultipartFile xlsxFile, MultipartFile zipFile) {
        Path tempDir = null;
        try {
            tempDir = Files.createTempDirectory("beer_images");
            System.out.println("Временная папка создана: " + tempDir);
            unzip(zipFile, tempDir);
            processXLSX(xlsxFile, tempDir);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обработке импорта", e);
        } finally {

            cleanUp(tempDir);
        }
    }

    public String importAvatar(MultipartFile avatar, Long userId) {
        Path tempDir = null;
        try {
            tempDir = Files.createTempDirectory("avatar_images");
            System.out.println("Временная папка создана: " + tempDir);
            Path avatarPath = tempDir.resolve(userId + ".png");
            Files.copy(avatar.getInputStream(), avatarPath, StandardCopyOption.REPLACE_EXISTING);
            return uploadAvatarToMinio(avatarPath);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обработке аватара", e);
        } finally {
            cleanUp(tempDir);
        }
    }

    private void unzip(MultipartFile zipFile, Path tempDir) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(zipFile.getInputStream())) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    Path filePath = tempDir.resolve(entry.getName());
                    Files.copy(zis, filePath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Извлечён файл: " + filePath);
                }
            }
        }
    }
    @Transactional
    public void processXLSX (MultipartFile xlsxFile, Path imageFolder) {
        try (Workbook workbook = WorkbookFactory.create(xlsxFile.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            List<Beer> beerList = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                String name = row.getCell(0).getStringCellValue();
                Double price = row.getCell(1).getNumericCellValue();
                Double volume = row.getCell(2).getNumericCellValue();
                Long fermentationType = (long) row.getCell(3).getNumericCellValue();
                Long srm = (long) row.getCell(4).getNumericCellValue();
                Long ibu = (long) row.getCell(5).getNumericCellValue();
                Long abv = (long) row.getCell(6).getNumericCellValue();
                Long og = (long) row.getCell(7).getNumericCellValue();
                String country = row.getCell(8).getStringCellValue();
                String imageName = row.getCell(9).getStringCellValue();
                Path imagePath = imageFolder.resolve(imageName);
                if (!Files.exists(imagePath)) {
                    System.out.println("Файл " + imageName + " не найден!");
                    continue;
                }

                String minioUrl = uploadImageToMinio(imagePath);

                Beer beer = new Beer();
                beer.setName(name);
                beer.setPrice(price);
                beer.setVolume(volume);
                beer.setFermentationType(fermentationTypeRepository.findById(fermentationType).orElseThrow());
                beer.setSrm(srm);
                beer.setIbu(ibu);
                beer.setAbv(abv);
                beer.setOg(og);
                beer.setCountry(country);
                beer.setImagePath(minioUrl);
                beer.setLastUpdated(Timestamp.valueOf(LocalDateTime.now()));
                System.out.println("rshngou;rasnhopawenrhnawrhno[" + beer.getLastUpdated());
                beerList.add(beer);

            }

            beerRepository.saveAll(beerList);

            System.out.println("Импорт завершён. Загружено " + beerList.size() + " записей.");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при импорте пива", e);
        }
    }

    private String uploadImageToMinio(Path imagePath) {
        try {
            if (!Files.exists(imagePath)) {
                throw new RuntimeException("Файл " + imagePath + " не найден!");
            }

            try (FileInputStream fis = new FileInputStream(imagePath.toFile())) {
                String objectName = "beer_images/" + imagePath.getFileName();
                minioService.uploadFile(objectName, fis, Files.size(imagePath), "image/png");
                return minioService.generateUrl(objectName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки изображения " + imagePath + " в MinIO", e);
        }
    }

    private String uploadAvatarToMinio(Path imagePath) {
        try {
            if (!Files.exists(imagePath)) {
                throw new RuntimeException("Файл " + imagePath + " не найден!");
            }

            try (FileInputStream fis = new FileInputStream(imagePath.toFile())) {
                String objectName = "avatar_images/" + imagePath.getFileName();
                minioService.uploadFile(objectName, fis, Files.size(imagePath), "image/png");
                return minioService.generateUrl(objectName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки изображения " + imagePath + " в MinIO", e);
        }
    }


    private void cleanUp(Path tempDir) {
        if (tempDir != null && Files.exists(tempDir)) {
            try {
                FileUtils.deleteDirectory(tempDir.toFile());
                System.out.println("Временная папка удалена: " + tempDir);
            } catch (IOException e) {
                System.err.println("Ошибка удаления временной папки: " + e.getMessage());
            }
        }
    }
}
