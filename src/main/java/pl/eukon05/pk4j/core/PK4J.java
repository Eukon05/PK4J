package pl.eukon05.pk4j.core;

import pl.eukon05.pk4j.core.impl.rest.RestPK4J;
import pl.eukon05.pk4j.core.impl.scraper.ScraperPK4J;
import pl.eukon05.pk4j.model.Announcement;
import pl.eukon05.pk4j.model.UserDetails;

import java.io.IOException;
import java.util.List;

public interface PK4J {
    List<Announcement> getAnnouncements(EHMSUser user) throws IOException, InterruptedException;

    UserDetails getUserDetails(EHMSUser user) throws IOException, InterruptedException;

    static PK4J getInstance() {
        return ScraperPK4J.getInstance();
    }

    static PK4J getInstance(boolean useRest) {
        return useRest ? RestPK4J.getInstance() : ScraperPK4J.getInstance();
    }
}
