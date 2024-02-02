package com.api.videoReference.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Series extends Video {
    private int numberOfEpisodes;

    @JsonCreator
    public Series(
            @JsonProperty("id") String id,
            @JsonProperty("title") String title,
            @JsonProperty("labels") List<String> labels,
            @JsonProperty("numberOfEpisodes") int numberOfEpisodes
    ) {
        super(id, title, labels);
        this.numberOfEpisodes = numberOfEpisodes;
    }

    // Getter
    public int getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

}
