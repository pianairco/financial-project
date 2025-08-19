package ir.piana.financial.commons.iso.header.impl;

import ir.piana.financial.commons.iso.header.ISOHeaderProcessor;
import ir.piana.financial.commons.utilities.ByteArrayReader;
import ir.piana.financial.commons.utilities.BytesManipulation;
import org.jpos.iso.types.ByteArrangedType;

public class Shetab7V98ISOHeaderProcessor implements ISOHeaderProcessor {
    @Override
    public boolean isCorrect(byte[] requestHeader) {
        if (requestHeader.length < 57)
            return false;

        byte[] control = ByteArrayReader.read(requestHeader, 0, 1);
        String version = ByteArrayReader.readAsString(requestHeader, 1, 4);
        int reserved = ByteArrayReader.readAsInt(requestHeader, 5, ByteArrangedType.HSB);
        String target = ByteArrayReader.readAsString(requestHeader, 9, 11);
        String source = ByteArrayReader.readAsString(requestHeader, 20, 11);
        String shetabSpecial = ByteArrayReader.readAsString(requestHeader, 31, 16);
        String local = ByteArrayReader.readAsString(requestHeader, 47, 6);
        int errorCode = ByteArrayReader.readAsInt(requestHeader, 53, ByteArrangedType.HSB);

        return true;
    }
}
