package yota.homework.tariff.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import yota.homework.tariff.RepositoryTest;
import yota.homework.tariff.dto.InternetPackageDto;
import yota.homework.tariff.dto.MinutesPackageDto;
import yota.homework.tariff.dto.PackageDto;
import yota.homework.tariff.exception.NiceException;
import yota.homework.tariff.model.SimCard;
import yota.homework.tariff.model.User;
import yota.homework.tariff.repository.SimRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static io.qala.datagen.RandomShortApi.Long;
import static io.qala.datagen.RandomShortApi.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static yota.homework.tariff.dto.UserDto.PHONE_LOWER_BOUNDARY;
import static yota.homework.tariff.dto.UserDto.PHONE_UPPER_BOUNDARY;

import java.lang.Double;
import java.lang.Long;

@ExtendWith(SpringExtension.class)
@RepositoryTest
@Import(SimCardService.class)
public class SimCardServiceTest {
    @Autowired
    private SimCardService simCardService;
    @Autowired
    private SimRepository simRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void shouldGetAvailableGigaBytesAndMinutes() {
        Long randomPhoneNumber = Long(PHONE_LOWER_BOUNDARY, PHONE_UPPER_BOUNDARY);
        createSimCard(randomPhoneNumber);

        PackageDto packageDto = createPackageDtoWithMinutesAndGigaBytes();
        simCardService.updatePackage(randomPhoneNumber, packageDto);

        PackageDto actual = simCardService.getGigsAndMinutesLeft(randomPhoneNumber);
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(packageDto);
    }

    @Test
    public void shouldThrowNiceException_ifSimCardNotFound() {
        Long randomPhoneNumber = Long(PHONE_LOWER_BOUNDARY, PHONE_UPPER_BOUNDARY);
        createSimCard(randomPhoneNumber);

        Long anotherPhoneNumber = Long(PHONE_LOWER_BOUNDARY, PHONE_UPPER_BOUNDARY);

        assertThatExceptionOfType(NiceException.class)
                .isThrownBy(() -> simCardService.getGigsAndMinutesLeft(anotherPhoneNumber))
                .withMessageContaining("SIM card with " + anotherPhoneNumber + " phone number not found");
    }

    @Test
    public void shouldActivateOrBlockSimByPhoneNumber() {
        Long randomPhoneNumber = Long(PHONE_LOWER_BOUNDARY, PHONE_UPPER_BOUNDARY);
        boolean activateOrBlock = bool();

        createSimCard(randomPhoneNumber);
        simCardService.activateOrBlockSim(randomPhoneNumber, activateOrBlock);

        SimCard simCard = simRepository.findByPhoneNumber(randomPhoneNumber);
        assertEquals(activateOrBlock, simCard.isActive());
    }

    @Test
    public void shouldUpdatePackageWithGigaBytesAndMinutes() {
        Long randomPhoneNumber = Long(PHONE_LOWER_BOUNDARY, PHONE_UPPER_BOUNDARY);
        createSimCard(randomPhoneNumber);

        PackageDto firstPackage = createPackageDtoWithMinutesAndGigaBytes();
        PackageDto firstUpdated = simCardService.updatePackage(randomPhoneNumber, firstPackage);
        assertThat(firstUpdated).isEqualToComparingFieldByFieldRecursively(firstPackage);

        PackageDto secondPackage = createPackageDtoWithMinutesAndGigaBytes();
        PackageDto secondUpdated = simCardService.updatePackage(randomPhoneNumber, secondPackage);
        assertThat(secondUpdated).isEqualToComparingFieldByFieldRecursively(secondPackage);
    }

    private void createSimCard(Long phoneNumber) {
        simRepository.save(new SimCard(phoneNumber, new User()));
        flushAndClear();
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

    private PackageDto createPackageDtoWithMinutesAndGigaBytes() {
        Integer minutes = integer(positiveInteger());
        Integer days = integer(positiveInteger());
        MinutesPackageDto minutesPackage = MinutesPackageDto.builder().minutes(minutes).daysLeft(days).build();

        Double gigabytes = positiveDouble();
        InternetPackageDto internetPackage = InternetPackageDto.builder().gigaBytes(gigabytes).daysLeft(days).build();

        return new PackageDto(internetPackage, minutesPackage);
    }
}
