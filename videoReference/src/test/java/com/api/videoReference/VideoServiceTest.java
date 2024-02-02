package com.api.videoReference;

import com.api.videoReference.model.Movie;
import com.api.videoReference.model.Series;
import com.api.videoReference.model.Video;
import com.api.videoReference.repository.VideoRepository;
import com.api.videoReference.service.VideoService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class VideoServiceTest {
    private VideoRepository videoRepository;
    private VideoService videoService;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        videoRepository = new VideoRepository();
        videoService = new VideoService(videoRepository, objectMapper);
    }

    @Test
    void generateVideo() throws Exception {
        String movieJson = "{\"id\": \"1\", \"title\": \"matrix\", \"labels\": [\"sci-fi\", \"dystopia\"], \"director\": \"Steven Spielberg\", \"releaseDate\": \"1982-03-18T12:00:00Z\"}";
        String seriesJson = "{\"id\": \"2\", \"title\": \"Breaking Bad\", \"labels\": [\"chemistry\", \"drug\"], \"numberOfEpisodes\": \"62\"}";

        JsonNode movieJsonNode = objectMapper.readTree(movieJson);
        JsonNode seriesJsonNode = objectMapper.readTree(seriesJson);

        Video movieResult = videoService.generateVideo(movieJsonNode);
        Video seriesResult = videoService.generateVideo(seriesJsonNode);

        // Assert the response details
        assertNotNull(movieResult, "Generated video should not be null.");
        assertEquals("matrix", movieResult.getTitle(), "Title should match.");

        // Check if the Video is an instance of Movie
        assertTrue(movieResult instanceof Movie, "Result should be an instance of Movie.");

        // Assert the response details
        assertNotNull(seriesResult, "Generated video should not be null.");
        assertEquals("Breaking Bad", seriesResult.getTitle(), "Title should match.");

        // Check if the Video is an instance of Series
        assertTrue(seriesResult instanceof Series, "Result should be an instance of Series.");
    }

    @Test
    void testAddVideo() {
        Video video = new Movie("1", "matrix", Arrays.asList("sci-fi", "dystopia"), "Steven Spielberg", "1982-03-18T12:00:00Z");
        Video result = videoRepository.addVideo(video);

        // Assert the response details
        assertNotNull(result);
        assertEquals("matrix", result.getTitle());
        assertTrue(result.getLabels().contains("sci-fi"));
    }

    @Test
    void testGetVideo() {
        Video video = new Movie("2", "indiana jones", Arrays.asList("sci-fi", "dystopia"), "Steven Spielberg", "1982-03-18T12:00:00Z");

        videoRepository.addVideo(video);
        Video result = videoRepository.getVideo("2");

        // Assert the response details
        assertNotNull(result);
        assertEquals("indiana jones", result.getTitle());
        assertTrue(result.getLabels().contains("sci-fi"));

        // Test for a non-existing video
        assertNull(videoRepository.getVideo("nonExistingId"));
    }

    @Test
    void testSearchVideosByTitle() {
        Video video1 = new Movie("1", "indiana jones", Arrays.asList("sci-fi", "dystopia"), "Steven Spielberg", "1982-03-18T12:00:00Z");
        Video video2 = new Movie("2", "matrix", Arrays.asList("sci-fi", "dystopia"), "Steven Spielberg", "1982-03-18T12:00:00Z");
        Video video3 = new Movie("3", "indestructibles", Arrays.asList("sci-fi", "dystopia"), "Steven Spielberg", "1982-03-18T12:00:00Z");

        videoRepository.addVideo(video1);
        videoRepository.addVideo(video2);
        videoRepository.addVideo(video3);

        List<Video> result = videoService.searchVideosByTitle("ind");

        assertEquals(2, result.size(), "The size should be 2");
        assertTrue(result.contains(video1), "The result should contain the video 'indiana jones'");
        assertTrue(result.contains(video3), "The result should contain the video 'indestructibles'");
        assertFalse(result.contains(video2), "The result should not contain the video 'matrix'");
    }
}
