package com.wrona.northwnd.products;

import jdk.jfr.ContentType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;
    @GetMapping
    public List<ProductEntity> getSuchProducts(String string){
        return productService.getSuchProducts(string);
    }
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
//    @GetMapping
//    public List<Products> getAllProducts(){
//        return productService.getAllProducts();
//    }
    @PostMapping
    public void AddNewProduct(@RequestBody ProductRequest productRequest){
        productService.AddNewProduct(productRequest);
    }
}
