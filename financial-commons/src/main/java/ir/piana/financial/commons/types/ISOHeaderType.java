package ir.piana.financial.commons.types;

import java.util.Arrays;

public enum ISOHeaderType {
    NONE("none"),
    NOT_SUPPORTED("not-supported"),
    FIX("fix"),
    VARIABLE("variable"),
    NOT_SEPARATED_FROM_THE_MESSAGE("not-separated-from-the-message"),
    ;

    private final String type;

    ISOHeaderType(String type) {
        this.type = type;
    }

    public static ISOHeaderType byType(String type){
        for (ISOHeaderType value : ISOHeaderType.values()) {
            if (value.type.equalsIgnoreCase(type.trim())) {
                return value;
            }
        }
        return NOT_SUPPORTED;
    }

    public boolean equals(String type) {
        if (this.type.equalsIgnoreCase(type.trim())) {
            return true;
        }
        return false;
    }

    public boolean equalsAny(ISOHeaderType... types) {
        return Arrays.asList(types).contains(this);
    }

    public String getType() {
        return type;
    }
}
