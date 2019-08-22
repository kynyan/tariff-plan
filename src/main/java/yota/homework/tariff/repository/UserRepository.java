package yota.homework.tariff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yota.homework.tariff.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findOneByPassport(Long passport);

}
