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
import org.jpos.iso.types.ByteArrangedType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class BinaryISOChannelSerializer implements ISOChannelSerializer {
    private final Logger log =  LoggerFactory.getLogger(ISOChannelSerializer.class);
    private final ChannelInfo channelInfo;

    public BinaryISOChannelSerializer(ChannelInfo channelInfo) throws FinancialWrappedException {
        if (channelInfo == null || !ISOChannelType.BINARY_CHANNEL.equalsName(channelInfo.getIsoChannelType()))
            throw FinancialExceptionType.ISOError.returnException("channel-info is null or its type is not binary");
        this.channelInfo = channelInfo;
    }

    @Override
    public byte[] readIsoPayloadBytes(InputStream inputStream) throws IOException {
        int totalLength = InputStreamUtility.readLengthBinary(inputStream,
                channelInfo.getBinaryDetail().getTotalLenBytes(),
                ByteArrangedType.LSB.equals(channelInfo.getBinaryDetail().getLenBytesArranged()));

        return InputStreamUtility.readBytes(inputStream, totalLength);
    }

    @Override
    public ISOPayload readIsoPayload(InputStream inputStream) throws IOException, FinancialWrappedException {
        ISOHeaderType isoHeaderType = ISOHeaderType.byType(channelInfo.getBinaryHeaderDetail().getIsoHeaderType());
        if (isoHeaderType.equalsAny(ISOHeaderType.NOT_SEPARATED_FROM_THE_MESSAGE)) {
            throw FinancialExceptionType.ISOError.returnException("iso header type not support this functionality");
        } else if (isoHeaderType.equalsAny(ISOHeaderType.NONE, ISOHeaderType.NOT_SUPPORTED)) {
            return new ISOPayload(null, readIsoPayloadBytes(inputStream));
        } else {
            int totalLength = InputStreamUtility.readLengthBinary(inputStream,
                    channelInfo.getBinaryDetail().getTotalLenBytes(),
                    ByteArrangedType.LSB.equals(channelInfo.getBinaryDetail().getLenBytesArranged()));

            byte[] headerBytes = new byte[0];
            int headerLenBytes = 0;
            if (isoHeaderType.equals(ISOHeaderType.FIX)) {
                headerBytes = InputStreamUtility.readBytes(
                        inputStream, channelInfo.getBinaryHeaderDetail().getBytes());
            } else if (isoHeaderType.equals(ISOHeaderType.VARIABLE)) {
                headerLenBytes = channelInfo.getBinaryHeaderDetail().getLenBytes();
                int headerBytesCounts = InputStreamUtility.readLengthBinary(
                        inputStream, headerLenBytes,
                        ByteArrangedType.LSB.equals(channelInfo.getBinaryDetail().getLenBytesArranged()));
                headerBytes = InputStreamUtility.readBytes(inputStream, headerBytesCounts);
            }

            byte[] messageBytes = InputStreamUtility.readBytes(
                    inputStream, totalLength - headerLenBytes - headerBytes.length);
            return new ISOPayload(headerBytes, messageBytes);
        }
    }

    @Override
    public void writeIsoPayloadBytesByLength(byte[] payload, OutputStream outputStream) throws IOException {
        OutputStreamUtility.writeByBinaryLength(outputStream,
                payload,
                channelInfo.getBinaryDetail().getTotalLenBytes(),
                ByteArrangedType.LSB.equals(channelInfo.getBinaryDetail().getLenBytesArranged()));
    }

    @Override
    public void writeIsoPayloadBytes(byte[] payload, OutputStream outputStream) throws IOException {
        OutputStreamUtility.writeBytes(outputStream, payload);
    }

    @Override
    public void writeIsoPayload(ISOPayload isoPayload, OutputStream outputStream) throws IOException, FinancialWrappedException {
        ISOHeaderType isoHeaderType = ISOHeaderType.byType(channelInfo.getBinaryHeaderDetail().getIsoHeaderType());
        if (isoHeaderType.equals(ISOHeaderType.NOT_SEPARATED_FROM_THE_MESSAGE)) {
            throw FinancialExceptionType.ISOError.returnException("iso header type not support this functionality");
        } else if (isoHeaderType.equalsAny(ISOHeaderType.NONE, ISOHeaderType.NOT_SUPPORTED)) {
            OutputStreamUtility.writeByBinaryLength(outputStream, isoPayload.messageBytes(),
                    channelInfo.getBinaryDetail().getTotalLenBytes(),
                    ByteArrangedType.LSB.equals(channelInfo.getBinaryDetail().getLenBytesArranged()));
        } else {
            if (isoHeaderType.equals(ISOHeaderType.FIX)) {
                int totalLen = isoPayload.headerBytes().length + isoPayload.messageBytes().length;
                OutputStreamUtility.writeNumberBinary(outputStream, totalLen,
                        channelInfo.getBinaryHeaderDetail().getBytes(),
                        ByteArrangedType.LSB.equals(channelInfo.getBinaryDetail().getLenBytesArranged()));
                OutputStreamUtility.writeBytes(outputStream, isoPayload.headerBytes());
                OutputStreamUtility.writeBytes(outputStream, isoPayload.messageBytes());
            } else if (isoHeaderType.equals(ISOHeaderType.VARIABLE)) {
                int totalLen = isoPayload.headerBytes().length +
                        isoPayload.messageBytes().length +
                        channelInfo.getBinaryHeaderDetail().getLenBytes();
                OutputStreamUtility.writeNumberBinary(outputStream, totalLen,
                        channelInfo.getBinaryDetail().getTotalLenBytes(),
                        ByteArrangedType.LSB.equals(channelInfo.getBinaryDetail().getLenBytesArranged()));
                OutputStreamUtility.writeByBinaryLength(outputStream, isoPayload.headerBytes(),
                        channelInfo.getBinaryDetail().getTotalLenBytes(),
                        ByteArrangedType.LSB.equals(channelInfo.getBinaryDetail().getLenBytesArranged()));
                OutputStreamUtility.writeBytes(outputStream, isoPayload.messageBytes());
            }
        }
    }

    @Override
    public ISOMsg readIsoMsg(byte[] isoBytes, ISOPackagerType isoPackagerType) throws IOException, FinancialWrappedException {
        ISOHeaderType isoHeaderType = ISOHeaderType.byType(channelInfo.getBinaryHeaderDetail().getIsoHeaderType());
        if (isoHeaderType.equalsAny(ISOHeaderType.NOT_SEPARATED_FROM_THE_MESSAGE)) {
            throw FinancialExceptionType.ISOError.returnException("iso header type not support this functionality");
        } else if (isoHeaderType.equalsAny(ISOHeaderType.NONE, ISOHeaderType.NOT_SUPPORTED)) {
            return isoPackagerType.unpack(isoBytes);
        } else {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(isoBytes);

            byte[] headerBytes = new byte[0];
            int headerLenBytes = 0;
            if (isoHeaderType.equals(ISOHeaderType.FIX)) {
                headerBytes = InputStreamUtility.readBytes(
                        inputStream, channelInfo.getBinaryHeaderDetail().getBytes());
            } else if (isoHeaderType.equals(ISOHeaderType.VARIABLE)) {
                headerLenBytes = channelInfo.getBinaryHeaderDetail().getLenBytes();
                int headerBytesCounts = InputStreamUtility.readLengthBinary(
                        inputStream, headerLenBytes,
                        ByteArrangedType.LSB.equals(channelInfo.getBinaryDetail().getLenBytesArranged()));
                headerBytes = InputStreamUtility.readBytes(inputStream, headerBytesCounts);
            }

            byte[] messageBytes = InputStreamUtility.readBytes(
                    inputStream, isoBytes.length - headerLenBytes - headerBytes.length);
            ISOMsg unpack = isoPackagerType.unpack(messageBytes);
            unpack.setHeader(headerBytes);
            return unpack;
        }
    }

    @Override
    public ISOMsg readIsoMsg(InputStream inputStream, ISOPackagerType isoPackagerType) throws IOException, FinancialWrappedException {
        //ToDo implementation
        return null;
    }

    @Override
    public byte[] writeIsoMsg(ISOMsg isoMsg, ISOPackagerType isoPackagerType) throws IOException, FinancialWrappedException {
        ISOHeaderType isoHeaderType = ISOHeaderType.byType(channelInfo.getBinaryHeaderDetail().getIsoHeaderType());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (isoHeaderType.equals(ISOHeaderType.NOT_SEPARATED_FROM_THE_MESSAGE)) {
            throw FinancialExceptionType.ISOError.returnException("iso header type not support this functionality");
        } else if (isoHeaderType.equalsAny(ISOHeaderType.NONE, ISOHeaderType.NOT_SUPPORTED)) {
            if (isoMsg.getHeader() != null) {
                log.info("message has header but don't write");
            }
            byte[] message = isoPackagerType.pack(isoMsg);
            int totalLen = message.length;
            OutputStreamUtility.writeNumberBinary(outputStream, totalLen,
                    channelInfo.getBinaryDetail().getTotalLenBytes(),
                    ByteArrangedType.LSB.equals(channelInfo.getBinaryDetail().getLenBytesArranged()));
            OutputStreamUtility.writeBytes(outputStream, message);
        } else {
            byte[] header = isoMsg.getHeader();
            if (header == null) {
                throw FinancialExceptionType.ISOError.returnException("iso header type not support this functionality");
            }

            byte[] message = isoPackagerType.pack(isoMsg);

            if (isoHeaderType.equals(ISOHeaderType.FIX)) {
                int totalLen = header.length + message.length;
                OutputStreamUtility.writeNumberBinary(outputStream, totalLen,
                        channelInfo.getBinaryDetail().getTotalLenBytes(),
                        ByteArrangedType.LSB.equals(channelInfo.getBinaryDetail().getLenBytesArranged()));
                OutputStreamUtility.writeBytes(outputStream, header);
                OutputStreamUtility.writeBytes(outputStream, message);
            } else if (isoHeaderType.equals(ISOHeaderType.VARIABLE)) {
                int totalLen = header.length + message.length + channelInfo.getBinaryHeaderDetail().getLenBytes();
                OutputStreamUtility.writeNumberBinary(outputStream, totalLen,
                        channelInfo.getBinaryDetail().getTotalLenBytes(),
                        ByteArrangedType.LSB.equals(channelInfo.getBinaryDetail().getLenBytesArranged()));
                OutputStreamUtility.writeByBinaryLength(outputStream, header,
                        channelInfo.getBinaryDetail().getTotalLenBytes(),
                        ByteArrangedType.LSB.equals(channelInfo.getBinaryDetail().getLenBytesArranged()));
                OutputStreamUtility.writeBytes(outputStream, message);
            }
        }
        return outputStream.toByteArray();
    }

    @Override
    public void writeIsoMsg(OutputStream outputStream, ISOMsg isoMsg, ISOPackagerType isoPackagerType) throws IOException, FinancialWrappedException {
        //ToDo implementation
    }
}
