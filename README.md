# Leon Scraper

## Description

A pre-match parser for leonbets.com. Works via the official API, without any browser or browser emulator. Asynchronous processing in up to 3 parallel threads. Outputs structured data to the console.

## Requirements
- Java 17+
- Maven 3.6+

## Usage

```bash
# Clone the repository
git clone https://github.com/kogutenko-alex/leon-scraper
cd leon-scraper

# Build the project
mvn clean package

# Run
mvn spring-boot:run
```

## Features
- Parses Football, Tennis, Ice Hockey, Basketball
- Only top leagues
- For each top league: first 2 prematch matches
- For each match: all markets from the "All markets" tab (max 2 markets with the same name)
- Outputs structured data to the console

## Configuration
- All constants (number of threads, sports, markets, etc.) are in `MatchProcessingService`

## Project Structure
- `src/main/java/com/kogutenko/leonscraper/model` — data models
- `src/main/java/com/kogutenko/leonscraper/service` — services
- `src/main/java/com/kogutenko/leonscraper/dto` — output DTOs
- `src/main/java/com/kogutenko/leonscraper/util` — utilities
- `src/main/java/com/kogutenko/leonscraper/runner` — startup runner
