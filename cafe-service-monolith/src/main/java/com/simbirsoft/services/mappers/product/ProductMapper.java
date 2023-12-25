package com.simbirsoft.services.mappers.product;

import com.simbirsoft.domain.Product;
import com.simbirsoft.dto.product.CreateProductRequest;
import com.simbirsoft.dto.product.ProductPageResponse;
import com.simbirsoft.dto.product.ProductResponse;
import com.simbirsoft.dto.product.UpdateProductRequest;
import com.simbirsoft.repositories.projection.ProductParametersProjection;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {
    public ProductResponse productToProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getTitle(),
                product.getPrice(),
                product.getDescription(),
                product.getCategory(),
                product.getProducer(),
                product.isAvailability(),
                product.getImagePath()
        );
    }

    public ProductPageResponse productPageToProductPageResponse(Page<Product> productPage) {
        return new ProductPageResponse(
                productPage
                        .stream()
                        .map(this::productToProductResponse)
                        .toList(),
                productPage.getTotalPages(),
                productPage.hasNext()
        );
    }

    public Product createProductRequestToProduct(CreateProductRequest request, String imageName) {
        Product product = new Product();
        product.setTitle(request.getTitle());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setProducer(request.getProducer());
        product.setAvailability(request.isAvailability());
        product.setImagePath(imageName);
        return product;
    }

    public Product updateProductRequestToProduct(UpdateProductRequest request, String imageName) {
        Product product = new Product();
        product.setId(request.getId());
        product.setTitle(request.getTitle());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setProducer(request.getProducer());
        product.setAvailability(request.isAvailability());
        product.setImagePath(imageName);
        return product;
    }

    public List<String> getProductPropertiesFromProductPropertiesProjectionByName(
            List<ProductParametersProjection> projections,
            String propertyName
    ) {
        return projections
                .stream()
                .filter(parameter -> parameter.getParameterName().equals(propertyName))
                .map(ProductParametersProjection::getParameter)
                .toList();
    }
}
