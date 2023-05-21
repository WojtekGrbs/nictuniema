package com.wrona.northwnd.products;

import lombok.Data;

@Data
public class ProductEntity {
    private String productID;
    private String productName;
    private String supplierID;
    private String categoryID;
    private String quantityPerUnit;
    private String unitPrice;
    private String unitsInStock;
    private String unitsOnOrder;
    private String reorderLevel;
    private String discontinued;
}

