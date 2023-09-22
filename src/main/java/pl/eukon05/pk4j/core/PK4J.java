package pl.eukon05.pk4j.core;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.eukon05.pk4j.model.Announcement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class PK4J {
    private PK4J() {
    }

    public static List<Announcement> getAnnouncements(EHMSUser user) throws IOException {
        Document doc = EHMSWebClient.getRequest(EHMSUrl.BASE, user);
        Elements announcements = doc.selectXpath("//*[@id=\"content\"]/div/div[2]/div/div[2]/table/tbody").select("tr");

        List<Announcement> result = new ArrayList<>();
        String title;
        String content;
        String priority;
        String author;
        String lastModified;
        Announcement announcement;

        for (Element e : announcements) {
            Elements rows = e.select("td");
            title = rows.get(1).text();
            content = rows.get(2).select("span").get(0).attr("title");
            priority = rows.get(4).text();
            author = rows.get(5).text();
            lastModified = rows.get(6).text();

            announcement = new Announcement(title, content, priority, author, lastModified);
            result.add(announcement);
        }

        return result;
    }
}
