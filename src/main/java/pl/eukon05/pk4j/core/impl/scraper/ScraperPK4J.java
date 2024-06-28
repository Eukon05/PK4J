package pl.eukon05.pk4j.core.impl.scraper;

import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import pl.eukon05.pk4j.core.EHMSUser;
import pl.eukon05.pk4j.core.EHMSUserSession;
import pl.eukon05.pk4j.core.PK4J;
import pl.eukon05.pk4j.exception.EHMSExpiredTokenException;
import pl.eukon05.pk4j.model.Announcement;
import pl.eukon05.pk4j.model.UserDetails;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class ScraperPK4J extends PK4J {
    private static final ScraperPK4J INSTANCE = new ScraperPK4J(new ScraperEHMSWebClient(), new ElementToModelMapper());
    private final ElementToModelMapper mapper;

    private ScraperPK4J(ScraperEHMSWebClient ehmsWebClient, ElementToModelMapper mapper) {
        super(ehmsWebClient);
        this.mapper = mapper;
    }

    public static ScraperPK4J getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Announcement> getAnnouncements(EHMSUser user) throws IOException, EHMSExpiredTokenException, InterruptedException {
        EHMSUserSession session = getSession(user);

        try {
            JsonObject jsDoc = client.getRequest(session, ScraperEHMSUrl.BASE);
            Document doc = Jsoup.parse(jsDoc.get("Document").getAsString());
            Elements announcements = doc.selectXpath("//*[@id=\"content\"]/div/div[2]/div/div[2]/table/tbody").select("tr");

            return announcements.stream().map(mapper::announcementFromElement).sorted(Comparator.comparing(Announcement::lastModified).reversed()).toList();
        } catch (EHMSExpiredTokenException e) {
            sessionCache.remove(user.getUsername());
            return getAnnouncements(user);
        }
    }

    @Override
    public UserDetails getUserDetails(EHMSUser user) throws IOException, EHMSExpiredTokenException, InterruptedException {
        EHMSUserSession session = getSession(user);

        try {
            JsonObject jsDoc = client.getRequest(session, ScraperEHMSUrl.USER_DETAILS);
            Document doc = Jsoup.parse(jsDoc.get("Document").getAsString());

            return mapper.userDetailsFromElement(Objects.requireNonNull(doc.select("#content > div > div.p-3").first()));
        } catch (EHMSExpiredTokenException e) {
            sessionCache.remove(user.getUsername());
            return getUserDetails(user);
        }
    }
}
