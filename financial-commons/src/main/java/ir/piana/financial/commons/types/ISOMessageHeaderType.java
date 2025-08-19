package ir.piana.financial.commons.types;

public enum ISOMessageHeaderType {
    SHETAB_7_V93("shetab-7-v93", "1d=control|4s=version|4d=reserved|11d=target|11d=source|16s=shetabSpecial|6s=local|4d=errorCode"),
    ;

    private final String name;
    private final String format;

    ISOMessageHeaderType(String name, String format) {
        this.name = name;
        this.format = format;
    }
}
