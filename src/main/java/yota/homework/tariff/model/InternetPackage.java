package yota.homework.tariff.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import yota.homework.tariff.dto.InternetPackageDto;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "internet_packages")
public class InternetPackage extends Package {

    public InternetPackage() {
        super();
    }

    private Double gigaBytes;

    public InternetPackage(InternetPackageDto dto) {
        super(dto.getDaysLeft());
        this.gigaBytes = dto.getGigaBytes();
    }

    public InternetPackageDto toDto() {
        return InternetPackageDto.builder().gigaBytes(gigaBytes).daysLeft(daysLeft()).build();
    }
}
