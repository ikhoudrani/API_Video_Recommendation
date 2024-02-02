package com.api.videoReference.service;

import com.api.videoReference.model.Movie;
import com.api.videoReference.model.Series;
import com.api.videoReference.model.Video;
import com.api.videoReference.repository.VideoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class VideoService {
    private final ObjectMapper objectMapper;
    private final VideoRepository videoRepository;

    @Autowired
    public VideoService(VideoRepository videoRepository, ObjectMapper objectMapper) {
        this.videoRepository = videoRepository;
        this.objectMapper = objectMapper;
    }

    public final Video generateVideo(JsonNode videoNode) throws Exception {
        if (videoNode.has("director") && videoNode.has("releaseDate")) {
            return this.objectMapper.treeToValue(videoNode, Movie.class);
        } else if (videoNode.has("numberOfEpisodes")) {
            return this.objectMapper.treeToValue(videoNode, Series.class);
        } else {
            throw new Exception("Bad video format : should be a movie or a series");
        }
    }


    public List<Video> searchVideosByTitle(String title) {
        return videoRepository.getAllVideos().stream()
                .filter(video -> video.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Video> filterVideosByMovie() {
        return videoRepository.getAllVideos().stream()
                .filter(Movie.class::isInstance)
                .map(Movie.class::cast)
                .collect(Collectors.toList());
    }

    public List<Video> filterVideosBySeries() {
        return videoRepository.getAllVideos().stream()
                .filter(Series.class::isInstance)
                .map(Series.class::cast)
                .collect(Collectors.toList());
    }

    public List<Video> findSimilarVideos(String videoId, int minCommonLabels) {
        Video video = videoRepository.getVideo(videoId);
        if (video == null) return List.of();

        List<String> labels = video.getLabels();

        return videoRepository.getAllVideos().stream()
                .filter(similarVideo -> !similarVideo.getId().equals(videoId))
                .filter(similarVideo -> {
                    List<String> commonLabels = similarVideo.getLabels();
                    long commonLabelsCount = labels.stream().filter(commonLabels::contains).count();
                    return commonLabelsCount >= minCommonLabels;
                })
                .collect(Collectors.toList());
    }
}
