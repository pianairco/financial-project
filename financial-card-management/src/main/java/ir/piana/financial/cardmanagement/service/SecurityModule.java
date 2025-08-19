package ir.piana.financial.cardmanagement.service;

import ir.piana.financial.commons.utilities.HexUtility;

public class SecurityModule {
    private final String localMasterKey;
    private final String prefix;
    private final String postfix;
    private final String mask;
    private final String csvMask;
    private final String cvvMask;
    private final String zonePinKey;
    private final String zoneCsdKey;

    public SecurityModule(
            String localMasterKey, String zonePinKey, String zoneCsdKey,
            String prefix, String postfix,
            String mask, String csvMask, String cvvMask) {
        this.localMasterKey = localMasterKey;
        this.prefix = prefix;
        this.postfix = postfix;
        this.mask = mask;
        this.csvMask = csvMask;
        this.cvvMask = cvvMask;
        this.zonePinKey = zonePinKey;
        this.zoneCsdKey = zoneCsdKey;
    }

    public void generateMasterKey(int length) {
        try {
            String masterKey = HexUtility.getRandomHexString(length);
            ssmResponse.setEKey1(MyUtil.encrypt3Des(LMK1, masterKey));
            ssmResponse.setKcv(calculateKCV(masterKey, (short) 6));
            ssmResponse.setResp("00");
        } catch (Exception e) {
            ssmResponse.setResp("06");
            e.printStackTrace();
        }

        return ssmResponse;
    }
}
