package com.simbirsoft.repositories.impl;

import com.simbirsoft.common.KeyGenerator;
import com.simbirsoft.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ImageRepositoryImpl implements ImageRepository {
    @Value("${images.directory.path}")
    private String imageDirectoryPath;
    private final KeyGenerator keyGenerator;
    @Override
    public String saveImage(MultipartFile image) throws IOException {
        String imageExpansion = Objects.requireNonNull(image.getOriginalFilename()).split("\\.")[1];
        String randomFileName = keyGenerator.generateKey() + "." + imageExpansion;
        byte[] bytes = image.getBytes();
        try (OutputStream os = new FileOutputStream(String.format("%s//%s", imageDirectoryPath, randomFileName))) {
            os.write(bytes);
        }
        return randomFileName;
    }

    @Override
    public byte[] loadImageByName(String imageName) throws IOException {
        String imagePath = String.format("%s//%s", imageDirectoryPath, imageName);
        File imageFile = new File(imagePath);
        byte[] bytes;
        try (InputStream is = new FileInputStream(imageFile)) {
            bytes = is.readAllBytes();
        }
        return bytes;
    }
}
