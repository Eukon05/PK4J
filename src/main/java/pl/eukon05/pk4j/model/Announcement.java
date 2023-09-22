package pl.eukon05.pk4j.model;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Announcement(long id, String title, String content, String priority, String author,
                           LocalDateTime lastModified) {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public static Announcement fromElement(Element element) {
        long id;
        String title;
        String content;
        String priority;
        String author;
        LocalDateTime lastModified;

        id = Long.parseLong(element.attr("onclick").replaceAll("\\D", ""));
        Elements rows = element.select("td");
        title = rows.get(1).text();
        content = rows.get(2).select("span").get(0).attr("title");
        priority = rows.get(4).text();
        author = rows.get(5).text();
        lastModified = LocalDateTime.parse(rows.get(6).text(), DATE_TIME_FORMATTER);

        return new Announcement(id, title, content, priority, author, lastModified);
    }

}
