package pl.eukon05.pk4j.core.impl.rest;

import com.google.gson.JsonObject;
import pl.eukon05.pk4j.core.EHMSUser;
import pl.eukon05.pk4j.core.PK4J;
import pl.eukon05.pk4j.model.Announcement;
import pl.eukon05.pk4j.model.UserDetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RestPK4J implements PK4J {
    private static final RestPK4J INSTANCE = new RestPK4J(new RestEHMSWebClient(), new JsonToModelMapper());
    private final JsonToModelMapper mapper;
    private final RestEHMSWebClient client;

    private RestPK4J(RestEHMSWebClient ehmsWebClient, JsonToModelMapper mapper) {
        this.client = ehmsWebClient;
        this.mapper = mapper;
    }

    @Override
    public List<Announcement> getAnnouncements(EHMSUser user) throws IOException, InterruptedException {
        JsonObject shortAnnouncements = client.getRequest(user, RestEHMSUrl.ALL_ANNOUNCEMENTS);

        List<Long> annIds = shortAnnouncements.getAsJsonArray("data")
                .asList()
                .stream()
                .map(el -> el.getAsJsonObject().get("id").getAsLong())
                .toList();

        List<Announcement> result = new ArrayList<>();
        for (long id : annIds) {
            JsonObject announcement = client.getRequest(user, RestEHMSUrl.ANNOUNCEMENT, id);
            result.add(mapper.announcementFromJson(announcement.getAsJsonObject("data"), id));
        }

        return result;
    }

    @Override
    public UserDetails getUserDetails(EHMSUser user) throws IOException, InterruptedException {
        JsonObject details = client.getRequest(user, RestEHMSUrl.USER_DETAILS);
        return mapper.detailsFromJson(details.getAsJsonArray("data").get(0).getAsJsonObject());
    }

    public static RestPK4J getInstance() {
        return INSTANCE;
    }
}
