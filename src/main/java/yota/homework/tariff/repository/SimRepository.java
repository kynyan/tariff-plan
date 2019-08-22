package yota.homework.tariff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yota.homework.tariff.model.SimCard;

public interface SimRepository extends JpaRepository<SimCard, Long> {

    SimCard findByPhoneNumber(Long phoneNumber);
}
