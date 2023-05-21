package com.wrona.northwnd.customers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientResponse {

    private String customerId;
    private String companyName;
    private String contactName;
    private String contactTitle;
    private String address;
    private String city;
    private String region;
    private String postalCode;
    private String country;
    private String phone;
    private String fax;
}
