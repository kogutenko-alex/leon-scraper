package com.kogutenko.leonscraper.service;

import com.kogutenko.leonscraper.dto.MatchMarketDTO;
import com.kogutenko.leonscraper.model.*;
import com.kogutenko.leonscraper.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchProcessingService {

    public static final String PREMATCH = "prematch";
    private final LeonApiMatchService leonApiMatchService;
    private final LeonApiEventService leonApiEventService;

    public static final Set<String> NEEDED_SPORTS = Set.of("Football", "Tennis", "Ice Hockey", "Basketball");
    public static final int MAX_THREADS = 3;
    public static final int MAX_MARKETS_PER_NAME = 2;
    public static final int MAX_MATCHES_PER_LEAGUE = 2;

    public Predicate<Sport> sportFilter() {
        return s -> NEEDED_SPORTS.contains(s.getName());
    }
    public Predicate<League> leagueFilter() {
        return League::isTop;
    }
    public Predicate<Event> eventFilter() {
        return e -> PREMATCH.equalsIgnoreCase(e.getBetline());
    }

    public CompletableFuture<Void> processLeagueAsync(Sport sport, League league, String vtag, ExecutorService executor) {
        return CompletableFuture.runAsync(() -> {
            try {
                LeonApiMatchService.MatchResponse matchResponse = leonApiMatchService.fetchEvents(league.getId(), vtag);
                List<Event> events = matchResponse.getData();
                if (events == null) return;
                List<Event> prematchEvents = events.stream()
                        .filter(eventFilter())
                        .limit(MAX_MATCHES_PER_LEAGUE)
                        .toList();
                for (Event event : prematchEvents) {
                    try {
                        MatchMarketDTO dto = buildMatchMarketDTO(sport, league, event);
                        printMatchMarketDTO(dto);
                    } catch (Exception ex) {
                        log.error("Error processing event {} in league {}: {}", event.getId(), league.getName(), ex.getMessage(), ex);
                    }
                }
            } catch (Exception ex) {
                log.error("Error processing league {}: {}", league.getName(), ex.getMessage(), ex);
            }
        }, executor);
    }

    public MatchMarketDTO buildMatchMarketDTO(Sport sport, League league, Event event) {
        LeonApiEventService.EventDetails details = leonApiEventService.fetchEventDetails(event.getId());
        List<MatchMarketDTO.MarketDTO> marketDTOs = new ArrayList<>();
        if (details.getMarkets() != null) {
            Map<String, Integer> marketCount = new HashMap<>();
            for (Market market : details.getMarkets()) {
                String name = market.getName();
                int count = marketCount.getOrDefault(name, 0);
                if (count >= MAX_MARKETS_PER_NAME) continue;
                marketCount.put(name, count + 1);
                List<MatchMarketDTO.RunnerDTO> runnerDTOs = market.getRunners() == null ? List.of() : market.getRunners().stream()
                        .map(r -> MatchMarketDTO.RunnerDTO.builder()
                                .name(r.getName())
                                .price(r.getPrice())
                                .id(r.getId())
                                .build())
                        .toList();
                marketDTOs.add(MatchMarketDTO.MarketDTO.builder()
                        .name(name)
                        .runners(runnerDTOs)
                        .build());
            }
        }
        return MatchMarketDTO.builder()
                .sport(sport.getName())
                .league(league.getName())
                .matchName(event.getName())
                .kickoff(DateTimeUtil.formatUtc(event.getKickoff()))
                .matchId(event.getId())
                .markets(marketDTOs)
                .build();
    }

    public void printMatchMarketDTO(MatchMarketDTO dto) {
        System.out.printf("%s, %s%n", dto.getSport(), dto.getLeague());
        System.out.printf("\t%s, %s, %d%n", dto.getMatchName(), dto.getKickoff(), dto.getMatchId());
        for (MatchMarketDTO.MarketDTO market : dto.getMarkets()) {
            System.out.printf("\t\t%s%n", market.getName());
            for (MatchMarketDTO.RunnerDTO runner : market.getRunners()) {
                System.out.printf("\t\t\t%s, %s, %d%n", runner.getName(), runner.getPrice(), runner.getId());
            }
        }
    }
} 