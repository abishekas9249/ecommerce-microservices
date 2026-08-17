package com.learn.product_service.controller;

import com.learn.product_service.model.Product;
import com.learn.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepo;

    @GetMapping
    public List<Product> getAllProducts(){
        return productRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id){
        return productRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound()
                        .build());
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product){
        return productRepo.save(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id,@RequestBody Product product){

        return productRepo.findById(id).map(exists->{
            exists.setName(product.getName());
            exists.setDescription(product.getDescription());
            exists.setPrice(product.getPrice());
            exists.setQuantity(product.getQuantity());
            return ResponseEntity.ok(productRepo.save(exists));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        if (!productRepo.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        productRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
