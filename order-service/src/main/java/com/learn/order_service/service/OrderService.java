package com.learn.order_service.service;

import com.learn.order_service.client.ProductClient;
import com.learn.order_service.client.ProductResponse;
import com.learn.order_service.model.Order;
import com.learn.order_service.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductClient client;
    private final OrderRepository repository;

    @Retry(name="productService")
    @CircuitBreaker(name = "product-service",fallbackMethod = "placeOrderFallback")
    public Order placeOrder(Long productId,Integer quantity){
        ProductResponse product=client.getProductById(productId);

        Order order=new Order();
        order.setStatus("PLACED");
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setTotalPrice(product.getPrice()*quantity);

        return repository.save(order);
    }

    //Fallback Signature must match Method's Param and throwable
    public Order placeOrderFallback(Long productId,Integer quantity,Throwable t){
        Order order=new Order();
        order.setQuantity(quantity);
        order.setProductId(productId);
        order.setTotalPrice(0.0);
        order.setStatus("FAILED");
        return repository.save(order);
    }
}
