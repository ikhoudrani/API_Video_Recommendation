package com.api.videoReference;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Step2Test {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getVideoGET_Valid() throws Exception {
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
        restTemplate.postForEntity(createURLWithPort("/videos"), request, String.class);

        // GET request by id to check if the video has been added
        ResponseEntity<String> response = restTemplate.getForEntity(createURLWithPort("/videos/1"), String.class);

        // Assert the response status code
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK.");
        assertNotNull(response.getBody(), "Response body should not be null.");

        // Deserialize response to Video or Map object
        Map<String, Object> video = objectMapper.readValue(response.getBody(), Map.class);

        // Assert the response details
        assertEquals("matrix", video.get("title"), "Title should match 'matrix'.");
        assertEquals("Lana Wachowski", video.get("director"), "Director should match 'Lana Wachowski'.");
    }

    @Test
    public void getVideoGET_Invalid() throws Exception {
        // GET request by an invalid id
        ResponseEntity<String> response = restTemplate.getForEntity(createURLWithPort("/videos/999"), String.class);

        // Assert the response details
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Response status should be NOT FOUND.");
        assertNull(response.getBody(), "Response body should be null.");
    }
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }



}
