package com.kogutenko.leonscraper.model;

import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Competitor {
    long id;
    String name;
    String homeAway;
    String logo;
} 