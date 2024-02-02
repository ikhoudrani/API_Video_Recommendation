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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Step3Test {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void searchVideosByTitle() throws Exception {
        // Send POST request to add a new videos
        addTestVideo("1", "indiana jones", "Steven Spielberg", "1982-03-18T12:00:00Z", Arrays.asList("sci-fi", "dystopia"));
        addTestVideo("2", "matrix", "Lana Wachowski", "1999-03-31T12:00:00Z", Arrays.asList("sci-fi", "action"));
        addTestVideo("3", "indestructibles", "Brad Bird", "2004-11-05T12:00:00Z", Arrays.asList("animation", "family"));

        // Search for videos with the title containing "ind"
        ResponseEntity<List> response = restTemplate.getForEntity(createURLWithPort("/videos/search?title=ind"), List.class);

        // Assert the response status code and check the size of the response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK.");
        assertNotNull(response.getBody(), "Response body should not be null.");
        assertTrue(response.getBody().size() == 2, "Response should contain at least two videos.");

        List<Map<String, Object>> videos = response.getBody();

        // Assert the response details
        assertTrue(
                videos.stream().anyMatch(video -> video.get("title").equals("indiana jones")),
                "Response should include 'indiana jones'."
        );
        assertTrue(
                videos.stream().anyMatch(video -> video.get("title").equals("indestructibles")),
                "Response should include 'indestructibles'."
        );
        assertFalse(
                videos.stream().anyMatch(video -> video.get("title").equals("matrix")),
                "Response should not include 'matrix'."
        );
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private void addTestVideo(String id, String title, String director, String releaseDate, List<String> labels) throws Exception {
        Map<String, Object> videoData = new HashMap<>();
        videoData.put("id", id);
        videoData.put("title", title);
        videoData.put("labels", labels);
        videoData.put("director", director);
        videoData.put("releaseDate", releaseDate);

        String videoJson = objectMapper.writeValueAsString(videoData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(videoJson, headers);

        restTemplate.postForEntity(createURLWithPort("/videos"), request, String.class);
    }
}
