package com.api.videoReference.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Movie extends Video {
    private String director;
    private String releaseDate;

    @JsonCreator
    public Movie(
            @JsonProperty("id") String id,
            @JsonProperty("title") String title,
            @JsonProperty("labels") List<String> labels,
            @JsonProperty("director") String director,
            @JsonProperty("releaseDate") String releaseDate
    ) {
        super(id, title, labels);
        this.director = director;
        this.releaseDate = releaseDate;
    }

    // Getters
    public String getDirector() {
        return director;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

}
