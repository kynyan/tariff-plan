package yota.homework.tariff.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Duration;
import java.time.Instant;
import java.time.Period;

@Getter @NoArgsConstructor
@MappedSuperclass
public class Package {
    private static final String SEQUENCE_GENERATOR = "package_seq_gen";
    private static final String SEQUENCE_NAME = "PACKAGE_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_GENERATOR)
    @SequenceGenerator(name = SEQUENCE_GENERATOR, sequenceName = SEQUENCE_NAME)
    private Long id;

    private Instant usageStart;

    private Instant expirationDate;

    Package(int days) {
        this.usageStart = Instant.now();
        this.expirationDate = usageStart.plus(Period.ofDays(days));
    }

    int daysLeft() {
        return (int) Duration.between(usageStart, expirationDate).toDays();
    }
}
