package org.appsdeveloperblog.ws.products.service;

import org.appsdeveloperblog.ws.products.rest.CreateProductRestModel;

public interface ProductService {

    String createProduct(CreateProductRestModel product) throws Exception;
}
