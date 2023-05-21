package com.wrona.northwnd.products;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductEntityRepository productEntityRepository;
    private final ProductRepository productRepository;

//    public List<Products> getAllProducts() {
//        return productRepository.getAllProducts();
//    }
    public void AddNewProduct(ProductRequest productRequest) {
        productEntityRepository.AddNewProduct(productRequest);
    }

    public List<ProductEntity> getSuchProducts(String string) {
        return productEntityRepository.getSuchProducts(string);
    }
}
