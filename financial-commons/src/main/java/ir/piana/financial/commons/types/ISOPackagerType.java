package ir.piana.financial.commons.types;

import ir.piana.financial.commons.errors.FinancialExceptionType;
import ir.piana.financial.commons.errors.FinancialWrappedException;
import ir.piana.financial.commons.utilities.BytesManipulation;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.packager.ISO87APackager;
import org.jpos.iso.packager.ISO87BPackager;
import org.jpos.iso.packager.ISO93APackager;
import org.jpos.iso.packager.ISO93BPackager;

public enum ISOPackagerType {
    BINARY_V87("binary-v87", (byte) 0x11b, new ISO87BPackager(), "12"),
    ASCII_V87("ascii-v87", (byte) 0x12b, new ISO87APackager(), "12"),
    BINARY_V93("binary-v93", (byte) 0x21b, new ISO93BPackager(), "12"),
    ASCII_V93("ascii-v93", (byte) 0x22b, new ISO93APackager(), "12"),
    BINARY_V2003("binary-v2003", (byte) 0x31b, null, "12"),
    ASCII_V2003("ascii-v2003", (byte) 0x32b, null, "12"),
    SHETAB7_BINARY_V87("shetab-7-binary-v87", (byte) 0x13, Shetab7Version87Fields.getIsoBinaryPackager(), "12"),
    SHETAB7_ASCII_V87("shetab-7-ascii-v87", (byte) 0x14, Shetab7Version87Fields.getIsoAsciiPackager(), "12"),
    SHETAB7_BINARY_V93("shetab-7-binary-v93", (byte) 0x23, Shetab7Version93Fields.getIsoBinaryPackager(), "12"),
    SHETAB7_ASCII_V93("shetab-7-ascii-v93", (byte) 0x24, Shetab7Version93Fields.getIsoAsciiPackager(), "12"),
    ;

    private final String name;
    private final byte byteValue;
    private final ISOPackager isoPackager;
    private final String invalidTransactionCode;

    ISOPackagerType(String name, byte byteValue, ISOPackager isoPackager, String invalidTransactionCode) {
        this.name = name;
        this.byteValue = byteValue;
        this.isoPackager = isoPackager;
        this.invalidTransactionCode = invalidTransactionCode;
    }

    public static ISOPackagerType byName(String name) {
        for (ISOPackagerType value : ISOPackagerType.values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public static ISOPackagerType byByteValue(byte byteValue) {
        for (ISOPackagerType value : ISOPackagerType.values()) {
            if (value.byteValue == byteValue) {
                return value;
            }
        }
        return null;
    }

    public byte getByteValue() {
        return byteValue;
    }

    public byte[] appendToPayload(byte[] payload) {
        return BytesManipulation.appendByteAtFirst(payload, byteValue);
    }

    public ISOPackager getIsoPackager() {
        return isoPackager;
    }

    public ISOMsg unpack(byte[] bytes) throws FinancialWrappedException {
        if (isoPackager == null) throw FinancialExceptionType.ISOError.returnException("isoPackager is null");
        try {
            ISOMsg isoMsg = new ISOMsg();
            int unpack = isoPackager.unpack(isoMsg, bytes);
            isoMsg.setLastCalculatedBytes(bytes);
            return isoMsg;
        } catch (ISOException e) {
            throw FinancialExceptionType.ISOError.returnException("isoMsg have not valid structure!");
        }
    }

    public byte[] pack(ISOMsg isoMsg) throws FinancialWrappedException {
        if (isoPackager == null) throw FinancialExceptionType.ISOError.returnException("isoPackager is null");
        try {
            return isoPackager.pack(isoMsg);
        } catch (ISOException e) {
            throw FinancialExceptionType.ISOError.returnException("isoMsg have not valid structure!");
        }
    }

    public String getInvalidTransactionCode() {
        return invalidTransactionCode;
    }
}
