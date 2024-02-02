package com.api.videoReference.repository;

import com.api.videoReference.model.Video;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class VideoRepository {
    private final Map<String, Video> videos = new HashMap<>();

    public Video addVideo(Video video) {
        videos.put(video.getId(), video);
        return video;
    }

    public Video getVideo(String id) {
        return videos.get(id);
    }

    public Collection<Video> getAllVideos() {
        return videos.values();
    }
}
