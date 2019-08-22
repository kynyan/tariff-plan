package yota.homework.tariff.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "timestamp")
public class ErrorCodeInfo {

    @NonNull
    private ErrorCode errorCode;

    private String description;

    @JsonIgnore
    private Exception nestedException;

    private ZonedDateTime timestamp;
    private String exception;
    private String message;

    public ErrorCodeInfo(final ErrorCode errorCode, final String description, final Exception ex) {
        this.errorCode = errorCode;
        this.description = description;
        timestamp = ZonedDateTime.now();
        if (ex != null) {
            nestedException = ex;
            exception = ex.getClass().getName();
            message = ex.getMessage();
        }
    }

    public ErrorCodeInfo(final ErrorCode errorCode, final String description) {
        this(errorCode, description, null);
    }
}
