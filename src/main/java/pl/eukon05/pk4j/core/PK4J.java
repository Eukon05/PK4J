package pl.eukon05.pk4j.core;

import pl.eukon05.pk4j.core.impl.rest.RestPK4J;
import pl.eukon05.pk4j.core.impl.scraper.ScraperPK4J;
import pl.eukon05.pk4j.exception.InvalidCredentialsException;
import pl.eukon05.pk4j.model.Announcement;
import pl.eukon05.pk4j.model.UserDetails;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PK4J {
    protected final Map<String, EHMSUserSession> sessionCache = new HashMap<>();
    protected final EHMSWebClient client;

    protected PK4J(EHMSWebClient client) {
        this.client = client;
    }

    protected final EHMSUserSession getSession(EHMSUser user) throws InvalidCredentialsException, IOException, InterruptedException {
        if (!sessionCache.containsKey(user.getUsername()))
            sessionCache.put(user.getUsername(), client.login(user));

        return sessionCache.get(user.getUsername());
    }

    public abstract List<Announcement> getAnnouncements(EHMSUser user) throws IOException, InterruptedException, InvalidCredentialsException;

    public abstract UserDetails getUserDetails(EHMSUser user) throws IOException, InterruptedException, InvalidCredentialsException;

    public static PK4J getInstance() {
        return ScraperPK4J.getInstance();
    }

    public static PK4J getInstance(boolean useRest) {
        return useRest ? RestPK4J.getInstance() : ScraperPK4J.getInstance();
    }
}
