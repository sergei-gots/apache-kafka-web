package org.appsdeveloperblog.ws.products.rest;

import lombok.extern.slf4j.Slf4j;
import org.appsdeveloperblog.ws.products.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@Slf4j
@RestController
@RequestMapping("/products")    //http://localhost:<port>/products
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody CreateProductRestModel product) {

        String productId;
        try {
            productId = productService.createProduct(product);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorMessage(Instant.now(), "Failed to create a product", e)
            );
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }
}
