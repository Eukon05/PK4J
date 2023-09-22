package pl.eukon05.pk4j.core;

import java.util.Collections;
import java.util.Map;

public class EHMSUser {
    private final String login;
    private final String password;
    private Map<String, String> cookies = Collections.emptyMap();

    public EHMSUser(String login, String password) {
        this.login = login;
        this.password = password;
    }

    Map<String, String> getCookies() {
        return cookies;
    }

    String getLogin() {
        return login;
    }

    String getPassword() {
        return password;
    }

    void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }
}
