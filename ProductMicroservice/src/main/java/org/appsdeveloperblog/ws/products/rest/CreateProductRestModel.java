package org.appsdeveloperblog.ws.products.rest;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class CreateProductRestModel {

    private String title;
    private BigDecimal price;
    private Integer quantity;

}
