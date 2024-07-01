package pl.eukon05.pk4j.core.impl.scraper;

import pl.eukon05.pk4j.core.EHMSUrlEnum;

enum ScraperEHMSUrl implements EHMSUrlEnum {
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
