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
    @PositiveOrZero
    @ApiModelProperty(example = "300")
    private Integer minutes;
    @PositiveOrZero
    @ApiModelProperty(example = "15")
    private int daysLeft;
}
