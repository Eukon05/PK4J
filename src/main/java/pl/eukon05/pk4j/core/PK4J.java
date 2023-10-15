package pl.eukon05.pk4j.core;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import pl.eukon05.pk4j.model.Announcement;
import pl.eukon05.pk4j.model.UserDetails;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class PK4J {
    private PK4J() {
    }

    public static List<Announcement> getAnnouncements(EHMSUser user) throws IOException {
        Document doc = EHMSWebClient.getRequest(EHMSUrl.BASE, user);
        Elements announcements = doc.selectXpath("//*[@id=\"content\"]/div/div[2]/div/div[2]/table/tbody").select("tr");

        return announcements.stream().map(Announcement::fromElement).sorted(Comparator.comparing(Announcement::lastModified).reversed()).toList();
    }

    public static UserDetails getUserDetails(EHMSUser user) throws IOException {
        Document doc = EHMSWebClient.getRequest(EHMSUrl.USER_DETAILS, user);
        return UserDetails.fromElement(Objects.requireNonNull(doc.select("#content > div > div.p-3").first()));
    }
}
