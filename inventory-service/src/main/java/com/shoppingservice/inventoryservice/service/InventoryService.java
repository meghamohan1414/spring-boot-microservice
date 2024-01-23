package com.shoppingservice.inventoryservice.service;

import com.shoppingservice.inventoryservice.dto.InventoryRequest;
import com.shoppingservice.inventoryservice.dto.InventoryResponse;
import com.shoppingservice.inventoryservice.model.Inventory;
import com.shoppingservice.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {

    @Autowired
    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<InventoryRequest> inventoryRequestList) {
        List<String> skuCodes = inventoryRequestList.stream()
                .map(InventoryRequest::getSkuCode)
                .collect(Collectors.toList());


        return inventoryRepository.findBySkuCodeIn(skuCodes).stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .skuCode(inventory.getSkuCode())
                                .availableQuantity(inventory.getQuantity())
                                .isInStock(isRequestedQuantityAvailable(inventory, inventoryRequestList))
                                .build()
                ).toList();
    }

    private boolean isRequestedQuantityAvailable(Inventory inventory, List<InventoryRequest> inventoryRequestList) {
        Optional<InventoryRequest> inventoryRequest = inventoryRequestList.stream()
                .filter(inventoryRequest1 ->
                        inventoryRequest1.getSkuCode().equals(inventory.getSkuCode()))
                .findFirst();
        if (inventoryRequest.isPresent())
            return (inventoryRequest.get().getQuantity() <= inventory.getQuantity());
        return false;
    }
}
