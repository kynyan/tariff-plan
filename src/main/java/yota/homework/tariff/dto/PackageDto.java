package yota.homework.tariff.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PackageDto {
    private InternetPackageDto internetPackage;
    private MinutesPackageDto minutesPackage;
}
