package yota.homework.tariff.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NiceException extends ResponseStatusException {
    private final ErrorCodeInfo errorCodeInfo;

    public NiceException(ErrorCodeInfo errorCodeInfo, HttpStatus httpStatus) {
        super(httpStatus, errorCodeInfo.getDescription());
        this.errorCodeInfo = errorCodeInfo;
    }

    public NiceException(ErrorCode errorCode, HttpStatus httpStatus, String errorMessage) {
        this(new ErrorCodeInfo(errorCode, errorMessage), httpStatus);
    }
}
