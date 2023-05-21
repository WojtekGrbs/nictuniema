package com.wrona.northwnd.customers;

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

@Slf4j
@Repository
@AllArgsConstructor
public class CustomerEntityRepository {

    private final static int PAGE_SIZE = 3;

    private final HikariDataSource hikariDataSource;


    public List<CustomerEntity> findAllCustomerByCountry(String country, int page) throws SQLException {
        int offset = page * PAGE_SIZE;
        String query = "SELECT * FROM Customers WHERE Country LIKE '%s%%' ORDER BY CustomerID OFFSET %d ROWS FETCH NEXT %d ROWS ONLY";
        String sql = String.format(query, country, offset, PAGE_SIZE);

        log.info(sql);

        List<CustomerEntity> customers = new ArrayList<>();
        try (Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet results = preparedStatement.executeQuery();

            while (results.next()) {
                CustomerEntity customer = new CustomerEntity();
                customer.setCustomerId(results.getString("CustomerID"));
                customer.setCompanyName(results.getString("CompanyName"));
                customer.setContactName(results.getString("ContactName"));
                customer.setContactTitle(results.getString("ContactTitle"));
                customer.setAddress(results.getString("Address"));
                customer.setCity(results.getString("City"));
                customer.setRegion(results.getString("Region"));
                customer.setPostalCode(results.getString("PostalCode"));
                customer.setCountry(results.getString("Country"));
                customer.setPhone(results.getString("Phone"));
                customer.setFax(results.getString("Fax"));
                customers.add(customer);
            }
        }
        return customers;
    }

    public void createClient(ClientRequest request) throws SQLException {
        String query = "insert into Customers (CustomerID, CompanyName, ContactName, ContactTitle, Address, City, Region, PostalCode, Country, Phone, Fax) values (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s);";
        //TODO map request to SQL query - fill query mask with parameters from request
        String sql = String.format(query);

        log.info(sql);

        try (Connection connection = hikariDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int rows = preparedStatement.executeUpdate();
            log.info("{} clients inserted successfully", rows);
        }
    }

}
