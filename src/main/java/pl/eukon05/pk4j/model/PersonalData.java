package pl.eukon05.pk4j.model;

import java.time.LocalDate;
import java.util.Optional;

public record PersonalData(String fullName,
                           Optional<String> familyName,
                           Gender gender,
                           String albumNumber,
                           String address,
                           String voivodeship,
                           Optional<String> pesel,
                           LocalDate dob,
                           String birthPlace) {
}
