package pl.eukon05.pk4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Announcement {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private final String title;
    private final String content;
    private final String priority;
    private final String author;
    private final LocalDateTime lastModified;

    public Announcement(String title, String content, String priority, String author, String lastModified) {
        this.title = title;
        this.content = content;
        this.priority = priority;
        this.author = author;
        this.lastModified = LocalDateTime.parse(lastModified, formatter);
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getPriority() {
        return priority;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    @Override
    public String toString() {
        return "Announcement{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", priority='" + priority + '\'' +
                ", author='" + author + '\'' +
                ", lastModified=" + lastModified +
                '}';
    }
}
