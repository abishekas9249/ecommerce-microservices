package com.learn.order_service.controller;

import com.learn.order_service.client.ProductClient;
import com.learn.order_service.client.ProductResponse;
import com.learn.order_service.model.Order;
import com.learn.order_service.repository.OrderRepository;
import com.learn.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestParam Long productId,@RequestParam Integer quantity){
        Order order=orderService.placeOrder(productId,quantity);
        HttpStatus status="FAILED".equals(order.getStatus())?HttpStatus.SERVICE_UNAVAILABLE:HttpStatus.CREATED;
        return ResponseEntity.status(status).body(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable("id") Long id){
        return orderRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
