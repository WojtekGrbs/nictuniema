package com.wrona.northwnd.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products, String> {
    //@Query(value = "SELECT * FROM Products", nativeQuery = true)
    //List<Products> getAllProducts();
}
