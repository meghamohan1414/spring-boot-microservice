package com.shoppingservice.orderservice.event;

import lombok.Data;

@Data
public class OrderPlacedEvent {

    private String orderNumber;

    public OrderPlacedEvent(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public OrderPlacedEvent(Object source, String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
