package ir.piana.financial.commons.iso.channel.configs;

import ir.piana.financial.commons.types.ISOChannelType;
import ir.piana.financial.commons.types.ISOHeaderType;

public class ChannelInfo {
    private String name;
    private String isoChannelType;

    private BinaryHeaderDetail binaryHeaderDetail;
    private BinaryDetail binaryDetail;

    private AsciiHeaderDetail asciiHeaderDetail;
    private AsciiDetail asciiDetail;

    public ChannelInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsoChannelType() {
        return isoChannelType;
    }

    public void setIsoChannelType(String isoChannelType) {
        this.isoChannelType = isoChannelType;
    }

    public BinaryHeaderDetail getBinaryHeaderDetail() {
        return binaryHeaderDetail;
    }

    public void setBinaryHeaderDetail(BinaryHeaderDetail binaryHeaderDetail) {
        this.binaryHeaderDetail = binaryHeaderDetail;
    }

    public BinaryDetail getBinaryDetail() {
        return binaryDetail;
    }

    public void setBinaryDetail(BinaryDetail binaryDetail) {
        this.binaryDetail = binaryDetail;
    }

    public AsciiHeaderDetail getAsciiHeaderDetail() {
        return asciiHeaderDetail;
    }

    public void setAsciiHeaderDetail(AsciiHeaderDetail asciiHeaderDetail) {
        this.asciiHeaderDetail = asciiHeaderDetail;
    }

    public AsciiDetail getAsciiDetail() {
        return asciiDetail;
    }

    public void setAsciiDetail(AsciiDetail asciiDetail) {
        this.asciiDetail = asciiDetail;
    }

    public boolean hasHeader() {
        if (ISOChannelType.byName(isoChannelType) == ISOChannelType.ASCII_CHANNEL) {
            //ToDo handle null pointer exception
            return ISOHeaderType.byType(asciiHeaderDetail.getIsoHeaderType()).equalsAny(ISOHeaderType.FIX, ISOHeaderType.VARIABLE);
        } else {
            return ISOHeaderType.byType(binaryHeaderDetail.getIsoHeaderType()).equalsAny(ISOHeaderType.FIX, ISOHeaderType.VARIABLE);
        }
    }
}
