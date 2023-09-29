package pl.eukon05.pk4j.model;

import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record PersonalData(String fullName,
                           String familyName,
                           Gender gender,
                           String albumNumber,
                           String address,
                           String voivodeship,
                           String pesel,
                           LocalDate dob,
                           String birthPlace) {

    static PersonalData fromElements(Elements elements) {
        Elements basicInfo = getSection(elements, 0);

        String fullName = basicInfo.get(0).text();
        String familyName = basicInfo.get(1).text();
        Gender gender = Gender.fromString(basicInfo.get(2).text());

        String albumNumber = getSection(elements, 1).get(0).text();

        Elements addressInfo = getSection(elements, 2);

        String address = addressInfo.get(0).text();
        String voivodeship = addressInfo.get(1).text();

        Elements birthInfo = getSection(elements, 3);

        String pesel = birthInfo.get(0).text();
        LocalDate dob = LocalDate.parse(birthInfo.get(1).text(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String birthPlace = birthInfo.get(2).text();

        return new PersonalData(fullName, familyName, gender, albumNumber, address, voivodeship, pesel, dob, birthPlace);
    }

    private static Elements getSection(Elements sections, int n) {
        return sections.get(n).select("dd");
    }

}
