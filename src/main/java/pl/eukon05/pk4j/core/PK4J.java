package pl.eukon05.pk4j.core;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import pl.eukon05.pk4j.model.Announcement;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public final class PK4J {
    private PK4J() {
    }

    public static List<Announcement> getAnnouncements(EHMSUser user) throws IOException {
        Document doc = EHMSWebClient.getRequest(EHMSUrl.BASE, user);
        Elements announcements = doc.selectXpath("//*[@id=\"content\"]/div/div[2]/div/div[2]/table/tbody").select("tr");

        return announcements.stream().map(Announcement::fromElement).sorted(Comparator.comparing(Announcement::lastModified).reversed()).toList();
    }
}
