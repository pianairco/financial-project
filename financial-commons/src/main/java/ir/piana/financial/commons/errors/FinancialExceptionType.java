package ir.piana.financial.commons.errors;

import io.nats.client.JetStreamApiException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public enum FinancialExceptionType {
    TimeOut,
    ConnectionFailure,
    UnknownUpstreamException,
    AgreedUpstreamError,
    UnknownException,
    InternalError,
    ISOError,
    ;

    public FinancialWrappedException wrapException(Throwable throwable, String message, Object... args) {
        return new FinancialWrappedException(this, throwable, message, args);
    }

    public FinancialWrappedException returnException(String message, Object... args) {
        return new FinancialWrappedException(this, message, args);
    }

    public void throwsException(String message, Object... args) throws FinancialWrappedException {
        throw returnException(message, args);
    }

    public void throwsWrappedException(Throwable throwable, String message, Object... args) throws FinancialWrappedException {
        throw wrapException(throwable, message, args);
    }

    public static FinancialWrappedException wrapAnException(Throwable throwable) {
        return wrapAnException(throwable, null);
    }

    public static FinancialWrappedException wrapAnException(Throwable throwable, String message, Object... args) {
        return switch (throwable) {
            case IOException e -> FinancialExceptionType.ConnectionFailure.wrapException(throwable, message, args);
            case JetStreamApiException e -> FinancialExceptionType.UnknownUpstreamException.wrapException(throwable, message, args);
            case ExecutionException e -> FinancialExceptionType.UnknownException.wrapException(throwable, message, args);
            case InterruptedException e -> FinancialExceptionType.UnknownException.wrapException(throwable, message, args);
            case TimeoutException e -> FinancialExceptionType.TimeOut.wrapException(throwable, message, args);
            default -> FinancialExceptionType.UnknownException.wrapException(throwable, message, args);
        };
    }

    public static void throwsAnException(Throwable throwable) throws FinancialWrappedException {
        throw wrapAnException(throwable);
    }

    public static void throwsAnException(Throwable throwable, String message, Object... args) throws FinancialWrappedException {
        throw wrapAnException(throwable, message, args);
    }
}
