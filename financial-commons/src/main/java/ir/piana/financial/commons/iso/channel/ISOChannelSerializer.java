package ir.piana.financial.commons.iso.channel;

import ir.piana.financial.commons.errors.FinancialWrappedException;
import ir.piana.financial.commons.types.ISOPackagerType;
import org.jpos.iso.ISOMsg;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ISOChannelSerializer {
    byte[] readIsoPayloadBytes(InputStream inputStream) throws IOException;
    ISOPayload readIsoPayload(InputStream inputStream) throws IOException, FinancialWrappedException;
    void writeIsoPayloadBytesByLength(byte[] payload, OutputStream outputStream) throws IOException;
    void writeIsoPayloadBytes(byte[] payload, OutputStream outputStream) throws IOException;
    void writeIsoPayload(ISOPayload isoPayload, OutputStream outputStream) throws IOException, FinancialWrappedException;

    ISOMsg readIsoMsg(byte[] isoBytes, ISOPackagerType isoPackagerType) throws IOException, FinancialWrappedException;
    ISOMsg readIsoMsg(InputStream inputStream, ISOPackagerType isoPackagerType) throws IOException, FinancialWrappedException;
    byte[] writeIsoMsg(ISOMsg isoMsg, ISOPackagerType isoPackagerType) throws IOException, FinancialWrappedException;
    void writeIsoMsg(OutputStream outputStream, ISOMsg isoMsg, ISOPackagerType isoPackagerType) throws IOException, FinancialWrappedException;
}
