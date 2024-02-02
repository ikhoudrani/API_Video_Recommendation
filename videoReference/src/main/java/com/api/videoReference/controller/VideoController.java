package com.api.videoReference.controller;

import com.api.videoReference.model.Video;
import com.api.videoReference.repository.VideoRepository;
import com.api.videoReference.service.VideoService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/videos")
public class VideoController {

    private final VideoService videoService;
    private final VideoRepository videoRepository;

    @Autowired

    public VideoController(VideoService videoService, VideoRepository videoRepository) {
        this.videoService = videoService;
        this.videoRepository = videoRepository;
    }

    @PostMapping
    public ResponseEntity<?> addVideoPOST(@RequestBody JsonNode videoNode) {
        try {
            Video videoToAdd = this.videoService.generateVideo(videoNode);
            videoRepository.addVideo(videoToAdd);
            return ResponseEntity.status(HttpStatus.CREATED).body(videoToAdd);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Video> getVideoGET(@PathVariable String id) {
        Video video = videoRepository.getVideo(id);
        if (video != null) {
            return ResponseEntity.ok(video);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Video>> searchVideosByTitleGET(@RequestParam String title) {
        if (title.length() < 3) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            List<Video> videosByTitleList = videoService.searchVideosByTitle(title);
            if (!videosByTitleList.isEmpty()) {
                return ResponseEntity.ok(videosByTitleList);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }
    }

    @GetMapping("/movies")
    public ResponseEntity<List<Video>> getMovies() {
        List<Video> movies = videoService.filterVideosByMovie();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/series")
    public ResponseEntity<List<Video>> getSeries() {
        List<Video> series = videoService.filterVideosBySeries();
        return ResponseEntity.ok(series);
    }

    @GetMapping("/{videoId}/similar")
    public ResponseEntity<List<Video>> findSimilarVideos(@PathVariable String videoId, @RequestParam int minLabels) {
        List<Video> similarVideos = videoService.findSimilarVideos(videoId, minLabels);
        if (similarVideos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(similarVideos);
    }
}
