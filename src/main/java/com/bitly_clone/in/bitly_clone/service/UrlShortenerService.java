package com.bitly_clone.in.bitly_clone.service;

import java.time.LocalDateTime;
import  java.util.*;
import com.bitly_clone.in.bitly_clone.entity.ShortenedUrl;
import com.bitly_clone.in.bitly_clone.exceptionhandling.InvalidUrlException;
import com.bitly_clone.in.bitly_clone.exceptionhandling.UrlNotFoundException;
import com.bitly_clone.in.bitly_clone.repository.ShortenedUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {
    private final ShortenedUrlRepository repository;

    public ShortenedUrl createShortUrl(String originalUrl) {
        String shortCode = generateShortCode();
        ShortenedUrl url = new ShortenedUrl(0L, shortCode, originalUrl, 0L, LocalDateTime.now(), LocalDateTime.now());
        return repository.save(url);
    }

    public Optional<ShortenedUrl> getOriginalUrl(String shortUrl) {
        return Optional.ofNullable(repository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException("Short URL not found: " + shortUrl)));
    }

    public ShortenedUrl updateShortUrl(String shortCode, String newUrl) {
        ShortenedUrl existingUrl = repository.findByShortUrl(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("Short URL not found: " + shortCode));

        if (newUrl == null || newUrl.isEmpty()) {
            throw new InvalidUrlException("URL cannot be empty.");
        }

        existingUrl.setOriginalUrl(newUrl);
        existingUrl.setLastAccessedAt(LocalDateTime.now());
        return repository.save(existingUrl);
    }


    public void deleteShortUrl(String shortCode) {
        repository.findByShortUrl(shortCode).ifPresent(repository::delete);
    }

    public Optional<Long> getUrlStats(String shortCode) {
        return repository.findByShortUrl(shortCode).map(ShortenedUrl::getAccessCount);
    }

    private String generateShortCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
}
