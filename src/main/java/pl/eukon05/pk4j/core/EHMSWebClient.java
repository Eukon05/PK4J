package pl.eukon05.pk4j.core;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.eukon05.pk4j.exception.EHMSException;
import pl.eukon05.pk4j.exception.RateLimitExceededException;

import java.io.IOException;
import java.util.Map;

class EHMSWebClient {
    private EHMSWebClient() {
    }

    private static final String NOT_LOGGED_IN = "Logowanie do systemu";
    private static final String RATE_LIMITED = "Przepisz kod z obrazka";
    private static final Logger LOGGER = LoggerFactory.getLogger(EHMSWebClient.class);

    static Document getRequest(EHMSUrl url, EHMSUser user) throws IOException {
        LOGGER.debug("Trying to retrieve data from {} for user {} ...", url.value(), user.login);
        Connection.Response response = Jsoup.connect(url.value()).cookies(user.cookies).execute();

        checkStatusCode(response);

        Document responseBody = response.parse();

        if (!responseBody.getElementsContainingText(NOT_LOGGED_IN).isEmpty()) {
            LOGGER.debug("User {} didn't have an active session, trying to log in...", user.login);
            login(user);
            return getRequest(url, user);
        }

        LOGGER.debug("Successfully retrieved data from {}, for user {}", url.value(), user.login);
        return responseBody;
    }

    private static void login(EHMSUser user) throws IOException {
        LOGGER.debug("Trying to retrieve a login form from EHMS...");
        Connection.Response response = Jsoup.connect(EHMSUrl.BASE.value()).execute();

        checkStatusCode(response);

        LOGGER.debug("Successfully retrieved the login form");
        Document responseBody = response.parse();

        Map<String, String> cookies = response.cookies();
        Map<String, String> formData = prepareLoginFormData(responseBody, user.login, user.password);

        LOGGER.debug("Trying to log in as user {} ...", user.login);

        Connection.Response loginResponse = Jsoup.connect(EHMSUrl.BASE.value()).data(formData).cookies(cookies).method(Connection.Method.POST).execute();
        checkStatusCode(loginResponse);

        Document loginResponseBody = loginResponse.parse();

        if (loginResponseBody.getElementsContainingText(NOT_LOGGED_IN).isEmpty()) {
            LOGGER.debug("Successfully logged in as user {}", user.login);
            user.cookies = cookies;
        } else {
            if (!loginResponseBody.getElementsContainingText(RATE_LIMITED).isEmpty()) {
                LOGGER.warn("User {} got rate-limited! Please wait or solve the captcha on a different device before trying to log in again!", user.login);
                throw new RateLimitExceededException(user.login);
            } else {
                LOGGER.warn("Authentication for user {} failed, are the login details correct?", user.login);
                throw new IllegalArgumentException(String.format("Authentication failed for user %s, are the login details correct?", user.login));
            }
        }
    }

    private static Map<String, String> prepareLoginFormData(Document document, String login, String password) {
        Elements form = document.getElementsByClass("form-control");

        String loginFieldName = form.get(0).attr("name");
        String passwordFieldName = form.get(1).attr("name");
        String counterFieldName = document.select("input[type=hidden]").get(1).val();

        return Map.of(loginFieldName, login, passwordFieldName, password, "log_form", "yes", "counter", counterFieldName);
    }

    private static void checkStatusCode(Connection.Response response) {
        if (response.statusCode() != 200) {
            LOGGER.error("EHMS returned an unexpected status code: {}", response.statusCode());
            throw new EHMSException(response.statusCode());
        }
    }

}
