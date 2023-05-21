package com.wrona.northwnd.products;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Products {

    @Id
    @Column(name = "productid")
    private String productID;

    @Column(name = "productname")
    private String productName;

    @Column(name = "supplierid")
    private String supplierID;

    @Column(name = "categoryid")
    private String categoryID;

    @Column(name = "quantityperunit")
    private String quantityPerUnit;

    @Column(name = "unitprice")
    private String unitPrice;

    @Column(name = "unitsinstock")
    private String unitsInStock;

    @Column(name = "unitsonorder")
    private String unitsOnOrder;

    @Column(name = "reorderlevel")
    private String reorderLevel;

    private String discontinued;
}
