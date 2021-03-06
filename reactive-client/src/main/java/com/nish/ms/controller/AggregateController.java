package com.nish.ms.controller;

import com.nish.ms.model.CustomerDTO;
import com.nish.ms.model.ProductDTO;
import com.nish.ms.model.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
public class AggregateController {


    private final String customerServiceURL = "//CUSTOMER-SERVICE/api/v1/customers";
    private final String productServiceURL = "//PRODUCT-SERVICE/api/v1/product";

    @Autowired
    private WebClient client;

    @Autowired
    private ResponseDTO dto;

    @GetMapping(path = "/aggregate/srch")
    public Mono<ResponseDTO> getDetails() {


        return client.get()
                .uri("lb://CUSTOMER-SERVICE", uriBuilder -> uriBuilder.path("/api/v1/customers")
						.build())
                        .retrieve()
                        .bodyToMono(CustomerDTO.class)
                        .zipWhen(custResp -> client.get()
                                        .uri("lb://PRODUCT-SERVICE", uriBuilder -> uriBuilder.path("/api/v1/product")
                                                .build(custResp.getCustomerName())).retrieve().bodyToMono(ProductDTO.class),
                                (custResp, prodResp) -> {
                                    dto.setCustomer(custResp);
                                    dto.setProduct(prodResp);
                                    return dto;
                                });

    }

}
