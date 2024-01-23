package com.shoppingservice.orderservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryRequest {
    private String skuCode;
    private Integer quantity;
}
