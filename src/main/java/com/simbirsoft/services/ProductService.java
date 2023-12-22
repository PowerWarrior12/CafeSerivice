package com.simbirsoft.services;

import com.simbirsoft.dto.product.*;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    ProductResponse getProductById(int productId);
    ProductPageResponse getProductsByParams(ProductFilter productFilter);

    /**
     * @param image Icon of adding product
     * @param createProductRequest Data of adding product
     * @return id of added product
     */
    int addProduct(MultipartFile image, CreateProductRequest createProductRequest);
    /**
     * @param image Icon of updating product
     * @param updateProductRequest Data of updating product
     * @return id of added product
     */
    String updateProduct(MultipartFile image, UpdateProductRequest updateProductRequest);
    String deleteProductById(int productId);
    ProductParametersResponse getProductParameters();

}
