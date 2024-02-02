package com.api.videoReference;

import com.api.videoReference.model.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Step1Test {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addVideoPOST_Valid() throws Exception {
        // Build the video data
        Map<String, Object> videoData = new HashMap<>();
        videoData.put("id", "1");
        videoData.put("title", "matrix");
        videoData.put("labels", Arrays.asList("sci-fi", "dystopia"));
        videoData.put("director", "Lana Wachowski");
        videoData.put("releaseDate", "1999-03-31T12:00:00Z");

        // Convert video data to JSON string
        String videoJson = objectMapper.writeValueAsString(videoData);

        // Create request with JSON content type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(videoJson, headers);

        // Send POST request to add a new video
        ResponseEntity<String> response = restTemplate.postForEntity(createURLWithPort("/videos"), request, String.class);

        // Assert the response status code
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Response status should be CREATED.");

        // Deserialize response to Movie object
        Movie responseMovie = objectMapper.readValue(response.getBody(), Movie.class);

        // Assert the response details
        assertNotNull(responseMovie, "Response movie should not be null.");
        assertEquals("matrix", responseMovie.getTitle(), "Movie title should match.");
        assertEquals("Lana Wachowski", responseMovie.getDirector(), "Movie director should match.");
        assertEquals("1999-03-31T12:00:00Z", responseMovie.getReleaseDate(), "Movie release date should match.");
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
