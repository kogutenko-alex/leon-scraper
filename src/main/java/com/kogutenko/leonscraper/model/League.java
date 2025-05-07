package com.kogutenko.leonscraper.model;

import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class League {
    private long id;
    private String name;
    private String nameDefault;
    private String url;
    private int weight;
    private int prematch;
    private int inplay;
    private int outright;
    private boolean top;
    private int topOrder;
    private boolean hasZeroMarginEvents;
    private String logoUrl;
    private String background;
} 