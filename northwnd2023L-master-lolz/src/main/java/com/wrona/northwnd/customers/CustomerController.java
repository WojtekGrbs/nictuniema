package com.wrona.northwnd.customers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public Clients findAllCustomerByCountry(@RequestParam(defaultValue = "") String country, @RequestParam(defaultValue = "0") int page) {
        return customerService.findAllCustomerByCountry(country, page);
    }

    @PostMapping
    public void findAllCustomerByCountry(@RequestBody ClientRequest request) {
        customerService.createClient(request);
    }
}
