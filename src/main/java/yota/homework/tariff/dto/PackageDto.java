package yota.homework.tariff.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PackageDto {
    @Valid
    private InternetPackageDto internetPackage;
    @Valid
    private MinutesPackageDto minutesPackage;
}
