package com.kogutenko.leonscraper.client;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
public class LeonHttpClient {
    private static final String BASE_URL = "https://leonbets.com/bets/";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";

    public Document getPage(String sport) throws IOException {
        return Jsoup.connect(BASE_URL + sport)
                .userAgent(USER_AGENT)
                .timeout(10000)
                .get();
    }

    public boolean isSiteAvailable(String sport) {
        try {
            Connection.Response resp = Jsoup.connect(BASE_URL + sport)
                    .userAgent(USER_AGENT)
                    .timeout(5000)
                    .ignoreHttpErrors(true)
                    .execute();
            String body = resp.body().toLowerCase();
            if (resp.statusCode() >= 400) return false;
            if (body.contains("немає інтернету") || body.contains("немає зв'язку") || body.contains("err_") || body.contains("connection refused") || body.contains("proxy")) {
                return false;
            }
            return true;
        } catch (SocketTimeoutException e) {
            log.warn("Timeout while checking site availability: {}", e.getMessage());
            return false;
        } catch (IOException e) {
            log.warn("IOException while checking site availability: {}", e.getMessage());
            return false;
        }
    }

    public Map<String, String> getAvailableSports() throws IOException {
        Document doc = Jsoup.connect(BASE_URL + "soccer")
                .userAgent(USER_AGENT)
                .timeout(10000)
                .get();
        Map<String, String> sports = new LinkedHashMap<>();
        doc.select("div.swiper__wrapper_b1B0Q a.sportline-filter__item_HgLvb").forEach(a -> {
            String href = a.attr("href");
            if (href.startsWith("/bets/")) {
                String slug = href.replace("/bets/", "").trim();
                String name = a.selectFirst("span.sportline-filter__item-title_QD4Dy").text();
                sports.put(slug, name);
            }
        });
        return sports;
    }
} 