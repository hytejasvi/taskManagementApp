package com.hytejasvi.taskManagementApp.service;

import com.hytejasvi.taskManagementApp.api.response.QuotesApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

@Component
@Slf4j
public class QuotesService {

    @Autowired
    private RestTemplate restTemplate;

    public QuotesApiResponse getQuote() {
        String quoteApi = "https://quoteslate.vercel.app/api/quotes/random";
        log.info("final api: {}", quoteApi);
        ResponseEntity<QuotesApiResponse> response = restTemplate.exchange(
                quoteApi,
                HttpMethod.GET,
                null,
                QuotesApiResponse.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            log.debug(response.toString());
            log.info(response.getBody().getQuote());
            return response.getBody();
        } else {
            log.error("Failed to fetch quote. Status code: {}", response.getStatusCode());
            return null; // Or handle as you prefer
        }
    }
}
