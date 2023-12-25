package com.simbirsoft.services.impl;

import com.simbirsoft.exceptions.ApiRequestException;
import com.simbirsoft.repositories.ImageRepository;
import com.simbirsoft.services.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.simbirsoft.constants.ErrorMessages.FAILED_LOADING_IMAGE;
import static com.simbirsoft.constants.ErrorMessages.FAILED_SAVING_IMAGE;

@Service
@RequiredArgsConstructor
@Log4j2
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    @Override
    public String saveImage(MultipartFile image) {
        log.atDebug().log(String.format("Save image process: start saving image with name %s", image.getName()));
        try {
            return imageRepository.saveImage(image);
        } catch (IOException ioException) {
            log.atError().log(String.format("Save image process: failed saving the image with name %s", image.getName()));
            throw new ApiRequestException(FAILED_SAVING_IMAGE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public byte[] loadImage(String imageName) {
        log.atDebug().log(String.format("Load image process: start loading image with name %s", imageName));
        try {
            return imageRepository.loadImageByName(imageName);
        } catch (IOException ioException) {
            log.atError().log(String.format("Load image process: failed loading the image with name %s", imageName));
            throw new ApiRequestException(FAILED_LOADING_IMAGE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
