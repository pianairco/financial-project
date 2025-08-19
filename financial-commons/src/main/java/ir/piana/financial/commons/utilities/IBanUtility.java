package ir.piana.financial.commons.utilities;

import ir.piana.financial.commons.types.IranianBank;

import java.math.BigDecimal;

public class IBanUtility {
    public static String generateIban(String accountNumber, IranianBank iranianBank) {
        if (accountNumber.startsWith("IR")) {
            return accountNumber;
        } else {
            int VAR_CCCD = 182700;
            String VAR_STR_BBAN = iranianBank.getBankId() + StringUtility.leftPad(accountNumber, '0', 19);
            BigDecimal VAR_CHECKNUMBER = new BigDecimal(VAR_STR_BBAN + VAR_CCCD);
            int VAR_Mode = (VAR_CHECKNUMBER.remainder(new BigDecimal(97))).intValue();
            int VAR_CD = 98 - VAR_Mode;
            String VAR_IBAN = "IR" + VAR_CD + VAR_STR_BBAN;
            return VAR_IBAN;
        }
    }
}
