package yota.homework.tariff.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InternetPackageDto {
    public static final String PACKAGE_NOTE = "Should not be negative";
    public static final String DAYS_NOTE = "Days should be specified";

    @PositiveOrZero(message = PACKAGE_NOTE)
    @ApiModelProperty(example = "3.2")
    private Double gigaBytes;

    @NotNull(message = DAYS_NOTE)
    @PositiveOrZero(message = PACKAGE_NOTE)
    @ApiModelProperty(example = "30")
    private Integer daysLeft;
}
