package com.kogutenko.leonscraper.service;

import com.kogutenko.leonscraper.client.LeonHttpClient;
import com.kogutenko.leonscraper.model.SportMatchData;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeonScraperService {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final int MATCHES_PER_LEAGUE = 2;

    LeonHttpClient httpClient;

    @Async("taskExecutor")
    public CompletableFuture<List<SportMatchData>> scrapeMatches() {
        return scrapeMatches("soccer");
    }

    @Async("taskExecutor")
    public CompletableFuture<List<SportMatchData>> scrapeMatches(String sportSlug) {
        log.info("Starting to scrape matches from Leon [{}]", sportSlug);
        if (!httpClient.isSiteAvailable(sportSlug)) {
            log.error("Leon site is not available or blocked in your region [{}]", sportSlug);
            throw new IllegalStateException("Leon site is not available or blocked in your region");
        }
        try {
            log.info("Fetching page from Leon [{}]", sportSlug);
            Document doc = httpClient.getPage(sportSlug);
            List<SportMatchData> matches = new ArrayList<>();
            
            // Get all leagues
            Elements leagues = doc.select("div.sport-section");
            log.info("Found {} leagues", leagues.size());
            
            for (Element league : leagues) {
                String sport = league.selectFirst("div.sport-section__header").text();
                String leagueName = league.selectFirst("div.sport-section__title").text();
                log.info("Processing league: {} - {}", sport, leagueName);
                
                // Get matches for this league
                Elements matchElements = league.select("div.sport-event");
                int matchCount = 0;
                
                for (Element matchElement : matchElements) {
                    if (matchCount >= MATCHES_PER_LEAGUE) {
                        log.info("Reached limit of {} matches for league {}", MATCHES_PER_LEAGUE, leagueName);
                        break;
                    }
                    
                    SportMatchData match = parseMatch(matchElement);
                    if (match != null) {
                        match.setSport(sport);
                        match.setLeague(leagueName);
                        matches.add(match);
                        matchCount++;
                        log.info("Added match: {} vs {}", match.getHomeTeam(), match.getAwayTeam());
                    }
                }
            }
            
            log.info("Successfully scraped {} matches", matches.size());
            return CompletableFuture.completedFuture(matches);
        } catch (IOException e) {
            log.error("Failed to scrape matches: {}", e.getMessage(), e);
            return CompletableFuture.failedFuture(e);
        }
    }

    private SportMatchData parseMatch(Element matchElement) {
        try {
            SportMatchData match = new SportMatchData();
            
            // Parse teams
            Element teamsElement = matchElement.selectFirst("div.sport-event__teams");
            String[] teams = teamsElement.text().split(" - ");
            match.setHomeTeam(teams[0].trim());
            match.setAwayTeam(teams[1].trim());
            
            // Parse match time
            String timeStr = matchElement.selectFirst("div.sport-event__time").text();
            match.setMatchTime(LocalDateTime.parse(timeStr, DATE_FORMATTER));
            
            // Parse match ID
            match.setMatchId(matchElement.attr("data-event-id"));
            
            // Parse bet options
            Map<String, List<SportMatchData.BetOption>> betOptions = new HashMap<>();
            Elements betGroups = matchElement.select("div.sport-event__market");
            
            for (Element betGroup : betGroups) {
                String betType = betGroup.selectFirst("div.sport-event__market-name").text();
                List<SportMatchData.BetOption> options = new ArrayList<>();
                
                Elements optionsElements = betGroup.select("div.sport-event__selection");
                for (Element option : optionsElements) {
                    String name = option.selectFirst("div.sport-event__selection-name").text();
                    double odds = Double.parseDouble(option.selectFirst("div.sport-event__selection-odds").text());
                    String id = option.attr("data-selection-id");
                    options.add(new SportMatchData.BetOption(name, odds, id));
                }
                
                betOptions.put(betType, options);
            }
            
            match.setBetOptions(betOptions);
            return match;
        } catch (Exception e) {
            log.error("Error parsing match: {}", e.getMessage(), e);
            return null;
        }
    }

    public void logAvailableSports() {
        try {
            Map<String, String> sports = httpClient.getAvailableSports();
            log.info("Available sports:");
            sports.forEach((slug, name) -> log.info("{} -> {}", slug, name));
        } catch (Exception e) {
            log.error("Failed to get available sports: {}", e.getMessage(), e);
        }
    }
} 