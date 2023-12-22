package com.simbirsoft.services.impl;

import com.simbirsoft.domain.Product;
import com.simbirsoft.dto.SortType;
import com.simbirsoft.dto.product.*;
import com.simbirsoft.exceptions.ApiRequestException;
import com.simbirsoft.repositories.ProductRepository;
import com.simbirsoft.repositories.projection.ProductParametersProjection;
import com.simbirsoft.services.ImageService;
import com.simbirsoft.services.ProductService;
import com.simbirsoft.services.mappers.product.ProductMapper;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static com.simbirsoft.constants.ErrorMessages.PRODUCT_NOT_FOUND_BY_ID;
import static com.simbirsoft.constants.OkMessages.DELETE_PRODUCT_OK;
import static com.simbirsoft.constants.OkMessages.UPDATE_PRODUCT_OK;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ImageService imageService;

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(int productId) {
        log.atDebug().log(String.format("Get product by id process: get product by id: %d", productId));
        Product product = productRepository.findProductById(productId)
                .orElseThrow(() -> {
                    log.atDebug().log(String.format("Get product by id process: not found product with id: %d", productId));
                    return new ApiRequestException(PRODUCT_NOT_FOUND_BY_ID, HttpStatus.NOT_FOUND);
                });
        return productMapper.productToProductResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductPageResponse getProductsByParams(ProductFilter productFilter) {
        log.atDebug().log(String.format("Get products by params process: start with params: %s", productFilter));
        Pageable pageable = generatePageable(
                productFilter.getPage(),
                productFilter.getPageSize(),
                productFilter.getSort());
        Page<Product> productPage = productRepository.findProductsByParams(
                productFilter.getCategory(),
                productFilter.getProducer(),
                productFilter.getQuery() != null ? productFilter.getQuery() : "",
                pageable
        );
        return productMapper.productPageToProductPageResponse(productPage);
    }

    @Override
    @Transactional
    public int addProduct(MultipartFile image, CreateProductRequest createProductRequest) {
        String imageName = image != null ? imageService.saveImage(image) : null;
        log.atDebug().log(String.format("Add product process: start with CreateProductRequest: %s and image: %s",
                createProductRequest, imageName));
        Product product = productRepository.saveAndFlush(productMapper.
                createProductRequestToProduct(createProductRequest, imageName));
        return product.getId();
    }

    @Override
    @Transactional
    public String updateProduct(MultipartFile image, UpdateProductRequest updateProductRequest) {
        String imageName = image != null ? imageService.saveImage(image) : null;
        log.atDebug().log(String.format("Update product process: start with UpdateProductRequest: %s and image: %s",
                updateProductRequest, imageName));
        productRepository.saveAndFlush(productMapper.updateProductRequestToProduct(updateProductRequest, imageName));
        return UPDATE_PRODUCT_OK;
    }

    @Override
    @Transactional
    public String deleteProductById(int productId) {
        log.atDebug().log(String.format("Delete product process: start with product id: %d", productId));
        Product product = productRepository.findProductById(productId)
                .orElseThrow(() -> {
                    log.atDebug().log(String.format("Delete product process: failed fount product with product id: %d", productId));
                    return new ApiRequestException(PRODUCT_NOT_FOUND_BY_ID, HttpStatus.NOT_FOUND);
                });
        product.setDeleted(true);
        productRepository.saveAndFlush(product);
        return DELETE_PRODUCT_OK;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductParametersResponse getProductParameters() {
        List<ProductParametersProjection> parametersProjectionList = productRepository.getAllProducersAndCategories();
        List<String> producers = productMapper.getProductPropertiesFromProductPropertiesProjectionByName(
                parametersProjectionList, "producer"
        );
        List<String> categories = productMapper.getProductPropertiesFromProductPropertiesProjectionByName(
                parametersProjectionList, "category"
        );
        List<SortType> sortTypes = Arrays.asList(SortType.values());
        return new ProductParametersResponse(
                producers,
                categories,
                sortTypes
        );
    }

    private Pageable generatePageable(int page, int pageSize, @Nullable SortType sortType) {
        Sort sort = sortType != null ? Sort.by(Sort.Direction.ASC, sortType.getParameter()) : Sort.unsorted();
        return PageRequest.of(page, pageSize, sort);
    }
}
