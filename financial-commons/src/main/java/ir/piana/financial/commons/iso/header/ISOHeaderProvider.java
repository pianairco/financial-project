package ir.piana.financial.commons.iso.header;

import org.jpos.iso.ISOMsg;

public interface ISOHeaderProvider {
    byte[] provide(ISOMsg requestIsoMsg, ISOMsg responseIsoMsg);
}
