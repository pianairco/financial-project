package ir.piana.financial.commons.iso.channel;

import ir.piana.financial.commons.errors.FinancialWrappedException;
import ir.piana.financial.commons.types.ISOPackagerType;
import org.jpos.iso.ISOMsg;

public record ISOPayload (
        byte[] headerBytes,
        byte[] messageBytes,
        ISOMsg isoMsg
) {
    public ISOPayload (byte[] headerBytes, ISOMsg isoMsg) {
        this(headerBytes, null, isoMsg);
    }

    public ISOPayload (byte[] headerBytes, ISOMsg isoMsg, ISOPackagerType isoPackagerType) throws FinancialWrappedException {
        this(headerBytes, isoPackagerType.pack(isoMsg), isoMsg);
    }

    public ISOPayload (byte[] headerBytes, byte[] messageBytes) {
        this(headerBytes, messageBytes, null);
    }

    public ISOPayload createNewByIsoMsg(ISOPackagerType isoPackagerType) throws FinancialWrappedException {
        return new ISOPayload(this.headerBytes, this.messageBytes, isoPackagerType.unpack(this.messageBytes));
    }

    public ISOPayload createNewByMessageBytes(ISOPackagerType isoPackagerType) throws FinancialWrappedException {
        return new ISOPayload(this.headerBytes, isoPackagerType.pack(this.isoMsg), this.isoMsg);
    }
}
