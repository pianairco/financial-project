package ir.piana.financial.commons.iso.channel.configs;

public class AsciiHeaderDetail {
    private String isoHeaderType;
    private int bytes;
    private int lenDigits;

    public AsciiHeaderDetail() {
    }

    public String getIsoHeaderType() {
        return isoHeaderType;
    }

    public void setIsoHeaderType(String isoHeaderType) {
        this.isoHeaderType = isoHeaderType;
    }

    public int getBytes() {
        return bytes;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    public int getLenDigits() {
        return lenDigits;
    }

    public void setLenDigits(int lenDigits) {
        this.lenDigits = lenDigits;
    }
}
