package ir.piana.financial.commons.utilities;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamUtility {
    public static byte[] readBytes(InputStream inputStream, int len) throws IOException {
        byte[] bytes = new byte[len];
        int read = inputStream.read(bytes, 0, len);
        if (read == -1)
            return null;
        if (read < len) {
            return BytesManipulation.getSubArray(bytes, 0, read);
        }
        return bytes;
    }

    public static int readInt(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[4];
        int read = inputStream.read(bytes, 0, 4);
        if (read > 0 && read < 4) {
            bytes = BytesManipulation.leftPadBytes(bytes, (byte) 0x00, 4 - read);
        }
        String s = HexUtility.bytesToHex(bytes);
        return bytes[0] >> 24 | bytes[1] >> 16 | bytes[2] >> 8 | bytes[0];
//        return bytes[3] | bytes[2] | bytes[1] | bytes[0];
    }

    public static int readLengthBinary(InputStream inputStream, int howManyBytes, boolean isLSB) throws IOException {
        byte[] bytes = InputStreamUtility.readBytes(inputStream, howManyBytes);
        int len = 0;
        if (isLSB) {
            for (int i = howManyBytes - 1; i >= 0; i--) {
                len |= bytes[i] << (i * 8);
            }
        } else {
            for (int i = 0; i < howManyBytes; i++) {
                len |= bytes[i] << ((howManyBytes - i - 1) * 8);
            }
        }
        return len;
    }

    public static int readLengthAscii(InputStream inputStream, int digitOfLength) throws IOException {
        byte[] bytes = InputStreamUtility.readBytes(inputStream, digitOfLength);
        if (bytes == null)
            throw new EOFException();
        return Integer.parseInt(new String(bytes).replaceFirst("^0+(?!$)", ""));
    }
}
