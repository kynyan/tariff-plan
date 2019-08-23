package yota.homework.tariff.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.PositiveOrZero;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MinutesPackageDto {
    public static final String PACKAGE_NOTE = "Should not be negative";

    @PositiveOrZero(message = PACKAGE_NOTE)
    @ApiModelProperty(example = "300")
    private Integer minutes;

    @PositiveOrZero(message = PACKAGE_NOTE)
    @ApiModelProperty(example = "15")
    private Integer daysLeft;
}
