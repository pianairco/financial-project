package ir.piana.financial.commons.types;

public enum ResponseHeaderProvideType {
    DEFAULT("default"),
    PROVIDE_FROM_HANDLER("provide-from-handler"),
    PROVIDE_BY_PROVIDER_BEAN("provide-by-provider-bean"),
    ;

    private final String type;

    ResponseHeaderProvideType(String type) {
        this.type = type;
    }

    public static ResponseHeaderProvideType from(String type) {
        for (ResponseHeaderProvideType v : ResponseHeaderProvideType.values()) {
            if (v.type.equals(type)) {
                return v;
            }
        }
        return DEFAULT;
    }
}
