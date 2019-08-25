package yota.homework.tariff.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import yota.homework.tariff.RepositoryTest;
import yota.homework.tariff.model.SimCard;
import yota.homework.tariff.model.User;
import yota.homework.tariff.repository.SimRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static io.qala.datagen.RandomShortApi.Long;
import static org.assertj.core.api.Assertions.assertThat;
import static yota.homework.tariff.dto.UserDto.PHONE_LOWER_BOUNDARY;
import static yota.homework.tariff.dto.UserDto.PHONE_UPPER_BOUNDARY;

@ExtendWith(SpringExtension.class)
@RepositoryTest
class SimRepositoryTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SimRepository simRepository;

    @Test
    void mustFindSimCardByPhoneNumber() {
        Long randomPhoneNumber = Long(PHONE_LOWER_BOUNDARY, PHONE_UPPER_BOUNDARY);
        SimCard card = new SimCard(randomPhoneNumber, new User());
        SimCard added = simRepository.save(card);
        flushAndClear();
        SimCard fromDb = simRepository.findByPhoneNumber(randomPhoneNumber);
        assertThat(added).isEqualToIgnoringGivenFields(fromDb, "user");
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
