package com.wrona.northwnd.customers;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Customers {

    @Id
    @Column(name = "CustomerID")
    private String customerId;

    @Column(name = "companyname")
    private String companyName;

    @Column(name = "contactname")
    private String contactName;

    @Column(name = "contacttitle")
    private String contactTitle;

    private String address;
    private String city;
    private String region;

    @Column(name = "postalcode")
    private String postalCode;

    private String country;
    private String phone;
    private String fax;
}
