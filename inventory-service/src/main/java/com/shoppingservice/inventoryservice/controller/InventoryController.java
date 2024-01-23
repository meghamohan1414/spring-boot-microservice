package com.shoppingservice.inventoryservice.controller;

import com.shoppingservice.inventoryservice.dto.InventoryRequest;
import com.shoppingservice.inventoryservice.dto.InventoryResponse;
import com.shoppingservice.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    @Autowired
    private final InventoryService inventoryService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestBody List<InventoryRequest> inventoryRequestList) {

        return inventoryService.isInStock(inventoryRequestList);
    }
}
