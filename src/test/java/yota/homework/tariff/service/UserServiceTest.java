package yota.homework.tariff.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import yota.homework.tariff.RepositoryTest;
import yota.homework.tariff.dto.UserDto;
import yota.homework.tariff.exception.NiceException;
import yota.homework.tariff.model.SimCard;
import yota.homework.tariff.model.User;
import yota.homework.tariff.repository.SimRepository;
import yota.homework.tariff.repository.UserRepository;

import java.time.Instant;
import java.util.List;

import static io.qala.datagen.RandomShortApi.Long;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static yota.homework.tariff.dto.UserDto.*;

@ExtendWith(SpringExtension.class)
@RepositoryTest
@Import({UserService.class, SimCardService.class})
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SimRepository simRepository;

    @Test
    void mustCreateNewUserFromDto() {
        UserDto original = UserDto.randomUserDto();
        UserDto created = userService.createUser(original);
        assertThat(created).isEqualToComparingFieldByFieldRecursively(original);
    }

    @Test
    void mustThrowNiceException_ifPhoneNumberAlreadyExists() {
        Long randomPhoneNumber = Long(PHONE_LOWER_BOUNDARY, PHONE_UPPER_BOUNDARY);
        simRepository.save(new SimCard(randomPhoneNumber, new User()));

        UserDto original = UserDto.randomUserDto();
        original.setPhoneNumber(randomPhoneNumber);

        assertThatExceptionOfType(NiceException.class)
                .isThrownBy(() -> userService.createUser(original))
                .withMessageContaining("Phone number " + randomPhoneNumber + " already exists");
    }

    @Test
    void mustAddNewSimCard_ifUserAlreadyHasOne() {
        UserDto original = createUserWithSimCard();
        Long anotherPhoneNumber = Long(PHONE_LOWER_BOUNDARY, PHONE_UPPER_BOUNDARY);
        original.setPhoneNumber(anotherPhoneNumber);
        userService.createUser(original);
        User user = userRepository.findOneByPassport(original.getPassport());
        assertEquals(2, user.getSimCards().size());

    }

    @Test
    void mustReturnDtoWithLatestAddedPhone_ifUserAlreadyHasSeveralSimCards() {
        //User with 3 sim cards is created
        UserDto original = createUserWithSimCard();
        Long secondPhoneNumber = Long(PHONE_LOWER_BOUNDARY, PHONE_UPPER_BOUNDARY);
        original.setPhoneNumber(secondPhoneNumber);
        userService.createUser(original);
        Long thirdPhoneNumber = Long(PHONE_LOWER_BOUNDARY, PHONE_UPPER_BOUNDARY);
        original.setPhoneNumber(thirdPhoneNumber);

        //We take the last one to verify that its phone number corresponds to the most recently added SIM
        UserDto last = userService.createUser(original);

        //Get all SIM cards and make sure the third one is the latest
        User user = userRepository.findOneByPassport(original.getPassport());
        List<SimCard> allCards = user.getSimCards();
        SimCard simCard = user.getSimCards().stream()
                .filter(card -> card.getPhoneNumber().equals(thirdPhoneNumber))
                .findAny().get();
        Instant thirdSimRegistration = simCard.getRegistrationDate();
        for (SimCard card : allCards) {
            Instant registrationDate = card.getRegistrationDate();
            assertTrue(registrationDate.isBefore(thirdSimRegistration) || registrationDate.equals(thirdSimRegistration));
        }

        //Check that phone number corresponds to the most recently added SIM card
        assertEquals(last.getPhoneNumber(), simCard.getPhoneNumber());

    }

    private UserDto createUserWithSimCard() {
        UserDto original = UserDto.randomUserDto();
        return userService.createUser(original);

    }
}
