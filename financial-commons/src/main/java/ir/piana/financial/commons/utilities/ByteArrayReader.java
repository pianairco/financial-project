package ir.piana.financial.commons.utilities;

import org.jpos.iso.types.ByteArrangedType;

public class ByteArrayReader {
    public static byte[] read(byte[] bytes, int from, int length) {
        byte[] b = new  byte[length];
        System.arraycopy(bytes, from, b, 0, length);
        return b;
    }

    public static String readAsString(byte[] bytes, int from, int length) {
        byte[] b = new  byte[length];
        System.arraycopy(bytes, from, b, 0, length);
        return new String(b);
    }

    public static short readAsShort(byte[] bytes, int from, ByteArrangedType byteArrangedType) {
        byte[] b = new  byte[2];
        System.arraycopy(bytes, from, b, 0, 2);
        return readAsShort(b, byteArrangedType);
    }

    public static int readAsInt(byte[] bytes, int from, ByteArrangedType byteArrangedType) {
        byte[] b = new  byte[4];
        System.arraycopy(bytes, from, b, 0, 4);
        return readAsInt(b, byteArrangedType);
    }

    public static long readAsLong(byte[] bytes, int from, ByteArrangedType byteArrangedType) {
        byte[] b = new  byte[8];
        System.arraycopy(bytes, from, b, 0, 8);
        return readAsLong(b, byteArrangedType);
    }

    public static short readAsShort(byte[] bytes, ByteArrangedType byteArrangedType) {
        short num = 0;
        if (byteArrangedType == ByteArrangedType.LSB) {
            for (int i = bytes.length - 1; i >= 0; i--) {
                num |= (short) (bytes[i] << (i * 8));
            }
        } else {
            for (int i = 0; i < bytes.length; i++) {
                num |= (short) (bytes[i] << ((bytes.length - i - 1) * 8));
            }
        }
        return num;
    }

    public static int readAsInt(byte[] bytes, ByteArrangedType byteArrangedType) {
        int num = 0;
        if (byteArrangedType == ByteArrangedType.LSB) {
            for (int i = bytes.length - 1; i >= 0; i--) {
                num |= bytes[i] << (i * 8);
            }
        } else {
            for (int i = 0; i < bytes.length; i++) {
                num |= bytes[i] << ((bytes.length - i - 1) * 8);
            }
        }
        return num;
    }

    public static long readAsLong(byte[] bytes, ByteArrangedType byteArrangedType) {
        long num = 0;
        if (byteArrangedType == ByteArrangedType.LSB) {
            for (int i = bytes.length - 1; i >= 0; i--) {
                num |= (long) bytes[i] << (i * 8);
            }
        } else {
            for (int i = 0; i < bytes.length; i++) {
                num |= (long) bytes[i] << ((bytes.length - i - 1) * 8);
            }
        }
        return num;
    }
}
