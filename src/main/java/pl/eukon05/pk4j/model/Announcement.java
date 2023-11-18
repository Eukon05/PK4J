package pl.eukon05.pk4j.model;

import java.time.LocalDateTime;

public record Announcement(long id, String title, String content, String priority, String author,
                           LocalDateTime lastModified) {
}
