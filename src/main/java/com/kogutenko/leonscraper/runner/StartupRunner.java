package com.kogutenko.leonscraper.runner;

import com.kogutenko.leonscraper.model.Sport;
import com.kogutenko.leonscraper.model.Region;
import com.kogutenko.leonscraper.model.League;
import com.kogutenko.leonscraper.service.LeonApiSportService;
import com.kogutenko.leonscraper.service.MatchProcessingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StartupRunner implements CommandLineRunner {

    final LeonApiSportService leonApiSportService;
    final MatchProcessingService matchProcessingService;
    private static final String vtag = "9c2cd386-31e1-4ce9-a140-28e9b63a9300";

    @Override
    public void run(String... args) {
        List<Sport> sports = leonApiSportService.fetchSports();
        ExecutorService executor = Executors.newFixedThreadPool(MatchProcessingService.MAX_THREADS);
        List<CompletableFuture<Void>> tasks = new ArrayList<>();
        for (Sport sport : sports.stream().filter(matchProcessingService.sportFilter()).toList()) {
            for (Region region : sport.getRegions()) {
                for (League league : region.getLeagues().stream().filter(matchProcessingService.leagueFilter()).toList()) {
                    tasks.add(matchProcessingService.processLeagueAsync(sport, league, vtag, executor));
                }
            }
        }
        CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).join();
        executor.shutdown();
    }
} 