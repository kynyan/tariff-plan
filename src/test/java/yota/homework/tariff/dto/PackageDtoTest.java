package yota.homework.tariff.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static io.qala.datagen.RandomShortApi.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PackageDtoTest {
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void minutesPackageNumbersViolateBoundaries() {
        MinutesPackageDto minutesPackage = wrongMinutesPackage();

        Set<ConstraintViolation<MinutesPackageDto>> constraintViolations = validator.validate(minutesPackage);

        assertEquals(2, constraintViolations.size() );
        assertEquals(MinutesPackageDto.PACKAGE_NOTE, constraintViolations.iterator().next().getMessage());
        assertEquals(MinutesPackageDto.PACKAGE_NOTE, constraintViolations.iterator().next().getMessage());
    }

    @Test
    void internetPackageNumbersViolateBoundaries() {
        InternetPackageDto internetPackage = wrongInternetPackage();

        Set<ConstraintViolation<InternetPackageDto>> constraintViolations = validator.validate(internetPackage);

        assertEquals(2, constraintViolations.size() );
        assertEquals(InternetPackageDto.PACKAGE_NOTE, constraintViolations.iterator().next().getMessage());
        assertEquals(InternetPackageDto.PACKAGE_NOTE, constraintViolations.iterator().next().getMessage());
    }

    @Test
    void packageDaysLeftMustNotBeNull() {
        InternetPackageDto internetPackage = new InternetPackageDto(positiveDouble(), null);
        MinutesPackageDto minutesPackage = new MinutesPackageDto(positiveInteger(), null);
        Set<ConstraintViolation<InternetPackageDto>> inetPackageViolations = validator.validate(internetPackage);
        Set<ConstraintViolation<MinutesPackageDto>> minutesPackageViolations = validator.validate(minutesPackage);

        assertEquals(InternetPackageDto.DAYS_NOTE, inetPackageViolations.iterator().next().getMessage());
        assertEquals(MinutesPackageDto.DAYS_NOTE, minutesPackageViolations.iterator().next().getMessage());
    }

    private MinutesPackageDto wrongMinutesPackage() {
        Integer wrongMinutes = integer(Integer.MIN_VALUE, -1);
        Integer wrongDays = integer(Integer.MIN_VALUE, -1);
        return MinutesPackageDto.builder().minutes(wrongMinutes).daysLeft(wrongDays).build();
    }

    private InternetPackageDto wrongInternetPackage() {
        Double wrongGigabytes = negativeDouble();
        Integer wrongDays = integer(Integer.MIN_VALUE, -1);
        return InternetPackageDto.builder().gigaBytes(wrongGigabytes).daysLeft(wrongDays).build();
    }
}
