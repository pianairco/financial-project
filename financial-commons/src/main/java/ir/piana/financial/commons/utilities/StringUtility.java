package ir.piana.financial.commons.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtility {
    private static final Logger log = LoggerFactory.getLogger(StringUtility.class);

    public static String leftPad(String input, char padChar, int length) {
        if (input == null) {
            input = "";
        }
        if (input.length() >= length) {
            return input;
        }
        StringBuilder sb = new StringBuilder();
        int padsNeeded = length - input.length();
        for (int i = 0; i < padsNeeded; i++) {
            sb.append(padChar);
        }
        sb.append(input);
        return sb.toString();
    }

    /*public static String leftPad(String value, char padCharacter, int length) {
        String paddedValue = value;
        try {
            String format = "%" + length + "s";
            paddedValue = String.format(format, value).replace(' ', padCharacter);
        } catch (Exception e) {
            log.error(Stringer.concat("lPad", "Value:", value, " PadCharacter:", String.valueOf(padCharacter),
                    " Length:", String.valueOf(length), "Message:'", e.getMessage(), "'"));
        }
        return paddedValue;
    }*/

    public static String rightPad(String input, char padChar, int length) {
        if (input == null) {
            input = "";
        }
        if (input.length() >= length) {
            return input;
        }
        StringBuilder sb = new StringBuilder(input);
        while (sb.length() < length) {
            sb.append(padChar);
        }
        return sb.toString();
    }

    public static Boolean checkStringValidChar(String str) {
        Boolean res = false;
        String regex = "^[a-zA-Z0-9 ]*$";
        if (str.matches(regex)) {
            res = true;
        }
        return res;
    }
}
