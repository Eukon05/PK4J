package pl.eukon05.pk4j.core.impl.scraper;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import pl.eukon05.pk4j.core.EHMSUser;
import pl.eukon05.pk4j.core.PK4J;
import pl.eukon05.pk4j.model.Announcement;
import pl.eukon05.pk4j.model.UserDetails;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class ScraperPK4J implements PK4J {
    private static final ScraperPK4J INSTANCE = new ScraperPK4J(new ScraperEHMSWebClient(), new ElementToModelMapper());
    private final ElementToModelMapper mapper;
    private final ScraperEHMSWebClient client;

    private ScraperPK4J(ScraperEHMSWebClient ehmsWebClient, ElementToModelMapper mapper) {
        this.client = ehmsWebClient;
        this.mapper = mapper;
    }

    public static ScraperPK4J getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Announcement> getAnnouncements(EHMSUser user) throws IOException {
        Document doc = client.getRequest(ScraperEHMSUrl.BASE, user);
        Elements announcements = doc.selectXpath("//*[@id=\"content\"]/div/div[2]/div/div[2]/table/tbody").select("tr");

        return announcements.stream().map(mapper::announcementFromElement).sorted(Comparator.comparing(Announcement::lastModified).reversed()).toList();
    }

    @Override
    public UserDetails getUserDetails(EHMSUser user) throws IOException {
        Document doc = client.getRequest(ScraperEHMSUrl.USER_DETAILS, user);
        return mapper.userDetailsFromElement(Objects.requireNonNull(doc.select("#content > div > div.p-3").first()));
    }
}
