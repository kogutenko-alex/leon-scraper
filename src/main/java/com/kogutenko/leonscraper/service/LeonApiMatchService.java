package com.kogutenko.leonscraper.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kogutenko.leonscraper.model.Event;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class LeonApiMatchService {
    private static final String API_URL = "https://leonbets.com/api-2/betline/changes/all?ctag=en-US&flags=reg,urlv2,mm2,rrc,nodup&hideClosed=true&league_id=%d&vtag=%s";
    private final RestTemplate restTemplate = new RestTemplate();

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MatchResponse {
        private String vtag;
        private List<Event> data;
    }

    public MatchResponse fetchEvents(long leagueId, String vtag) {
        String url = String.format(API_URL, leagueId, vtag);
        return restTemplate.getForObject(url, MatchResponse.class);
    }
} 