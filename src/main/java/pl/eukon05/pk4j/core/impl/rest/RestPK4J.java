package pl.eukon05.pk4j.core.impl.rest;

import com.google.gson.JsonObject;
import pl.eukon05.pk4j.core.EHMSUser;
import pl.eukon05.pk4j.core.EHMSUserSession;
import pl.eukon05.pk4j.core.PK4J;
import pl.eukon05.pk4j.exception.EHMSExpiredTokenException;
import pl.eukon05.pk4j.model.Announcement;
import pl.eukon05.pk4j.model.UserDetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class RestPK4J extends PK4J {
    private static final RestPK4J INSTANCE = new RestPK4J(new RestEHMSWebClient(), new JsonToModelMapper());
    private final JsonToModelMapper mapper;

    private RestPK4J(RestEHMSWebClient ehmsWebClient, JsonToModelMapper mapper) {
        super(ehmsWebClient);
        this.mapper = mapper;
    }

    @Override
    public List<Announcement> getAnnouncements(EHMSUser user) throws IOException, InterruptedException {
        EHMSUserSession session = getSession(user);

        try {
            JsonObject shortAnnouncements = client.getRequest(session, RestEHMSUrl.ALL_ANNOUNCEMENTS);

            List<Long> annIds = shortAnnouncements.getAsJsonArray("data")
                    .asList()
                    .stream()
                    .map(el -> el.getAsJsonObject().get("id").getAsLong())
                    .toList();

            List<Announcement> result = new ArrayList<>();
            for (long id : annIds) {
                JsonObject announcement = client.getRequest(session, RestEHMSUrl.ANNOUNCEMENT, id);
                result.add(mapper.announcementFromJson(announcement.getAsJsonObject("data"), id));
            }

            return result;
        } catch (EHMSExpiredTokenException e) {
            sessionCache.remove(user.getUsername());
            return getAnnouncements(user);
        }
    }

    @Override
    public UserDetails getUserDetails(EHMSUser user) throws IOException, InterruptedException {
        EHMSUserSession session = getSession(user);

        try {
            JsonObject details = client.getRequest(session, RestEHMSUrl.USER_DETAILS);
            return mapper.detailsFromJson(details.getAsJsonArray("data").get(0).getAsJsonObject());
        } catch (EHMSExpiredTokenException e) {
            sessionCache.remove(user.getUsername());
            return getUserDetails(user);
        }
    }

    public static RestPK4J getInstance() {
        return INSTANCE;
    }
}
