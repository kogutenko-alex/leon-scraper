package com.kogutenko.leonscraper.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SportMatchData {
    private String sport;
    private String league;
    private String homeTeam;
    private String awayTeam;
    private LocalDateTime matchTime;
    private String matchId;
    private Map<String, List<BetOption>> betOptions;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BetOption {
        private String name;
        private double odds;
        private String id;

        public String getName() { return name; }
        public double getOdds() { return odds; }
        public String getId() { return id; }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(sport).append(", ").append(league).append("\n");
        sb.append(homeTeam).append(" - ").append(awayTeam).append(", ")
          .append(matchTime).append(" UTC, ").append(matchId).append("\n");
        
        betOptions.forEach((betType, options) -> {
            sb.append("  ").append(betType).append("\n");
            options.forEach(option -> 
                sb.append("    ").append(option.getName())
                  .append(", ").append(option.getOdds())
                  .append(", ").append(option.getId()).append("\n")
            );
        });
        
        return sb.toString();
    }
} 