package com.kogutenko.leonscraper.model;

import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    long id;
    String name;
    long kickoff;
    List<Competitor> competitors;
    String betline;
} 