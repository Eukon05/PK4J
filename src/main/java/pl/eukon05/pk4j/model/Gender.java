package pl.eukon05.pk4j.model;

public enum Gender {
    FEMALE, MALE;

    public static Gender fromString(String value) {
        return value.equalsIgnoreCase("kobieta") ? FEMALE : MALE;
    }
}
