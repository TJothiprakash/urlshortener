package com.bitly_clone.in.bitly_clone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Table(name="bitly_clone")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShortenedUrl
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String originalUrl;

    @Column(nullable = false, unique = true)
    private String shortUrl;

    private long  accessCount = 0l;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime lastAccessedAt = LocalDateTime.now();

}
