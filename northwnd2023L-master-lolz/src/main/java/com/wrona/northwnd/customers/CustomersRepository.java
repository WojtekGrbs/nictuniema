package com.wrona.northwnd.customers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomersRepository extends JpaRepository<Customers, String> {

    List<Customers> findAllByCountryStartingWith(String country);

    @Query(value = "SELECT * FROM Customers c WHERE NOT EXISTS (SELECT * FROM Orders o WHERE o.CustomerId = c.CustomerId)", nativeQuery = true)
    List<Customers> findAllWithoutOrders();
}
