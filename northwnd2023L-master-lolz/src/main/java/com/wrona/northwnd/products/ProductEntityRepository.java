package com.wrona.northwnd.products;

import com.wrona.northwnd.customers.CustomerEntity;
import org.springframework.stereotype.Repository;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Repository
@Slf4j
@AllArgsConstructor
public class ProductEntityRepository {
    private final HikariDataSource hikariDataSource;

    public List<ProductEntity> getSuchProducts(String string) {
        String sql = "SELECT * FROM Products WHERE ProductName LIKE %s";
        String query = String.format(sql, string);
        List<ProductEntity> productEntities = new ArrayList<>();
        try {
            Connection connection = hikariDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();


            while(resultSet.next()){
                ProductEntity productEntity = new ProductEntity();
                productEntity.setProductID(resultSet.getString("ProductID"));
                productEntity.setProductName(resultSet.getString("ProductName"));
                productEntity.setCategoryID(resultSet.getString("CategoryID"));
                productEntity.setDiscontinued(resultSet.getString("Discontinued"));
                //itd
                productEntities.add(productEntity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return productEntities;
    }
    public void AddNewProduct(ProductRequest productRequest) {
        String query = "INSERT INTO Products (ProductID, CategoryID) values(%s, %s)";
        String sql = String.format(query, productRequest.getProductID(), productRequest.getCategoryID());
        try {
            Connection connection = hikariDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

