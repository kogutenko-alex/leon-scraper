package com.kogutenko.leonscraper.model;

import lombok.Data;
import java.util.List;

@Data
public class SportsResponse {
    private String vtag;
    private List<Sport> sports;
} 