package pl.eukon05.pk4j.core;

enum EHMSUrl {
    BASE("https://ehms.pk.edu.pl/standard/"),
    PERSONAL_INFO("https://ehms.pk.edu.pl/standard/?tab=2");

    private final String url;

    EHMSUrl(String url) {
        this.url = url;
    }

    public String get() {
        return this.url;
    }
}
