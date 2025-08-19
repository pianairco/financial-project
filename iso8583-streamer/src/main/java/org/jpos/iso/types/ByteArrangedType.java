package org.jpos.iso.types;

public enum ByteArrangedType {
    LSB("lsb"),
    HSB("hsb"),
    ;

    private final String type;

    ByteArrangedType(String type) {
        this.type = type;
    }

    public static ByteArrangedType byType(String type){
        for (ByteArrangedType value : ByteArrangedType.values()) {
            if (value.type.equalsIgnoreCase(type.trim())) {
                return value;
            }
        }
        return null;
    }

    public boolean equals(String type) {
        if (this.type.equalsIgnoreCase(type.trim())) {
            return true;
        }
        return false;
    }

    public String getType() {
        return type;
    }
}
