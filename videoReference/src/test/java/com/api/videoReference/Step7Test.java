package com.api.videoReference;

import com.api.videoReference.model.Movie;
import com.api.videoReference.model.Video;
import com.api.videoReference.repository.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class Step7Test {

    @LocalServerPort
    private int port;

    @Autowired
    private VideoRepository videoRepository;
    private RestTemplate restTemplate = new RestTemplate();

    @BeforeEach
    public void setUp() {
        // Ajoutez des vidéos pour les tests
        Video video1 = new Movie("1", "matrix", Arrays.asList("sci-fi", "action"), "The Wachowskis", "1999-03-31T12:00:00Z");
        Video video2 = new Movie("2", "inception", Arrays.asList("sci-fi", "mind-bending"), "Christopher Nolan", "2010-07-16T12:00:00Z");

        videoRepository.addVideo(video1);
        videoRepository.addVideo(video2);
    }

    @Test
    public void testFindSimilarVideos() {
        // Prepare a video with similar labels
        Video videoToTest = new Movie("3", "the matrix reloaded", Arrays.asList("sci-fi", "action", "matrix"), "The Wachowskis", "2003-05-07T12:00:00Z");
        videoRepository.addVideo(videoToTest);

        // Perform a GET request to find similar videos
        String videoIdToTest = "3";

        // Minimum 2 labels in common
        int minLabels = 2;

        ResponseEntity<List> response = restTemplate.getForEntity(
                createURLWithPort("/videos/{videoId}/similar?minLabels={minLabels}"),
                List.class, videoIdToTest, minLabels);

        // Assert the response status code
        assertEquals(200, response.getStatusCodeValue());

        // Check that the list of similar videos is not empty
        List<Map<String, Object>> similarVideos = response.getBody();
        assertFalse(similarVideos.isEmpty(), "The list of similar videos should not be empty");

        // Check that similar videos include the tested video
        assertTrue(similarVideos.stream().anyMatch(video -> "matrix".equals(video.get("title"))), "Similar videos should include 'matrix'");
    }
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
