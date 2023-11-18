package pl.eukon05.pk4j.core.impl.scraper;

enum ScraperEHMSUrl {
    BASE("https://ehms.pk.edu.pl/standard/"),
    USER_DETAILS("https://ehms.pk.edu.pl/standard/?tab=2");

    private final String value;

    ScraperEHMSUrl(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
