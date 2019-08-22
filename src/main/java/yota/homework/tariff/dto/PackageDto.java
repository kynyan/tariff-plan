package yota.homework.tariff.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class PackageDto {
    private InternetPackageDto internetPackage;
    private MinutesPackageDto minutesPackage;
}
