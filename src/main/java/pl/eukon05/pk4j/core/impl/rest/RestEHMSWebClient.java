package pl.eukon05.pk4j.core.impl.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.eukon05.pk4j.core.EHMSUser;
import pl.eukon05.pk4j.exception.EHMSException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class RestEHMSWebClient {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private final Logger logger = LoggerFactory.getLogger(RestEHMSWebClient.class);


    JsonObject getRequest(EHMSUser user, RestEHMSUrl url) throws IOException, InterruptedException {
        return getRequest(user, url, -1);
    }

    JsonObject getRequest(EHMSUser user, RestEHMSUrl url, long resourceID) throws IOException, InterruptedException {
        if (user.getSessionToken().isEmpty()) {
            logger.debug("User {} didn't have a session token attached, trying to log in...", user.getUsername());
            login(user);
            return getRequest(user, url);
        }

        if (user.getStudyID().isEmpty() || user.getUserID().isEmpty()) {
            logger.warn("""
                    User {} was previously logged in via a different PK4J implementation.
                    Please, DO NOT reuse the same user object across different implementations of PK4J, to avoid unnecessary logins.
                    Trying to log in...
                    """, user.getUsername());
            login(user);
            return getRequest(user, url);
        }

        logger.debug("Trying to retrieve data from {} for user {} ...", url.value(), user.getUsername());

        StringBuilder urlBuilder = new StringBuilder(url.value());

        if (resourceID != -1) {
            urlBuilder.append("/").append(resourceID);
        }

        urlBuilder.append("?user_id=").append(user.getUserID().get());
        urlBuilder.append("&study_id=").append(user.getStudyID().get());

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlBuilder.toString())).header("Content-Type", "application/json").header("Authorization", String.join("", "Bearer ", user.getSessionToken().get())).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 401) {
            logger.debug("User {}'s session token was inactive, trying to log back in...", user.getUsername());
            login(user);
            return getRequest(user, url);
        }

        if (response.statusCode() != 200) throw new EHMSException(response.statusCode());

        logger.debug("Successfully retrieved data from {}, for user {}", url.value(), user.getUsername());
        return gson.fromJson(response.body(), JsonObject.class);
    }


    private void login(EHMSUser user) throws IOException, InterruptedException {
        final String loginBody = "{\"username\":\"%s\",\"password\":\"%s\"}";

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(RestEHMSUrl.AUTHENTICATE.value())).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(String.format(loginBody, user.getUsername(), user.getPassword()))).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 403)
            throw new IllegalArgumentException(String.format("Authentication failed for user %s, are the login details correct?", user.getUsername()));

        if (response.statusCode() != 200) throw new EHMSException(response.statusCode());

        logger.debug("Successfully logged in as user {}", user.getUsername());

        JsonObject responseBody = gson.fromJson(response.body(), JsonObject.class);
        JsonObject userData = responseBody.getAsJsonObject("user");
        String studyID = responseBody.getAsJsonObject("ouStudyCourses").getAsJsonObject("ouStudyCoursesRow").get("id").getAsString();

        user.setSessionToken(userData.get("token").getAsString());
        user.setUserID(userData.get("id").getAsString());
        user.setStudyID(studyID);
    }
}
