package com.simbirsoft.repositories;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageRepository {
    public String saveImage(MultipartFile image) throws IOException;
    public byte[] loadImageByName(String imageName) throws IOException;
}
