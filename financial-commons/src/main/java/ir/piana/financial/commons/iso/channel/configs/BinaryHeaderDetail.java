package ir.piana.financial.commons.iso.channel.configs;

public class BinaryHeaderDetail {
    private String isoHeaderType;
    private int bytes;
    private int lenBytes;

    public BinaryHeaderDetail() {
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

    public int getLenBytes() {
        return lenBytes;
    }

    public void setLenBytes(int lenBytes) {
        this.lenBytes = lenBytes;
    }
}
