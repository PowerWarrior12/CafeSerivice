package com.simbirsoft.common;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;

public class ImageGenerator {
    public static MockMultipartFile generateImageMockMultipartFile() throws Exception {
        File image = ResourceUtils.getFile("classpath:empty.png");
        String[] imageNameArray = image.getName().split("\\.");
        String imageName = imageNameArray[0];
        String imageExtension = imageNameArray[1];
        return new MockMultipartFile(
                "image",
                String.format("%s.%s", imageName, imageExtension),
                MediaType.IMAGE_PNG_VALUE,
                new FileInputStream(image));
    }
}
