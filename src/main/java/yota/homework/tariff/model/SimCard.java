package yota.homework.tariff.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "sim_cards")
@NoArgsConstructor
public class SimCard {
    public static final String USER_FIELD = "user";
    private static final String SEQUENCE_GENERATOR = "sim_seq_gen";
    public static final String SEQUENCE_NAME = "SIM_SEQ";

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_GENERATOR)
    @SequenceGenerator(name = SEQUENCE_GENERATOR, sequenceName = SEQUENCE_NAME, allocationSize = 10)
    private Long id;

    private Long phoneNumber;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = User.ID_COLUMN)
    private User user;

    @Embedded
    private Plan plan;

    private Instant registrationDate;

    private boolean active;

    public SimCard(Long phoneNumber, User user) {
        this.phoneNumber = phoneNumber;
        this.user = user;
        this.registrationDate = Instant.now();
        this.active = true;
    }
}
