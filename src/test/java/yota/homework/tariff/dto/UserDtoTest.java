package yota.homework.tariff.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.Long;
import java.util.Set;

import static io.qala.datagen.RandomShortApi.Long;
import static io.qala.datagen.RandomShortApi.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static yota.homework.tariff.dto.UserDto.*;

class UserDtoTest {
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void nameLengthViolatesBoundaries() {
        String invalidFirstName = sample(nullOrEmpty(), unicode(NAME_UPPER_BOUNDARY + 1));
        String invalidLastName = sample(nullOrEmpty(), unicode(NAME_UPPER_BOUNDARY + 1));
        UserDto user = randomUserDto();
        user.setFirstName(invalidFirstName);
        user.setLastName(invalidLastName);

        Set<ConstraintViolation<UserDto>> constraintViolations = validator.validate(user);

        assertEquals(2, constraintViolations.size() );
        assertEquals(NAME_NOTE, constraintViolations.iterator().next().getMessage());
        assertEquals(NAME_NOTE, constraintViolations.iterator().next().getMessage());
    }

    @Test
    void passportLengthViolatesBoundaries() {
        Long tooShortNumber = Long(Long.MIN_VALUE, (PASSPORT_LOWER_BOUNDARY - 1));
        Long tooLongNumber = Long((PASSPORT_UPPER_BOUNDARY + 1), Long.MAX_VALUE);
        Long invalidPassportNumber = nullOr(sample(tooShortNumber, tooLongNumber));
        UserDto user = randomUserDto();
        user.setPassport(invalidPassportNumber);

        Set<ConstraintViolation<UserDto>> constraintViolations = validator.validate(user);

        assertEquals(1, constraintViolations.size() );
        assertEquals(PASSPORT_NOTE, constraintViolations.iterator().next().getMessage());
    }

    @Test
    void phoneNumberLengthViolatesBoundaries() {
        Long tooShortNumber = Long(Long.MIN_VALUE, (PHONE_LOWER_BOUNDARY - 1));
        Long tooLongNumber = Long((PHONE_UPPER_BOUNDARY + 1), Long.MAX_VALUE);
        Long invalidPhoneNumber = nullOr(sample(tooShortNumber, tooLongNumber));
        UserDto user = randomUserDto();
        user.setPhoneNumber(invalidPhoneNumber);

        Set<ConstraintViolation<UserDto>> constraintViolations = validator.validate(user);

        assertEquals(1, constraintViolations.size() );
        assertEquals(PHONE_NUMBER_NOTE, constraintViolations.iterator().next().getMessage());
    }
}
