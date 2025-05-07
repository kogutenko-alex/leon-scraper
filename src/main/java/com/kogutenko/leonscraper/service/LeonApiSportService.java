package com.kogutenko.leonscraper.service;

import com.kogutenko.leonscraper.model.Sport;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;

@Service
public class LeonApiSportService {
    private static final String API_URL = "https://leonbets.com/api-2/betline/sports?ctag=en-US&flags=urlv2";
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Sport> fetchSports() {
        ResponseEntity<Sport[]> response = restTemplate.getForEntity(API_URL, Sport[].class);
        return Arrays.asList(response.getBody());
    }
} 