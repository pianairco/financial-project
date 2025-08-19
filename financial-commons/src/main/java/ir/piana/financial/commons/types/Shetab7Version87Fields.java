package ir.piana.financial.commons.types;

import org.jpos.iso.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public enum Shetab7Version87Fields implements ISOFieldType {
    MTI(0, new IFB_NUMERIC(4, true), new IFA_NUMERIC(4), "mti"),
    B1_EXTRA_BITMAP(1, new IFB_BITMAP(16), new IFA_BITMAP(16), "extra-bitmap"),
    B2_PAN(2, new IFB_LLNUM(19, false), new IFA_LLNUM(19), "pan", "primary-account-number"),
    B3_PROCESSING_CODE(3, new IFB_NUMERIC(6), new IFA_NUMERIC(6), "processing-code"),
    B4_AMOUNT_TRANSACTION(4, new IFB_NUMERIC(12), new IFA_NUMERIC(12), "amount-transaction"),
    B6_AMOUNT_CARDHOLDER_BILLING(6, new IFB_NUMERIC(12), new IFA_NUMERIC(12), "amount-cardholder-billing"),
    B7_DATE_AND_TIME_TRANSMISSION(7, new IFB_NUMERIC(10), new IFA_NUMERIC(10), "date-and-time-transmission"),
    B10_CONVERSION_RATE_CARDHOLDER_BILLING(10, new IFB_NUMERIC(8), new IFA_NUMERIC(8), "conversion-rate-cardholder-billing"),
    B11_STAN(11, new IFB_NUMERIC(6), new IFA_NUMERIC(6), "stan", "system-trace-audit-number"),
    B12_TIME_LOCAL_TRANSACTION(12, new IFB_NUMERIC(6), new IFA_NUMERIC(6), "time-local-transaction"),
    B13_DATE_LOCAL_TRANSACTION(13, new IFB_NUMERIC(4), new IFA_NUMERIC(4), "date-local-transaction"),
    B14_DATE_EXPIRATION(14, new IFB_NUMERIC(4), new IFA_NUMERIC(4), "date-expiration"),
    B15_DATE_SETTLEMENT(15, new IFB_NUMERIC(4), new IFA_NUMERIC(4), "date-settlement"),
    B17_DATE_CAPTURE(17, new IFB_NUMERIC(4), new IFA_NUMERIC(4), "date-capture"),
    B18_MERCHANT_CATEGORY_CODE(18, new IFB_NUMERIC(4), new IFA_NUMERIC(4), "merchant-category-code", "business-type"),
    B19_ACQUIRE_COUNTRY_CODE(19, new IFB_NUMERIC(3), new IFA_NUMERIC(3), "acquire-country-code"),
    B22_POINT_OF_SERVICE_ENTRY_MODE(22, new IFB_NUMERIC(3), new IFA_NUMERIC(3), "point-of-service-entry-mode"),
    B25_POSCC(25, new IFB_NUMERIC(2), new IFA_NUMERIC(2), "point-of-service-condition-code"),
    B26_POSPCC(26, new IFB_NUMERIC(2), new IFA_NUMERIC(2), "point-of-service-pin-capture-code"),
    B32_ACQ_CODE(32, new IFB_LLNUM(11, false), new IFA_LLNUM(11), "acquiring-institution-identification-code"),
    B33_FWD_CODE(33, new IFB_LLNUM(11, false), new IFA_LLNUM(11), "forwarding-institution-identification-code"),
    B35_TRACK2(35, new IFB_LLNUM(37, false), new IFA_LLNUM(37), "track-2"),
    B37_RRN(37, new IF_CHAR(12), new IF_CHAR(12), "retrieval-reference-number"),
    B38_AUTHORIZATION_IDENTIFICATION_RESPONSE(38, new IF_CHAR(6), new IF_CHAR(6), "authorization-identification-response"),
    B39_RESPONSE_CODE(39, new IF_CHAR(2), new IF_CHAR(2), "response-code"),
    B41_CARD_ACCEPTOR_TERMINAL_IDENTIFICATION_CODE(41, new IF_CHAR(8), new IF_CHAR(8), "card-acceptor-terminal-identification-code"),
    B42_CARD_ACCEPTOR_IDENTIFICATION_CODE(42, new IF_CHAR(15), new IF_CHAR(15), "card-acceptor-identification-code"),
    B43_CARD_ACCEPTOR_NAME_LOCATION(43, new IF_CHAR(40), new IF_CHAR(40), "card-acceptor-name-location"),
    B44_ADDITIONAL_RESPONSE_DATA(44, new IFB_LLCHAR(25), new IFA_LLCHAR(25), "additional-response-data"),
    B48_ADDITIONAL_PRIVATE_DATA(48, new IFB_LLLCHAR(999), new IFA_LLLCHAR(999), "additional-private-data"),
    B49_TRANSACTION_CURRENCY_CODE(49, new IF_CHAR(3), new IF_CHAR(3), "transaction-currency-code"),
    B50_SETTLEMENT_CURRENCY_CODE(50, new IF_CHAR(3), new IF_CHAR(3), "settlement-currency-code"),
    B51_CARDHOLDER_BILLING_CURRENCY_CODE(51, new IF_CHAR(3), new IF_CHAR(3), "cardholder-billing-currency-code"),
    B52_PIN(52, new IFB_BINARY(8), new IFA_BINARY(8), "pin", "personal-identification-number"),
    B53_SECURITY_RELATED_CONTROL_INFORMATION(53, new IFB_NUMERIC(16), new IFA_NUMERIC(16), "security-related-control-information"),
    B54_AMOUNTS_ADDITIONAL(54, new IFB_LLLCHAR(120), new IFA_LLLCHAR(120), "amounts-additional"),
    B55_ICC_DATA(55, new IFB_LLLBINARY(255), new IFA_LLLBINARY(255), true, "icc-data"),
    B59_TRANSPORT_DATA(59, new IFB_LLLCHAR(999), new IFA_LLLCHAR(999), true, "transport-data"),
    B60_SECURITY_DATA(60, new IFB_LLLCHAR(999), new IFA_LLLCHAR(999), true, "security-data"),
    B61_IBAN_INFO(61, new IFB_LLLCHAR(999), new IFA_LLLCHAR(999), true, "iban-info"),
    B62_TRANSACTION_CODING(62, new IFB_LLLCHAR(999), new IFA_LLLCHAR(999), true, "transaction-coding"),
    B64_MAC(64, new IFB_BINARY(8), new IFA_BINARY(8), "mac", "message-authentication-code"),
    B66_SETTLEMENT_CODE(66, new IFB_NUMERIC(1), new IFA_NUMERIC(1), "settlement-code"),
    B70_NETWORK_MANAGEMENT_INFORMATION_CODE(70, new IFB_NUMERIC(3), new IFA_NUMERIC(3), "network-management-information-code"),
    B74_CREDITS_NUMBER(74, new IFB_NUMERIC(10), new IFA_NUMERIC(10), "credits-number"),
    B75_CREDITS_REVERSAL_NUMBER(75, new IFB_NUMERIC(10), new IFA_NUMERIC(10), "credits-reversal-number"),
    B76_DEBITS_NUMBER(76, new IFB_NUMERIC(10), new IFA_NUMERIC(10), "debits-number"),
    B77_DEBITS_REVERSAL_NUMBER(77, new IFB_NUMERIC(10), new IFA_NUMERIC(10), "debits-reversal-number"),
    B78_TRANSFER_NUMBER(78, new IFB_NUMERIC(10), new IFA_NUMERIC(10), "transfer-number"),
    B79_TRANSFER_REVERSAL_NUMBER(79, new IFB_NUMERIC(10), new IFA_NUMERIC(10), "transfer-reversal-number"),
    B80_INQUIRIES_NUMBER(80, new IFB_NUMERIC(10), new IFA_NUMERIC(10), "inquiries-number"),
    B81_AUTHORIZATIONS_NUMBER(81, new IFB_NUMERIC(10), new IFA_NUMERIC(10), "authorizations-number"),
    B82_CREDITS_PROCESSING_FEE_AMOUNT(82, new IFB_NUMERIC(12), new IFA_NUMERIC(12), "credits-processing-fee-amount"),
    B83_CREDITS_TRANSACTION_FEE_AMOUNT(83, new IFB_NUMERIC(12), new IFA_NUMERIC(12), "credits-transaction-fee-amount"),
    B84_DEBITS_PROCESSING_FEE_AMOUNT(84, new IFB_NUMERIC(12), new IFA_NUMERIC(12), "debits-processing-fee-amount"),
    B85_DEBITS_TRANSACTION_FEE_AMOUNT(85, new IFB_NUMERIC(12), new IFA_NUMERIC(12), "debits-transaction-fee-amount"),
    B86_CREDITS_AMOUNT(86, new IFB_NUMERIC(12), new IFA_NUMERIC(12), "credits-amount"),
    B87_CREDITS_REVERSAL_AMOUNT(87, new IFB_NUMERIC(12), new IFA_NUMERIC(12), "credits-reversal-amount"),
    B88_DEBITS_AMOUNT(88, new IFB_NUMERIC(12), new IFA_NUMERIC(12), "debits-amount"),
    B89_DEBITS_REVERSAL_AMOUNT(89, new IFB_NUMERIC(12), new IFA_NUMERIC(12), "debits-reversal-amount"),
    B90_ORIGINAL_DATA_ELEMENT(90, new IFB_NUMERIC(42), new IFA_NUMERIC(42), "original-data-element"),
    B95_REPLACEMENT_AMOUNT(95, new IF_CHAR(42), new IF_CHAR(42), "replacement-amount"),
    B96_MESSAGE_SECURITY_CODE(96, new IFB_BINARY(8), new IFA_BINARY(8), "message-security-code"),
    B97_AMOUNT_NET_RECONCILIATION(97, new IFB_AMOUNT(16, true), new IFA_AMOUNT(16), "amount-net-reconciliation"),
    B99_SETTLEMENT_INSTITUTION_IDENTIFICATION_CODE(99, new IFB_LLNUM(11, false), new IFA_LLNUM(11), "settlement-institution-identification-code"),
    B100_RECEIVING_INSTITUTION_IDENTIFICATION_CODE(100, new IFB_LLNUM(11, false), new IFA_LLNUM(11), "receiving-institution-identification-code"),
    B102_ACCOUNT_IDENTIFICATION_1(102, new IFB_LLCHAR(28), new IFA_LLCHAR(28), "account-identification-1"),
    B120_ADDITIONAL_DATA(120, new IFB_LLLCHAR(999), new IFA_LLLCHAR(999), true, "additional-data"),
    B124_SETTLEMENT_DATA(124, new IFB_LLLCHAR(999), new IFA_LLLCHAR(999), true, "settlement-data"),
    B128_MAC(128, new IFB_BINARY(8), new IFA_BINARY(8), "mac", "message-authentication-number"),
    ;

    private final int bitNumber;
    private final ISOFieldPackager isoBinaryFieldPackager;
    private final ISOFieldPackager isoAsciiFieldPackager;
    private final List<String> bitNames;
    private final boolean shetabUsed;

    private static ISOBasePackager isoBinaryPackager;
    private static ISOBasePackager isoAsciiPackager;

    Shetab7Version87Fields(int bitNumber, ISOFieldPackager isoBinaryFieldPackager, ISOFieldPackager isoAsciiFieldPackager, boolean shetabUsed, String... bitName) {
        this.bitNumber = bitNumber;
        this.isoBinaryFieldPackager = isoBinaryFieldPackager;
        this.isoAsciiFieldPackager = isoAsciiFieldPackager;
        this.shetabUsed = shetabUsed;
        this.bitNames = Arrays.asList(bitName);
    }

    Shetab7Version87Fields(int bitNumber, ISOFieldPackager isoBinaryFieldPackager, ISOFieldPackager isoAsciiFieldPackager, String... bitName) {
        this.bitNumber = bitNumber;
        this.isoBinaryFieldPackager = isoBinaryFieldPackager;
        this.isoAsciiFieldPackager = isoAsciiFieldPackager;
        this.shetabUsed = false;
        this.bitNames = Arrays.asList(bitName);
    }

    @Override
    public int getBitNumber() {
        return bitNumber;
    }

    @Override
    public List<String> getBitNames() {
        return bitNames;
    }

    public boolean isShetabUsed() {
        return shetabUsed;
    }

    public static Shetab7Version87Fields fromBitNumber(int bitNumber) {
        for (Shetab7Version87Fields s : Shetab7Version87Fields.values()) {
            if (s.getBitNumber() == bitNumber) {
                return s;
            }
        }
        return null;
    }

    public ISOFieldPackager getIsoBinaryFieldPackager() {
        return isoBinaryFieldPackager;
    }

    public static ISOPackager getIsoBinaryPackager() {
        if (isoBinaryPackager == null) {
            synchronized (Shetab7Version87Fields.class) {
                if (isoBinaryPackager == null) {
                    isoBinaryPackager = new ISOBasePackager() {
                    };

                    ISOFieldPackager[] array = IntStream.range(0, 128)
                            .mapToObj(Shetab7Version87Fields::fromBitNumber)
                            .map(b -> b == null ? null : b.isoBinaryFieldPackager)
                            .toArray(ISOFieldPackager[]::new);

                    isoBinaryPackager.setFieldPackager(array);
                }
            }
        }
        return isoBinaryPackager;
    }

    public static ISOPackager getIsoAsciiPackager() {
        if (isoAsciiPackager == null) {
            synchronized (Shetab7Version87Fields.class) {
                if (isoAsciiPackager == null) {
                    isoAsciiPackager = new ISOBasePackager() {
                    };

                    ISOFieldPackager[] array = IntStream.range(0, 128)
                            .mapToObj(Shetab7Version87Fields::fromBitNumber)
                            .map(b -> b == null ? null : b.isoAsciiFieldPackager)
                            .toArray(ISOFieldPackager[]::new);

                    isoAsciiPackager.setFieldPackager(array);
                }
            }
        }
        return isoAsciiPackager;
    }
}
