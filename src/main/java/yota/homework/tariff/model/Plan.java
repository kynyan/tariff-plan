package yota.homework.tariff.model;

import lombok.*;
import yota.homework.tariff.dto.PackageDto;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plan {

    @OneToOne
    @JoinColumn(name = "internet_package_id")
    private InternetPackage internetPackage;

    @OneToOne
    @JoinColumn(name = "minutes_package_id")
    private MinutesPackage minutesPackage;

    Plan(PackageDto packageDto) {
        if (packageDto.getInternetPackage() != null)
            this.internetPackage = new InternetPackage(packageDto.getInternetPackage());
        if (packageDto.getMinutesPackage() != null)
            this.minutesPackage = new MinutesPackage(packageDto.getMinutesPackage());
    }
}
