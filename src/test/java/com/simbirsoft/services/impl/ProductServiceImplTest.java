package com.simbirsoft.services.impl;

import com.simbirsoft.ServiceTest;
import com.simbirsoft.common.ProductGenerator;
import com.simbirsoft.domain.Product;
import com.simbirsoft.dto.product.CreateProductRequest;
import com.simbirsoft.dto.product.ProductResponse;
import com.simbirsoft.repositories.ProductRepository;
import com.simbirsoft.services.ImageService;
import com.simbirsoft.services.mappers.product.ProductMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

import static com.simbirsoft.common.TestConstants.PRODUCT_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ServiceTest
class ProductServiceImplTest {
    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private ProductMapper productMapper;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private ImageService imageService;

    @Test
    @DisplayName("Get product by id process in successful case")
    void getProductById() {
        int productId = PRODUCT_ID;
        Product product = ProductGenerator.generateProduct(PRODUCT_ID);
        when(productRepository.findProductById(PRODUCT_ID)).thenReturn(Optional.of(product));

        ProductResponse productResponse = productService.getProductById(PRODUCT_ID);

        verify(productRepository).findProductById(PRODUCT_ID);
        assertEquals(productResponse.getId(), productId);
        assertEquals(productResponse.getTitle(), product.getTitle());
        assertEquals(productResponse.getDescription(), product.getDescription());
        assertEquals(productResponse.getPrice(), product.getPrice());
        assertEquals(productResponse.getCategory(), product.getCategory());
        assertEquals(productResponse.getProducer(), product.getProducer());
        assertEquals(productResponse.isAvailability(), product.isAvailability());
        assertEquals(productResponse.getImagePath(), product.getImagePath());
    }

    @Test
    @DisplayName("Add product process in successful case")
    void addProduct() {
        Product product = ProductGenerator.generateProduct(PRODUCT_ID);
        MultipartFile image = new MockMultipartFile(
                "image",
                product.getImagePath(),
                MediaType.IMAGE_JPEG_VALUE,
                "text".getBytes()

        );
        String newImageName = UUID.randomUUID() + ".jpeg";
        CreateProductRequest request = new CreateProductRequest(
                product.getTitle(),
                product.getPrice(),
                product.getDescription(),
                product.getCategory(),
                product.getProducer(),
                product.isAvailability()
        );

        when(productRepository.saveAndFlush(any())).thenReturn(product);
        when(imageService.saveImage(image)).thenReturn(newImageName);

        int result = productService.addProduct(image, request);
        assertEquals(result, PRODUCT_ID);
        verify(imageService).saveImage(image);
        verify(productRepository).saveAndFlush(any());
    }

    @Test
    @DisplayName("Delete product by id process in successful case")
    void deleteProductById() {
        Product product = ProductGenerator.generateProduct(PRODUCT_ID);
        when(productRepository.findProductById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(productRepository.saveAndFlush(product)).thenReturn(product);

        String result = productService.deleteProductById(PRODUCT_ID);

        InOrder inOrder = inOrder(productRepository);
        inOrder.verify(productRepository).findProductById(PRODUCT_ID);
        inOrder.verify(productRepository).saveAndFlush(product);
        assertNotNull(result);
    }
}