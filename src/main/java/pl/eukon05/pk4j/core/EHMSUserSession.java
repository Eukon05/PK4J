package pl.eukon05.pk4j.core;

public abstract class EHMSUserSession {
    protected String sessionToken;

    public EHMSUserSession(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public final String getSessionToken() {
        return sessionToken;
    }
}
