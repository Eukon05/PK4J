package pl.eukon05.pk4j.core.impl.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.eukon05.pk4j.core.EHMSUrlEnum;
import pl.eukon05.pk4j.core.EHMSUser;
import pl.eukon05.pk4j.core.EHMSUserSession;
import pl.eukon05.pk4j.core.EHMSWebClient;
import pl.eukon05.pk4j.exception.EHMSException;
import pl.eukon05.pk4j.exception.EHMSExpiredTokenException;
import pl.eukon05.pk4j.exception.InvalidCredentialsException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

final class RestEHMSWebClient implements EHMSWebClient {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private final Logger logger = LoggerFactory.getLogger(RestEHMSWebClient.class);

    public JsonObject getRequest(EHMSUserSession session, EHMSUrlEnum url) throws IOException, InterruptedException {
        return getRequest(session, url, -1);
    }

    public JsonObject getRequest(EHMSUserSession session, EHMSUrlEnum url, long resourceID) throws IOException, InterruptedException {
        String value;
        if (url instanceof RestEHMSUrl restEHMSUrl)
            value = restEHMSUrl.value();
        else {
            logger.error("Using URLs from other PK4J implementations is unsupported!");
            throw new IllegalArgumentException("url must be an instance of RestEHMSUrl");
        }

        if (session instanceof RestUserSession restUserSession) {
            StringBuilder urlBuilder = new StringBuilder(value);

            if (resourceID != -1) {
                urlBuilder.append("/").append(resourceID);
            }

            urlBuilder.append("?user_id=").append(restUserSession.getUserID());
            urlBuilder.append("&study_id=").append(restUserSession.getStudyID());

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlBuilder.toString())).header("Content-Type", "application/json").header("Authorization", String.join("", "Bearer ", restUserSession.getSessionToken())).GET().build();

            logger.debug("Trying to retrieve data from {}", urlBuilder);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 401) {
                logger.debug("Unable to retrieve data from {}, session expired", urlBuilder);
                throw new EHMSExpiredTokenException();
            }

            if (response.statusCode() != 200) throw new EHMSException(response.statusCode());

            logger.debug("Successfully retrieved data from {}", urlBuilder);
            return gson.fromJson(response.body(), JsonObject.class);
        } else {
            logger.error("Using a session object from other PK4J implementations is unsupported!");
            throw new IllegalArgumentException("session must be an instance of RestUserSession");
        }
    }

    public EHMSUserSession login(EHMSUser user) throws IOException, InterruptedException {
        String loginBody = "{\"username\":\"%s\",\"password\":\"%s\"}";
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(RestEHMSUrl.AUTHENTICATE.value())).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(String.format(loginBody, user.getUsername(), user.getPassword()))).build();

        logger.debug("Trying to log in as user \"{}\"", user.getUsername());
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 403) {
            logger.warn("Authentication for user \"{}\" failed, are the login details correct?", user.getUsername());
            throw new InvalidCredentialsException(user.getUsername());
        }

        if (response.statusCode() != 200) throw new EHMSException(response.statusCode());

        logger.debug("Successfully logged in as user \"{}\"", user.getUsername());

        JsonObject responseBody = gson.fromJson(response.body(), JsonObject.class);
        JsonObject userData = responseBody.getAsJsonObject("user");
        String token = userData.get("token").getAsString();
        String userID = userData.get("id").getAsString();
        String studyID = responseBody.getAsJsonObject("ouStudyCourses").getAsJsonObject("ouStudyCoursesRow").get("id").getAsString();

        return new RestUserSession(token, userID, studyID);
    }
}
