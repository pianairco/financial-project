package ir.piana.financial.commons.iso.channel.impl;

import ir.piana.financial.commons.errors.FinancialExceptionType;
import ir.piana.financial.commons.errors.FinancialWrappedException;
import ir.piana.financial.commons.iso.channel.ISOChannelSerializer;
import ir.piana.financial.commons.iso.channel.ISOPayload;
import ir.piana.financial.commons.iso.channel.configs.ChannelInfo;
import ir.piana.financial.commons.types.ISOChannelType;
import ir.piana.financial.commons.types.ISOHeaderType;
import ir.piana.financial.commons.types.ISOPackagerType;
import ir.piana.financial.commons.utilities.InputStreamUtility;
import ir.piana.financial.commons.utilities.OutputStreamUtility;
import org.jpos.iso.ISOMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class AsciiISOChannelSerializer implements ISOChannelSerializer {
    private final Logger log = LoggerFactory.getLogger(ISOChannelSerializer.class);
    private final ChannelInfo channelInfo;

    public AsciiISOChannelSerializer(ChannelInfo channelInfo) throws FinancialWrappedException {
        if (channelInfo == null || !ISOChannelType.ASCII_CHANNEL.equalsName(channelInfo.getIsoChannelType()))
            throw FinancialExceptionType.ISOError.returnException("channel-info is null or its type is not ascii");
        this.channelInfo = channelInfo;
    }

    @Override
    public byte[] readIsoPayloadBytes(InputStream inputStream) throws IOException {
        int totalLength = InputStreamUtility.readLengthAscii(inputStream,
                channelInfo.getAsciiDetail().getTotalLenDigits());
        if (totalLength == 0)
            return new byte[0];
        return InputStreamUtility.readBytes(inputStream, totalLength);
    }

    @Override
    public ISOPayload readIsoPayload(InputStream inputStream) throws IOException, FinancialWrappedException {
        ISOHeaderType isoHeaderType = ISOHeaderType.byType(channelInfo.getAsciiHeaderDetail().getIsoHeaderType());
        if (isoHeaderType.equals(ISOHeaderType.NOT_SEPARATED_FROM_THE_MESSAGE)) {
            throw FinancialExceptionType.ISOError.returnException("iso header type not support this functionality");
        } else if (isoHeaderType.equalsAny(ISOHeaderType.NONE, ISOHeaderType.NOT_SUPPORTED)) {
            return new ISOPayload(null, readIsoPayloadBytes(inputStream));
        } else {
            int totalLength = InputStreamUtility.readLengthAscii(inputStream,
                    channelInfo.getAsciiDetail().getTotalLenDigits());

            byte[] headerBytes = new byte[0];
            int headerLenDigits = 0;
            if (isoHeaderType.equals(ISOHeaderType.FIX)) {
                headerBytes = InputStreamUtility.readBytes(inputStream, channelInfo.getAsciiHeaderDetail().getBytes());
            } else if (isoHeaderType.equals(ISOHeaderType.VARIABLE)) {
                headerLenDigits = channelInfo.getAsciiHeaderDetail().getLenDigits();
                int headerBytesCount = InputStreamUtility.readLengthAscii(inputStream, headerLenDigits);
                headerBytes = InputStreamUtility.readBytes(inputStream, headerBytesCount);
            }

            byte[] messageBytes = InputStreamUtility.readBytes(inputStream,
                    totalLength - headerLenDigits - headerBytes.length);
            return new ISOPayload(headerBytes, messageBytes);
        }
    }

    @Override
    public void writeIsoPayloadBytesByLength(byte[] payload, OutputStream outputStream) throws IOException {
        OutputStreamUtility.writeByAsciiLength(outputStream,
                payload,
                channelInfo.getAsciiDetail().getTotalLenDigits());
    }

    @Override
    public void writeIsoPayloadBytes(byte[] payload, OutputStream outputStream) throws IOException {
        OutputStreamUtility.writeBytes(outputStream,
                payload);
    }

    @Override
    public void writeIsoPayload(ISOPayload isoPayload, OutputStream outputStream) throws IOException, FinancialWrappedException {
        ISOHeaderType isoHeaderType = ISOHeaderType.byType(channelInfo.getAsciiHeaderDetail().getIsoHeaderType());
        if (isoHeaderType.equals(ISOHeaderType.NOT_SEPARATED_FROM_THE_MESSAGE)) {
            throw FinancialExceptionType.ISOError.returnException("iso header type not support this functionality");
        } else if (isoHeaderType.equalsAny(ISOHeaderType.NONE, ISOHeaderType.NOT_SUPPORTED)) {
            OutputStreamUtility.writeByAsciiLength(outputStream, isoPayload.messageBytes(), channelInfo.getAsciiDetail().getTotalLenDigits());
        } else {
            if (isoHeaderType.equals(ISOHeaderType.FIX)) {
                int totalLen = isoPayload.headerBytes().length + isoPayload.messageBytes().length;
                OutputStreamUtility.writeNumberAscii(outputStream, totalLen, channelInfo.getAsciiDetail().getTotalLenDigits());
                OutputStreamUtility.writeBytes(outputStream, isoPayload.headerBytes());
                OutputStreamUtility.writeBytes(outputStream, isoPayload.messageBytes());
            } else if (isoHeaderType.equals(ISOHeaderType.VARIABLE)) {
                int totalLen = isoPayload.headerBytes().length + isoPayload.messageBytes().length + channelInfo.getAsciiHeaderDetail().getLenDigits();
                OutputStreamUtility.writeNumberAscii(outputStream, totalLen, channelInfo.getAsciiDetail().getTotalLenDigits());
                OutputStreamUtility.writeByAsciiLength(outputStream, isoPayload.headerBytes(), channelInfo.getAsciiHeaderDetail().getLenDigits());
                OutputStreamUtility.writeBytes(outputStream, isoPayload.messageBytes());
            }
        }
    }

    @Override
    public ISOMsg readIsoMsg(byte[] isoBytes, ISOPackagerType isoPackagerType) throws IOException, FinancialWrappedException {
        if (isoBytes == null || isoBytes.length == 0) {
            return null;
        }
        ISOHeaderType isoHeaderType = ISOHeaderType.byType(channelInfo.getAsciiHeaderDetail().getIsoHeaderType());
        if (isoHeaderType.equals(ISOHeaderType.NOT_SEPARATED_FROM_THE_MESSAGE)) {
            throw FinancialExceptionType.ISOError.returnException("iso header type not support this functionality");
        } else if (isoHeaderType.equalsAny(ISOHeaderType.NONE, ISOHeaderType.NOT_SUPPORTED)) {
            return isoPackagerType.unpack(isoBytes);
        } else {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(isoBytes);

            byte[] headerBytes = new byte[0];
            int headerLenDigits = 0;
            if (isoHeaderType.equals(ISOHeaderType.FIX)) {
                headerBytes = InputStreamUtility.readBytes(inputStream, channelInfo.getAsciiHeaderDetail().getBytes());
            } else if (isoHeaderType.equals(ISOHeaderType.VARIABLE)) {
                headerLenDigits = channelInfo.getAsciiHeaderDetail().getLenDigits();
                int headerBytesCount = InputStreamUtility.readLengthAscii(inputStream, headerLenDigits);
                headerBytes = InputStreamUtility.readBytes(inputStream, headerBytesCount);
            }

            byte[] messageBytes = InputStreamUtility.readBytes(inputStream,
                    isoBytes.length - headerLenDigits - headerBytes.length);
            ISOMsg unpack = isoPackagerType.unpack(messageBytes);
            unpack.setHeader(headerBytes);
            return unpack;
        }
    }

    @Override
    public ISOMsg readIsoMsg(InputStream inputStream, ISOPackagerType isoPackagerType) throws IOException, FinancialWrappedException {
        byte[] isoBytes = readIsoPayloadBytes(inputStream);

        if (isoBytes == null || isoBytes.length == 0) {
            return null;
        }
        ISOHeaderType isoHeaderType = ISOHeaderType.byType(channelInfo.getAsciiHeaderDetail().getIsoHeaderType());
        if (isoHeaderType.equals(ISOHeaderType.NOT_SEPARATED_FROM_THE_MESSAGE)) {
            throw FinancialExceptionType.ISOError.returnException("iso header type not support this functionality");
        } else if (isoHeaderType.equalsAny(ISOHeaderType.NONE, ISOHeaderType.NOT_SUPPORTED)) {
            return isoPackagerType.unpack(isoBytes);
        } else {
            ByteArrayInputStream bais = new ByteArrayInputStream(isoBytes);

            byte[] headerBytes = new byte[0];
            int headerLenDigits = 0;
            if (isoHeaderType.equals(ISOHeaderType.FIX)) {
                headerBytes = InputStreamUtility.readBytes(bais, channelInfo.getAsciiHeaderDetail().getBytes());
            } else if (isoHeaderType.equals(ISOHeaderType.VARIABLE)) {
                headerLenDigits = channelInfo.getAsciiHeaderDetail().getLenDigits();
                int headerBytesCount = InputStreamUtility.readLengthAscii(bais, headerLenDigits);
                headerBytes = InputStreamUtility.readBytes(bais, headerBytesCount);
            }

            byte[] messageBytes = InputStreamUtility.readBytes(bais,
                    isoBytes.length - headerLenDigits - headerBytes.length);
            ISOMsg unpack = isoPackagerType.unpack(messageBytes);
            unpack.setHeader(headerBytes);
            return unpack;
        }
    }

    @Override
    public byte[] writeIsoMsg(ISOMsg isoMsg, ISOPackagerType isoPackagerType) throws IOException, FinancialWrappedException {
        ISOHeaderType isoHeaderType = ISOHeaderType.byType(channelInfo.getAsciiHeaderDetail().getIsoHeaderType());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (isoHeaderType.equals(ISOHeaderType.NOT_SEPARATED_FROM_THE_MESSAGE)) {
            throw FinancialExceptionType.ISOError.returnException("iso header type not support this functionality");
        } else if (isoHeaderType.equalsAny(ISOHeaderType.NONE, ISOHeaderType.NOT_SUPPORTED)) {
            if (isoMsg.getHeader() != null) {
                log.info("message has header but don't write");
            }
            byte[] message = isoPackagerType.pack(isoMsg);
            int totalLen = message.length;
            OutputStreamUtility.writeNumberAscii(outputStream, totalLen, channelInfo.getAsciiDetail().getTotalLenDigits());
            OutputStreamUtility.writeBytes(outputStream, message);
        } else {
            byte[] header = isoMsg.getHeader();
            if (header == null) {
                throw FinancialExceptionType.ISOError.returnException("iso header type not support this functionality");
            }

            byte[] message = isoPackagerType.pack(isoMsg);


            if (isoHeaderType.equals(ISOHeaderType.FIX)) {
                int totalLen = header.length + message.length;
                OutputStreamUtility.writeNumberAscii(outputStream, totalLen, channelInfo.getAsciiDetail().getTotalLenDigits());
                OutputStreamUtility.writeBytes(outputStream, header);
                OutputStreamUtility.writeBytes(outputStream, message);
            } else if (isoHeaderType.equals(ISOHeaderType.VARIABLE)) {
                int totalLen = header.length + message.length + channelInfo.getAsciiHeaderDetail().getLenDigits();
                OutputStreamUtility.writeNumberAscii(outputStream, totalLen, channelInfo.getAsciiDetail().getTotalLenDigits());
                OutputStreamUtility.writeByAsciiLength(outputStream, header, channelInfo.getAsciiHeaderDetail().getLenDigits());
                OutputStreamUtility.writeBytes(outputStream, message);
            }
        }
        return outputStream.toByteArray();
    }

    @Override
    public void writeIsoMsg(OutputStream outputStream, ISOMsg isoMsg, ISOPackagerType isoPackagerType) throws IOException, FinancialWrappedException {
        ISOHeaderType isoHeaderType = ISOHeaderType.byType(channelInfo.getAsciiHeaderDetail().getIsoHeaderType());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (isoHeaderType.equals(ISOHeaderType.NOT_SEPARATED_FROM_THE_MESSAGE)) {
            throw FinancialExceptionType.ISOError.returnException("iso header type not support this functionality");
        } else if (isoHeaderType.equalsAny(ISOHeaderType.NONE, ISOHeaderType.NOT_SUPPORTED)) {
            if (isoMsg.getHeader() != null) {
                log.info("message has header but don't write");
            }
            byte[] message = isoPackagerType.pack(isoMsg);
            int totalLen = message.length;
            OutputStreamUtility.writeNumberAscii(baos, totalLen, channelInfo.getAsciiDetail().getTotalLenDigits());
            OutputStreamUtility.writeBytes(baos, message);
        } else {
            byte[] header = isoMsg.getHeader();
            if (header == null) {
                throw FinancialExceptionType.ISOError.returnException("iso header type not support this functionality");
            }

            byte[] message = isoPackagerType.pack(isoMsg);


            if (isoHeaderType.equals(ISOHeaderType.FIX)) {
                int totalLen = header.length + message.length;
                OutputStreamUtility.writeNumberAscii(baos, totalLen, channelInfo.getAsciiDetail().getTotalLenDigits());
                OutputStreamUtility.writeBytes(baos, header);
                OutputStreamUtility.writeBytes(baos, message);
            } else if (isoHeaderType.equals(ISOHeaderType.VARIABLE)) {
                int totalLen = header.length + message.length + channelInfo.getAsciiHeaderDetail().getLenDigits();
                OutputStreamUtility.writeNumberAscii(baos, totalLen, channelInfo.getAsciiDetail().getTotalLenDigits());
                OutputStreamUtility.writeByAsciiLength(baos, header, channelInfo.getAsciiHeaderDetail().getLenDigits());
                OutputStreamUtility.writeBytes(baos, message);
            }
        }
        writeIsoPayloadBytesByLength(baos.toByteArray(), outputStream);
    }
}
