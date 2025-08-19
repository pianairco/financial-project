package ir.piana.financial.cardmanagement.dto;

public class IssueCardDto {
    /**
     * Key Check Value, It is a checksum of a cryptographic key,
     * used to verify the integrity of the key
     */
    private String kcv;

    /**
     * PIN Verification Key Indicator, is a single-digit index that identifies which PIN Verification Key (PVK)
     * to use when calculating the PIN Verification Value (PVV)
     */
    private String pvki;

    /**
     * PIN Verification Value, is a security code associated with a card's PIN,
     * used to verify the cardholder's PIN during transactions
     */
    private String pvv;

    private String pin1;

    private String pin2;

    /**
     * security code found on credit and debit cards, used to verify transactions
     * CVV1 is embedded in the magnetic stripe
     * used for in-person transactions at POS terminals or ATMs
     */
    private String cvv1;

    /**
     * security code found on credit and debit cards, used to verify transactions
     * CVV2 is a visible, three or four-digit code on the back of the card (or front for Amex)
     * used for online and over-the-phone purchases
     */
    private String cvv2;

    private String mac;
}
