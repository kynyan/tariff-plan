package yota.homework.tariff.model;

import lombok.Getter;
import yota.homework.tariff.dto.MinutesPackageDto;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "minutes_packages")
public class MinutesPackage extends Package {
    private Integer minutes;

    public MinutesPackage() {
        super();
    }

    public MinutesPackage(MinutesPackageDto dto) {
        super(dto.getDaysLeft());
        this.minutes = dto.getMinutes();
    }

    public MinutesPackageDto toDto() {
        return MinutesPackageDto.builder().minutes(minutes).daysLeft(daysLeft()).build();
    }
}
