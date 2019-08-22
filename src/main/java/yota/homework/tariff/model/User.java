package yota.homework.tariff.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import yota.homework.tariff.dto.UserDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    public static final String ID_COLUMN = "USER_ID";
    public static final String SEQUENCE_GENERATOR = "user_seq_gen";
    public static final String SEQUENCE_NAME = "USER_SEQ";
    public static final int BATCH_SIZE = 50;

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_GENERATOR)
    @SequenceGenerator(name = SEQUENCE_GENERATOR, sequenceName = SEQUENCE_NAME, allocationSize = 1)
    private Long id;

    private String firstName;

    private String lastName;

    private Long passport;

    @OneToMany(mappedBy = SimCard.USER_FIELD, cascade = CascadeType.ALL)
    @BatchSize(size = BATCH_SIZE)
    private List<SimCard> simCards = new ArrayList<>();

    public User (UserDto dto) {
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.passport = dto.getPassport();
        this.simCards.add(new SimCard(dto.getPhoneNumber(), this));
    }
}
