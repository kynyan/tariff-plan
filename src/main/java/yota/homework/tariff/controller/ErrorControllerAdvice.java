package yota.homework.tariff.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import yota.homework.tariff.exception.ErrorCode;
import yota.homework.tariff.exception.ErrorCodeInfo;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.joining;
import static org.springframework.http.ResponseEntity.badRequest;

@ControllerAdvice(value={"yota.homework.tariff.controller"}) @Slf4j
public class ErrorControllerAdvice {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleConstraintViolationException(ConstraintViolationException exception,
                                                             HttpServletRequest req) {
        logStackTraceWithRequestDetails(exception, req);
        return prepareErrorForConstraintViolationException(exception);
    }

    private static ResponseEntity prepareErrorForConstraintViolationException(Exception exception) {
        Throwable cause = exception;
        log.warn("Constraint Failure (could be client sent invalid data)", cause);
        ConstraintViolationException violationException = null;
        while (cause != null) {
            if (cause instanceof ConstraintViolationException) {
                violationException = (ConstraintViolationException) cause;
            }
            cause = cause.getCause();
        }

        if (violationException == null) {
            return getBadRequestResponseEntity(exception);
        } else {
            Set<ConstraintViolation<?>> violationSet = violationException.getConstraintViolations();
            String violation = violationSet.stream().map(ConstraintViolation::getMessage).collect(joining(", "));
            ErrorCodeInfo body = new ErrorCodeInfo(ErrorCode.CONSTRAINT_VIOLATION, violation, violationException);
            return badRequest().body(body);
        }
    }

    private static ResponseEntity getBadRequestResponseEntity(Exception exception) {
        ErrorCodeInfo body = new ErrorCodeInfo(ErrorCode.CONSTRAINT_VIOLATION, "", exception);
        return badRequest().body(body);
    }

    private static void logStackTraceWithRequestDetails(Exception e, HttpServletRequest req) {
        log.warn(getRequestInfoAsString(req), e);
    }

    private static String getRequestInfoAsString(HttpServletRequest req) {
        StringBuilder paramStr = new StringBuilder();
        for (Map.Entry<String, String[]> param : req.getParameterMap().entrySet())
            paramStr.append(param.getKey()).append('=').append(String.join(",", param.getValue())).append(' ');
        return String.format("Error processing request [%s %s], params: [%s]",
                req.getMethod(), req.getRequestURL(), paramStr);
    }

}
