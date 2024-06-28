package pl.eukon05.pk4j.core.impl.scraper;

import com.google.gson.JsonObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.eukon05.pk4j.core.EHMSUrlEnum;
import pl.eukon05.pk4j.core.EHMSUser;
import pl.eukon05.pk4j.core.EHMSUserSession;
import pl.eukon05.pk4j.core.EHMSWebClient;
import pl.eukon05.pk4j.exception.EHMSException;
import pl.eukon05.pk4j.exception.EHMSExpiredTokenException;
import pl.eukon05.pk4j.exception.InvalidCredentialsException;
import pl.eukon05.pk4j.exception.RateLimitExceededException;

import java.io.IOException;
import java.util.Map;

final class ScraperEHMSWebClient implements EHMSWebClient {
    private static final String NOT_LOGGED_IN = "Logowanie do systemu";
    private static final String RATE_LIMITED = "Przepisz kod z obrazka";
    private static final String PHP_SESS_ID = "dsysPHPSESSID";

    private final Logger logger = LoggerFactory.getLogger(ScraperEHMSWebClient.class);

    public JsonObject getRequest(EHMSUserSession session, EHMSUrlEnum url) throws IOException {
        String value;
        if (url instanceof ScraperEHMSUrl scraperEHMSUrl)
            value = scraperEHMSUrl.value();
        else {
            logger.error("Using URLs from other PK4J implementations is unsupported!");
            throw new IllegalArgumentException("url must be an instance of ScraperEHMSUrl");
        }

        if(session instanceof ScraperUserSession) {
            logger.debug("Trying to retrieve data from {}", value);
            Connection.Response response = Jsoup.connect(value).cookie(PHP_SESS_ID, session.getSessionToken()).execute();

            checkStatusCode(response);

            Document responseBody = response.parse();

            if (!responseBody.getElementsContainingText(NOT_LOGGED_IN).isEmpty()) {
                logger.debug("Unable to retrieve data from {}, session expired", value);
                throw new EHMSExpiredTokenException();
            }

            logger.debug("Successfully retrieved data from {}", value);

            JsonObject returnVal = new JsonObject();
            returnVal.addProperty("Document", responseBody.html());
            return returnVal;
        }
        else {
            logger.error("Using a session object from other PK4J implementations is unsupported!");
            throw new IllegalArgumentException("session must be an instance of ScraperUserSession");
        }
    }

    // Not used in the current implementation
    @Override
    public JsonObject getRequest(EHMSUserSession session, EHMSUrlEnum url, long resourceID) throws IOException {
        return getRequest(session, url);
    }

    public EHMSUserSession login(EHMSUser user) throws IOException {
        logger.debug("Trying to retrieve a login form from EHMS");
        Connection.Response response = Jsoup.connect(ScraperEHMSUrl.BASE.value()).execute();

        checkStatusCode(response);

        logger.debug("Successfully retrieved the login form");
        Document responseBody = response.parse();

        Map<String, String> cookies = response.cookies();
        Map<String, String> formData = prepareLoginFormData(responseBody, user.getUsername(), user.getPassword());

        logger.debug("Trying to log in as user \"{}\"", user.getUsername());

        Connection.Response loginResponse = Jsoup.connect(ScraperEHMSUrl.BASE.value()).data(formData).cookies(cookies).method(Connection.Method.POST).execute();
        checkStatusCode(loginResponse);

        Document loginResponseBody = loginResponse.parse();

        if (loginResponseBody.getElementsContainingText(NOT_LOGGED_IN).isEmpty()) {
            logger.debug("Successfully logged in as user \"{}\"", user.getUsername());
            return new ScraperUserSession(cookies.get(PHP_SESS_ID));
        } else {
            if (!loginResponseBody.getElementsContainingText(RATE_LIMITED).isEmpty()) {
                logger.warn("User \"{}\" got rate-limited! Please wait or solve the captcha on a different device before trying to log in again!", user.getUsername());
                throw new RateLimitExceededException(user.getUsername());
            } else {
                logger.warn("Authentication for user \"{}\" failed, are the login details correct?", user.getUsername());
                throw new InvalidCredentialsException(user.getUsername());
            }
        }
    }

    private Map<String, String> prepareLoginFormData(Document document, String login, String password) {
        Elements form = document.getElementsByClass("form-control");

        String loginFieldName = form.get(0).attr("name");
        String passwordFieldName = form.get(1).attr("name");
        String counterFieldName = document.select("input[type=hidden]").get(1).val();

        return Map.of(loginFieldName, login, passwordFieldName, password, "log_form", "yes", "counter", counterFieldName);
    }

    private void checkStatusCode(Connection.Response response) {
        if (response.statusCode() != 200) {
            logger.error("EHMS returned an unexpected status code: {}", response.statusCode());
            throw new EHMSException(response.statusCode());
        }
    }
}
