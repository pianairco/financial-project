package ir.piana.financial.commons.utilities;

public class LuhnUtility {
    public static int calculateCheckDigit(long number) {
        int sum = 0;
        boolean alternate = true;

        while (number > 0) {
            int digit = (int)(number % 10);
            number /= 10;

            if (alternate) {
                digit *= 2;
                if (digit > 9) digit -= 9;
            }

            sum += digit;
            alternate = !alternate;
        }

        return (10 - (sum % 10)) % 10;
    }

    public static char calculateCheckDigit(String number) {
        int sum = 0;
        boolean alternate = true;

        // Process digits from right to left (excluding check digit)
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = number.charAt(i) - '0';
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }
        int mod = sum % 10;
        int checkDigit = (10 - mod) % 10;
        return (char) ('0' + checkDigit);
    }

}
