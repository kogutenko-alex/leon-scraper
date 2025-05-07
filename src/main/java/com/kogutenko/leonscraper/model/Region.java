package com.kogutenko.leonscraper.model;

import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Region {
    long id;
    String name;
    String family;
    String url;
    List<League> leagues;
} 