package ir.piana.financial.commons.iso.header.impl;

import ir.piana.financial.commons.iso.header.ISOHeaderProcessor;
import ir.piana.financial.commons.iso.header.ISOHeaderProvider;
import ir.piana.financial.commons.utilities.ByteArrayReader;
import ir.piana.financial.commons.utilities.BytesManipulation;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.types.ByteArrangedType;

public class Shetab7V98ISOHeaderProvider implements ISOHeaderProvider {
    @Override
    public byte[] provide(ISOMsg requestIsoMsg, ISOMsg responseIsoMsg) {
        byte[] header = requestIsoMsg.getHeader();
        return BytesManipulation.changePartsAsNewBytes(header, 9, 20, 11);
    }
}
