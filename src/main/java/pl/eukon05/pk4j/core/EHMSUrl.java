package pl.eukon05.pk4j.core;

enum EHMSUrl {
    BASE("https://ehms.pk.edu.pl/standard/"),
    PERSONAL_INFO("https://ehms.pk.edu.pl/standard/?tab=2");

    private final String value;

    EHMSUrl(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
