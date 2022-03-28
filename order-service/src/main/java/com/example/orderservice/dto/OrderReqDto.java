package com.example.orderservice.dto;

import lombok.Data;

@Data
public class OrderReqDto {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
}
