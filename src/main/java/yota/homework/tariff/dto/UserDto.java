package yota.homework.tariff.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private static final String PHONE_NUMBER_NOTE = "Should consist of 10 digits";
    private static final String PASSPORT_NOTE = "Should consist of 10 digits";
    private static final String NAME_REGEX = "^[A-Za-z0-9_'-.\\s]+$";

    @NotNull
    @Size(min = 1, max = 255) @Pattern(regexp = NAME_REGEX)
    @ApiModelProperty(example = "Ivan")
    private String firstName;

    @NotNull @Size(min = 1, max = 255) @Pattern(regexp = NAME_REGEX)
    @ApiModelProperty(example = "Petrov")
    private String lastName;

    @Min(value = 1000000000L, message = PASSPORT_NOTE)
    @Max(value = 9999999999L, message = PASSPORT_NOTE)
    @ApiModelProperty(example = "1385698006")
    private Long passport;

    @NotNull
    @Min(value = 9000000000L, message = PHONE_NUMBER_NOTE)
    @Max(value = 9999999999L, message = PHONE_NUMBER_NOTE)
    @ApiModelProperty(example = "9999199299")
    private Long phoneNumber;
}
