package com.shoppingservice.orderservice.controller;

import com.shoppingservice.orderservice.dto.OrderRequest;
import com.shoppingservice.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest) {
        try {
            orderService.placeOrder(orderRequest);
            return "Order placed successfully";
        } catch(Exception e) {
            return "Order not placed due to the following exception. Please Try again later! "+e.getMessage();
        }

    }
}
