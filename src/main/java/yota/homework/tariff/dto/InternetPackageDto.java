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
public class InternetPackageDto {
    @PositiveOrZero
    @ApiModelProperty(example = "3.2")
    private Double gigaBytes;
    @PositiveOrZero
    @ApiModelProperty(example = "30")
    private int daysLeft;
}
