package pl.eukon05.pk4j.model;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Announcement(long id, String title, String content, String priority, String author,
                           LocalDateTime lastModified) {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public static Announcement fromElement(Element element) {
        long id = Long.parseLong(element.attr("onclick").replaceAll("\\D", ""));
        Elements rows = element.select("td");
        String title = rows.get(1).text();

        Element contentNode = rows.get(2);
        Elements contentSpanSelect = contentNode.select("span");

        String content = contentSpanSelect.isEmpty() ? contentNode.text() : contentSpanSelect.get(0).attr("title");

        String priority = rows.get(4).text();
        String author = rows.get(5).text();
        LocalDateTime lastModified = LocalDateTime.parse(rows.get(6).text(), DATE_TIME_FORMATTER);

        return new Announcement(id, title, content, priority, author, lastModified);
    }

}
