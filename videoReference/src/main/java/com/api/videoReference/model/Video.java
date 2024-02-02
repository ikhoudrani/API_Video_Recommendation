package com.api.videoReference.model;


import java.util.List;

public abstract class Video {
    private String id;
    private String title;
    private List<String> labels;

    public Video(String id, String title, List<String> labels) {
        this.id = id;
        this.title = title;
        this.labels = labels;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public List<String> getLabels() { return labels; }
}

