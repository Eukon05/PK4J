package pl.eukon05.pk4j.core;

import java.util.Optional;

public class EHMSUser {
    private final String username;
    private String userID;
    private final String password;
    private String studyID;
    private String sessionToken;

    private EHMSUser(String username, String password) {
        if (username == null || password == null)
            throw new NullPointerException();

        if (username.isBlank() || password.isBlank())
            throw new IllegalArgumentException("Credentials cannot be empty strings!");

        this.username = username;
        this.password = password;
    }

    public static EHMSUser fromCredentials(String username, String password) {
        return new EHMSUser(username, password);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Optional<String> getUserID() {
        return Optional.ofNullable(userID);
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Optional<String> getStudyID() {
        return Optional.ofNullable(studyID);
    }

    public void setStudyID(String studyID) {
        this.studyID = studyID;
    }

    public Optional<String> getSessionToken() {
        return Optional.ofNullable(sessionToken);
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
