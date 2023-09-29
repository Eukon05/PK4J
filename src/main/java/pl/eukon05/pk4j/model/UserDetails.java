package pl.eukon05.pk4j.model;

import org.jsoup.nodes.Element;

public record UserDetails(PersonalData personalData) {

    public static UserDetails fromElement(Element element) {
        PersonalData personalData = PersonalData.fromElements(element.select("#div_1 > div > div.col-sm-12.col-md-6.col-lg-5 > dl"));
        return new UserDetails(personalData);
    }
}
