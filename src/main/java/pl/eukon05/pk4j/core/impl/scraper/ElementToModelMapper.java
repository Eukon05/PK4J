package pl.eukon05.pk4j.core.impl.scraper;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.eukon05.pk4j.model.Announcement;
import pl.eukon05.pk4j.model.Gender;
import pl.eukon05.pk4j.model.PersonalData;
import pl.eukon05.pk4j.model.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

class ElementToModelMapper {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    Announcement announcementFromElement(Element element) {
        long id = Long.parseLong(element.attr("onclick").replaceAll("\\D", ""));
        Elements rows = element.select("td");
        String title = rows.get(1).text();

        Element contentNode = rows.get(2);
        Elements contentSpanSelect = contentNode.select("span");

        String content = contentSpanSelect.isEmpty() ? contentNode.text() : contentSpanSelect.get(0).attr("title");

        String priority = rows.get(4).text();
        String author = rows.get(5).text();
        LocalDateTime lastModified = LocalDateTime.parse(rows.get(6).text(), DATE_TIME_FORMATTER);

        return new Announcement(id, title, content, priority, author, lastModified);
    }

    UserDetails userDetailsFromElement(Element element) {
        PersonalData personalData = personalDataFromElements(element.select("#div_1 > div > div.col-sm-12.col-md-6.col-lg-5 > dl"));
        return new UserDetails(personalData);
    }

    PersonalData personalDataFromElements(Elements elements) {
        Elements basicInfo = getSection(elements, 0);

        String fullName = basicInfo.get(0).text();
        String familyNameValue = basicInfo.get(1).text();

        Optional<String> familyName = familyNameValue.equals("-") ? Optional.empty() : Optional.of(familyNameValue);

        Gender gender = Gender.fromString(basicInfo.get(2).text());

        String albumNumber = getSection(elements, 1).get(0).text();

        Elements addressInfo = getSection(elements, 2);

        String address = addressInfo.get(0).text();
        String voivodeship = addressInfo.get(1).text();

        Elements birthInfo = getSection(elements, 3);

        String peselValue = birthInfo.get(0).text();

        Optional<String> pesel = peselValue.equals("-") ? Optional.empty() : Optional.of(peselValue);

        LocalDate dob = LocalDate.parse(birthInfo.get(1).text(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String birthPlace = birthInfo.get(2).text();

        return new PersonalData(fullName, familyName, gender, albumNumber, address, voivodeship, pesel, dob, birthPlace);
    }

    private Elements getSection(Elements sections, int n) {
        return sections.get(n).select("dd");
    }
}
