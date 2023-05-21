package com.wrona.northwnd.customers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerEntityRepository customerEntityRepository;
    private final CustomersRepository customersRepository;

    public Clients findAllCustomerByCountry(String country, int page) {

        try {
            List<CustomerEntity> entities = customerEntityRepository.findAllCustomerByCountry(country, page);
            List<Customers> customers = customersRepository.findAllByCountryStartingWith(country);
            List<Customers> customersWithoutOrders = customersRepository.findAllWithoutOrders();

            List<ClientResponse> clients = entities.stream()
                    .map(client -> {
                        ClientResponse clientResponse = new ClientResponse();
                        clientResponse.setCustomerId(client.getCustomerId());
                        clientResponse.setCompanyName(client.getCompanyName());
                        clientResponse.setContactName(client.getContactName());
                        clientResponse.setContactTitle(client.getContactTitle());
                        clientResponse.setCity(client.getCity());
                        clientResponse.setRegion(client.getRegion());
                        clientResponse.setPostalCode(client.getPostalCode());
                        clientResponse.setCountry(client.getCountry());
                        clientResponse.setAddress(client.getAddress());
                        clientResponse.setPhone(client.getPhone());
                        clientResponse.setFax(client.getFax());
                        return clientResponse;
                    })
                    .collect(Collectors.toList());

            Clients response = new Clients();
            response.setClients(clients);

            return response;

        } catch (SQLException ignore) {
        }
        return null;
    }

    public void createClient(ClientRequest request) {
        try {
            customerEntityRepository.createClient(request);
        } catch (SQLException ignore) {
        }
    }

}
