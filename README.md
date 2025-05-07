# Leon Scraper

## Overview
This project is a Java Spring Boot application designed to scrape and process pre-match data from leonbets.com. It utilizes the official API to fetch sports, leagues, and matches, and processes the data to extract relevant information such as markets and outcomes.

## Technologies Used
- **Java 17**: The core programming language used for development.
- **Spring Boot 3.2.3**: Provides a robust framework for building the application.
- **Lombok**: Simplifies Java code by reducing boilerplate code.
- **Jsoup**: Used for HTML parsing (though the project primarily uses API calls).
- **ExecutorService**: Manages asynchronous processing of data with up to 3 parallel threads.

## Data Fetching and Parsing
The application interacts with the leonbets.com API to retrieve data. Here's a breakdown of the process:

1. **API Endpoints**: The application uses specific API endpoints to fetch:
   - Sports data
   - Leagues data
   - Matches data
   - Event details

2. **Data Models**: The data is mapped to Java models using Lombok annotations for consistency. Key models include:
   - `Sport`: Represents a sport.
   - `Region`: Represents a region.
   - `League`: Represents a league.
   - `Event`: Represents an event (match).
   - `Market`: Represents a market.
   - `Runner`: Represents a runner.
   - `Competitor`: Represents a competitor.

3. **Filtering**: The application filters data based on specific criteria:
   - Only Football, Tennis, Ice Hockey, and Basketball are included.
   - Only top leagues (where `top==true`) are considered.
   - Only pre-match matches are processed.
   - A maximum of 2 matches per league are processed.

4. **Data Processing**: The fetched data is processed asynchronously using `ExecutorService` and `CompletableFuture`. This allows for efficient handling of multiple data streams simultaneously.

5. **Output Formatting**: The processed data is formatted into a specific console output format, utilizing DTOs (Data Transfer Objects) like `MatchMarketDTO` for structured output.

## Project Structure
- **Models**: Contains the data models used throughout the application.
- **Services**: Contains the business logic for fetching and processing data.
- **Runners**: Contains the application runners that orchestrate the data fetching and processing.
- **Utils**: Contains utility classes for formatting and other helper functions.

## Configuration
The application is configured using Spring Boot properties, and constants are defined for configuration settings such as sports, threads, and markets.

## Error Handling
The application includes error handling mechanisms to log errors per league and event, ensuring that the application can continue running even if some data fetching operations fail.

## Usage
To run the application, ensure you have Java 17 installed and Maven for dependency management. Use the following command to start the application:

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

## Contributing
Contributions are welcome! Please feel free to submit a Pull Request.

## License
This project is licensed under the MIT License.
