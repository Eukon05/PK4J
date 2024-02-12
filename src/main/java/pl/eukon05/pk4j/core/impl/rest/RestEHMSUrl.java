package pl.eukon05.pk4j.core.impl.rest;

enum RestEHMSUrl {
    AUTHENTICATE("https://ehms.pk.edu.pl/api/users/authenticate"),
    ALL_ANNOUNCEMENTS("https://ehms.pk.edu.pl/api/announcements"),
    ANNOUNCEMENT("https://ehms.pk.edu.pl/api/announcements/announcement"),
    USER_DETAILS("https://ehms.pk.edu.pl/api/student/profile/about");

    private final String value;

    RestEHMSUrl(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
