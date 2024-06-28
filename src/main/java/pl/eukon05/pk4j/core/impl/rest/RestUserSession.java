package pl.eukon05.pk4j.core.impl.rest;

import pl.eukon05.pk4j.core.EHMSUserSession;

final class RestUserSession extends EHMSUserSession {
    private final String userID;
    private final String studyID;

    public RestUserSession(String sessionToken, String userID, String studyID) {
        super(sessionToken);
        this.userID = userID;
        this.studyID = studyID;
    }

    public String getUserID() {
        return userID;
    }

    public String getStudyID() {
        return studyID;
    }
}
