package com.kogutenko.leonscraper.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class MatchMarketDTO {
    private String sport;
    private String league;
    private String matchName;
    private String kickoff;
    private long matchId;
    private List<MarketDTO> markets;

    @Data
    @Builder
    public static class MarketDTO {
        private String name;
        private List<RunnerDTO> runners;
    }

    @Data
    @Builder
    public static class RunnerDTO {
        private String name;
        private double price;
        private long id;
    }
} 