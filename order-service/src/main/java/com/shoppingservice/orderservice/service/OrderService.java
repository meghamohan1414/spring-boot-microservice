package com.shoppingservice.orderservice.service;

import com.shoppingservice.orderservice.dto.InventoryRequest;
import com.shoppingservice.orderservice.dto.InventoryResponse;
import com.shoppingservice.orderservice.dto.OrderLineItemsDto;
import com.shoppingservice.orderservice.dto.OrderRequest;
import com.shoppingservice.orderservice.event.OrderPlacedEvent;
import com.shoppingservice.orderservice.model.Order;
import com.shoppingservice.orderservice.model.OrderLineItems;
import com.shoppingservice.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    RestTemplate restTemplate = new RestTemplate();

    public void placeOrder(OrderRequest orderRequest) {
        try {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());

            List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                    .stream()
                    .map(this::mapToDto)
                    .toList();

            order.setOrderLineItemsList(orderLineItems);

            /*Call inventory service to check if product is in stock*/

            List<InventoryRequest> inventoryRequestList = new ArrayList<>();
            for (OrderLineItems orderLineItem : orderLineItems) {
                InventoryRequest inventoryRequestListBuild = InventoryRequest.builder()
                        .skuCode(orderLineItem.getSkuCode())
                        .quantity(orderLineItem.getQuantity())
                        .build();
                inventoryRequestList.add(inventoryRequestListBuild);
            }

            HttpEntity<List<InventoryRequest>> inventoryRequestHttpEntity = new HttpEntity<>(inventoryRequestList);

            String url = "http://localhost:8082/api/inventory";

            ResponseEntity<List<InventoryResponse>> response = restTemplate.exchange(url,
                    HttpMethod.POST,
                    inventoryRequestHttpEntity,
                    new ParameterizedTypeReference<List<InventoryResponse>>() {
                    });

            List<InventoryResponse> inventoryStatus = response.getBody();

            Optional<List<InventoryResponse>> inventoryStatusList = Optional.ofNullable(inventoryStatus);
            if (!inventoryStatusList.isEmpty()) {
                if (inventoryStatus.stream().allMatch(InventoryResponse::isInStock)) {
                    orderRepository.save(order);
                    /*Publish order placed event*/
                    applicationEventPublisher.publishEvent(new OrderPlacedEvent(this, order.getOrderNumber()));

                } else {
                    List<InventoryResponse> inventoryNotAvailable = inventoryStatus.stream().filter(inventoryItem -> !inventoryItem.isInStock()).toList();
                    throw new IllegalArgumentException("Products " + inventoryNotAvailable.toString() + " is not in stock");
                }
            }
        } catch (IllegalArgumentException e) {
            throw e;
        }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());

        return orderLineItems;
    }
}
