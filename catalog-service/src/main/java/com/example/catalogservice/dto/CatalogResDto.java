package com.example.catalogservice.dto;


import com.example.catalogservice.domain.Catalog;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogResDto {
    private String productId;
    private String productName;
    private Integer stork;
    private Integer unitPrice;
    private Date createdAt;

    @Builder
    public CatalogResDto(Catalog entity){
        this.productId = entity.getProductId();
        this.productName = entity.getProductName();
        this.stork = entity.getStork();
        this.unitPrice = entity.getUnitPrice();
        this.createdAt = entity.getCreatedAt();

    }
}
