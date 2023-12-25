package com.simbirsoft.common;

import com.simbirsoft.domain.Product;
import com.simbirsoft.dto.product.CreateProductRequest;
import com.simbirsoft.dto.product.UpdateProductRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.simbirsoft.common.TestConstants.*;

public class ProductGenerator {
    public static Random random = new Random();
    public static Product generateProduct(int index) {
        Product product = new Product();
        product.setId(index);
        product.setTitle(PRODUCT_TITLE + "_" + index);
        product.setDescription(PRODUCT_DESCRIPTION + "_" + index);
        product.setPrice(PRODUCT_PRICE * random.nextInt() % 10);
        product.setCategory(PRODUCT_CATEGORY + "_" + random.nextInt() % 3);
        product.setProducer(PRODUCT_PRODUCER + "_" + random.nextInt() % 5);
        product.setAvailability(random.nextBoolean());
        product.setImagePath(index + "_" + PRODUCT_IMAGE_PATH);
        return product;
    }

    public static List<Product> generateProducts(int count) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            products.add(generateProduct(i));
        }
        return products;
    }
    public static CreateProductRequest generateCreateProductRequest() {
        return new CreateProductRequest(
                PRODUCT_TITLE,
                PRODUCT_PRICE,
                PRODUCT_DESCRIPTION,
                PRODUCT_CATEGORY,
                PRODUCT_PRODUCER,
                PRODUCT_AVAILABILITY
        );
    }

    public static UpdateProductRequest generateUpdateProductRequest() {
        return new UpdateProductRequest(
                PRODUCT_ID,
                PRODUCT_TITLE,
                PRODUCT_PRICE,
                PRODUCT_DESCRIPTION,
                PRODUCT_CATEGORY,
                PRODUCT_PRODUCER,
                PRODUCT_AVAILABILITY
        );
    }
}
