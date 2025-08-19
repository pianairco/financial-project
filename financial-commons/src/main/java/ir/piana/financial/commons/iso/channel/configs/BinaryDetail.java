package ir.piana.financial.commons.iso.channel.configs;

public class BinaryDetail {
    private String lenBytesArranged;
    private int totalLenBytes;

    public BinaryDetail() {
    }

    public String getLenBytesArranged() {
        return lenBytesArranged;
    }

    public void setLenBytesArranged(String lenBytesArranged) {
        this.lenBytesArranged = lenBytesArranged;
    }

    public int getTotalLenBytes() {
        return totalLenBytes;
    }

    public void setTotalLenBytes(int totalLenBytes) {
        this.totalLenBytes = totalLenBytes;
    }
}
