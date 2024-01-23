package com.shoppingservice.orderservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class InventoryResponseList {
    private List<InventoryRequest> inventoryResponseList;
}
