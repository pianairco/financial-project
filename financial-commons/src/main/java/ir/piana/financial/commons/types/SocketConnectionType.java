package ir.piana.financial.commons.types;

/**
 * DEFAULT => ONE_TIME("one-time"),
 */
public enum SocketConnectionType {
    DEFAULT("default"),
    ONE_TIME("one-time"),
//    TIME_LIMIT("time-limit"),
    AFTER_IDLE_TIMEOUT("after-idle-timeout"),
    PERMANENT("permanent"),
    ;

    private final String type;

    SocketConnectionType(String type) {
        this.type = type;
    }

    public static SocketConnectionType byType(String type) {
        for (SocketConnectionType c : SocketConnectionType.values()) {
            if (c.type.equals(type)) {
                return c;
            }
        }
        return DEFAULT;
    }
}
