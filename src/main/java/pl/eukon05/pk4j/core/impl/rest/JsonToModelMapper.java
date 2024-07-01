package pl.eukon05.pk4j.core.impl.rest;

import com.google.gson.JsonObject;
import pl.eukon05.pk4j.model.Announcement;
import pl.eukon05.pk4j.model.Gender;
import pl.eukon05.pk4j.model.PersonalData;
import pl.eukon05.pk4j.model.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

final class JsonToModelMapper {
    Announcement announcementFromJson(JsonObject object, long id) {
        return new Announcement(id,
                object.get("title").getAsString(),
                object.get("content").getAsString(),
                object.get("priority").getAsString(),
                object.get("author").getAsString(),
                LocalDateTime.parse(object.get("modification_date").getAsString()));
    }

    public UserDetails detailsFromJson(JsonObject data) {
        JsonObject addressObject = data.getAsJsonObject("correspondence_address");
        String address = String.format("%s %s/%s, %s %s",
                addressObject.get("street").getAsString(),
                addressObject.get("street_no").getAsString(),
                addressObject.get("apartment_no").getAsString(),
                addressObject.get("postal_code").getAsString(),
                addressObject.get("city").getAsString()
        );

        String maidenName = data.get("maiden_name").getAsString();
        Optional<String> maidenNameOptional = maidenName.isBlank() ? Optional.empty() : Optional.of(maidenName);

        String pesel = data.get("pesel").getAsString();
        Optional<String> peselOptional = pesel.isBlank() ? Optional.empty() : Optional.of(pesel);

        PersonalData personalData = new PersonalData(
                String.format("%s, %s %s", data.get("surname").getAsString(), data.get("first_name").getAsString(), data.get("second_name").getAsString()),
                maidenNameOptional,
                data.get("gender").getAsString().equals("M") ? Gender.MALE : Gender.FEMALE,
                data.get("study_info").getAsJsonObject().get("album").getAsString(),
                address,
                addressObject.get("voivodeship").getAsString(),
                peselOptional,
                LocalDate.parse(data.get("birth").getAsString()),
                data.get("birthplace").getAsString()
        );

        return new UserDetails(personalData);
    }
}
