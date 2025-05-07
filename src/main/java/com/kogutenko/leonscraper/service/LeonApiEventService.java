package com.kogutenko.leonscraper.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kogutenko.leonscraper.model.Market;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class LeonApiEventService {
    private static final String API_URL = "https://leonbets.com/api-2/betline/event/all?ctag=en-US&eventId=%d&flags=reg,urlv2,mm2,rrc,nodup,smgv2,outv2";
    private final RestTemplate restTemplate = new RestTemplate();

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EventDetails {
        private List<Market> markets;
    }

    public EventDetails fetchEventDetails(long eventId) {
        String url = String.format(API_URL, eventId);
        return restTemplate.getForObject(url, EventDetails.class);
    }
} 