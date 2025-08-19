package ir.piana.financial.commons.errors;

public class FinancialWrappedException extends Exception {
    private final Object[] args;
    private final FinancialExceptionType exceptionType;

    public FinancialWrappedException(FinancialExceptionType exceptionType, String message, Object ... args) {
        this(exceptionType, null, message, args);
    }

    public FinancialWrappedException(FinancialExceptionType exceptionType, Throwable underlingException) {
        this(exceptionType, underlingException, null);
    }

    public FinancialWrappedException(FinancialExceptionType exceptionType, Throwable underlingException, String message, Object... args) {
        super(message == null ? "" : message, underlingException);
        this.exceptionType = exceptionType;
        this.args = args == null ? new Object[0] : args;
    }

    @Override
    public String getMessage() {
        if (args.length == 0)
            return super.getMessage();
        else return String.format(super.getMessage(), args);
    }

    public FinancialExceptionType getExceptionType() {
        return exceptionType;
    }
}
