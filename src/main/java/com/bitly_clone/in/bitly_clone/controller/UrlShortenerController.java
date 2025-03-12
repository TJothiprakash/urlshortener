package com.bitly_clone.in.bitly_clone.controller;

import com.bitly_clone.in.bitly_clone.entity.ShortenedUrl;
import com.bitly_clone.in.bitly_clone.exceptionhandling.UrlNotFoundException;
import com.bitly_clone.in.bitly_clone.service.UrlShortenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/shorten")
@RequiredArgsConstructor
public class UrlShortenerController {
    private final UrlShortenerService service;

    @PostMapping
    public ResponseEntity<ShortenedUrl> createShortUrl(@RequestBody Map<String, String> request) {
        log.info("Received request to shorten URL: {}", request.get("url"));

        if (!request.containsKey("url") || request.get("url").isEmpty()) {
            log.warn("Invalid request: URL is missing or empty.");
            return ResponseEntity.badRequest().build();
        }

        ShortenedUrl url = service.createShortUrl(request.get("url"));
        log.info("Short URL created: {}", url.getShortUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(url);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Map<String, String>> getOriginalUrl(@PathVariable String shortUrl) {
        log.info("Retrieving original URL for shortCode: {}", shortUrl);
        return service.getOriginalUrl(shortUrl)
                .map(url -> {
                    log.info("Original URL found: {}", url.getOriginalUrl());
                    return ResponseEntity.ok(Map.of("url", url.getOriginalUrl()));
                })
                .orElseGet(() -> {
                    log.warn("Short URL not found: {}", shortUrl);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                });
    }

    @PutMapping("/{shortCode}")
    public ResponseEntity<ShortenedUrl> updateShortUrl(@PathVariable String shortCode, @RequestBody Map<String, String> request) {
        log.info("Updating short URL: {}", shortCode);

        if (!request.containsKey("url") || request.get("url").isEmpty()) {
            log.warn("Invalid update request: URL is missing or empty.");
            return ResponseEntity.badRequest().build();
        }

        try {
            ShortenedUrl updatedUrl = service.updateShortUrl(shortCode, request.get("url"));
            log.info("Short URL updated successfully: {}", updatedUrl);
            return ResponseEntity.ok(updatedUrl);
        } catch (UrlNotFoundException e) {
            log.error("Short URL not found: {}", shortCode);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{shortCode}")
    public ResponseEntity<Void> deleteShortUrl(@PathVariable String shortCode) {
        log.info("Deleting short URL: {}", shortCode);
        service.deleteShortUrl(shortCode);
        log.info("Short URL deleted successfully: {}", shortCode);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{shortCode}/stats")
    public ResponseEntity<Map<String, Long>> getUrlStats(@PathVariable String shortCode) {
        log.info("Fetching stats for short URL: {}", shortCode);
        return service.getUrlStats(shortCode)
                .map(count -> {
                    log.info("Stats retrieved: shortCode={} accessCount={}", shortCode, count);
                    return ResponseEntity.ok(Map.of("accessCount", count));
                })
                .orElseGet(() -> {
                    log.warn("Stats not found for shortCode: {}", shortCode);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                });
    }
}
