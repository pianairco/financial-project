package ir.piana.financial.commons.utilities;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class OutputStreamUtility {
    public static void writeBytes(OutputStream outputStream, byte[] bytes) throws IOException {
        outputStream.write(bytes);
    }

    public static int writeNumberBinary(OutputStream outputStream, int number, int howManyBytes, boolean isLSB) throws IOException {
        byte[] lenBytes = new byte[howManyBytes];
        if (isLSB) {
            for (int i = howManyBytes - 1; i >= 0; i--) {
                lenBytes[howManyBytes - i - 1] = (byte) ((number >> ((howManyBytes - i - 1) * 8)) & 0xFF);
            }
        } else {
            for (int i = 0; i < howManyBytes; i++) {
                lenBytes[i] = (byte) ((number >> ((howManyBytes - i - 1) * 8)) & 0xFF);
            }
        }
        outputStream.write(lenBytes);
        return lenBytes.length;
    }

    public static int writeNumberAscii(OutputStream outputStream, int number, int digitOfLength) throws IOException {
        String s = StringUtility.leftPad(String.valueOf(number), '0', digitOfLength);
        byte[] bytes = s.getBytes(StandardCharsets.US_ASCII);
        outputStream.write(bytes);
        return bytes.length;
    }

    public static void writeByBinaryLength(OutputStream outputStream, byte[] payload, int howManyBytes, boolean isLSB) throws IOException {
        if (writeNumberBinary(outputStream, payload.length, howManyBytes, isLSB) != howManyBytes)
            throw new IOException();
        outputStream.write(payload);
    }

    public static void writeByAsciiLength(OutputStream outputStream, byte[] payload, int digitOfLength) throws IOException {
        if (writeNumberAscii(outputStream, payload.length, digitOfLength) != digitOfLength)
            throw new IOException();
        outputStream.write(payload);
    }
}
