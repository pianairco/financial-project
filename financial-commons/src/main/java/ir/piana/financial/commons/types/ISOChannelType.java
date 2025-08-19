package ir.piana.financial.commons.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ISOChannelType {
    BINARY_CHANNEL(1, "binary-channel", "binary"),
    ASCII_CHANNEL(2, "ascii-channel", "ascii"),
    ;

    private Logger log = LoggerFactory.getLogger(ISOChannelType.class);

    private final int value;
    private final List<String> names;

    ISOChannelType(int value, String... names) {
        this.names = Arrays.stream(names).map(String::toLowerCase).collect(Collectors.toList());
        this.value = value;
    }

    public static ISOChannelType byName(String name) {
        String lowerCase = name.toLowerCase();
        for (ISOChannelType value : ISOChannelType.values()) {
            if (value.names.contains(lowerCase)) {
                return value;
            }
        }
        return null;
    }

    public String getName() {
        return names.getFirst();
    }

    public static ISOChannelType byByteValue(int byteValue) {
        for (ISOChannelType value : ISOChannelType.values()) {
            if (value.value == byteValue) {
                return value;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public boolean equalsName(String otherName) {
        return this.names.contains(otherName.toLowerCase());
    }

    /*public void writeIsoPayload(byte[] payload, OutputStream outputStream) throws IOException {
        serializer.writeIsoPayload(payload, outputStream);
    }

    public byte[] readIsoPayload(InputStream inputStream) throws IOException {
        byte[] bytes = serializer.readIsoPayload(inputStream);
        log.info("serialized: {}", HexUtility.bytesToHex(bytes));
        return bytes;
    }*/
}
