package com.coffeeordersystem.dataplatform.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class DataPlatformClient {

    private final RestClient restClient;

    public DataPlatformClient(@Qualifier("dataPlatformRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public void send(DataPlatformPayload payload) {
        restClient.post()
                .uri("/collect/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .toBodilessEntity();
    }
}
