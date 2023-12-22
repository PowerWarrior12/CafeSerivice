package com.simbirsoft.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    public String saveImage(MultipartFile image);
    public byte[] loadImage(String imageName);
}
