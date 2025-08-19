package ir.piana.financial.commons.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtility {
    private static final Logger log = LoggerFactory.getLogger(HashUtility.class);
    private static final MessageDigest MD5_MessageDigest;
    private static final MessageDigest SHA256_MessageDigest;

    static {
        try {
            MD5_MessageDigest = MessageDigest.getInstance("MD5");
            SHA256_MessageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    //ToDo change hex generator
    public static String getMD5Hash(String input) {
        byte[] messageDigest = MD5_MessageDigest.digest(input.getBytes());
        BigInteger number = new BigInteger(1, messageDigest);
        StringBuilder hashtext = new StringBuilder(number.toString(16));
        // Now we need to zero pad it if you actually want the full 32 chars.
        while (hashtext.length() < 32) {
            hashtext.insert(0, "0");
        }
        return hashtext.toString();
    }

    //ToDo change hex generator
    public static String getSHA256Hash(String input) {
        byte[] hashBytes = SHA256_MessageDigest.digest(input.getBytes());
        // Convert byte array to hex string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString().substring(0, 16); // Return first 16 chars
    }
}
