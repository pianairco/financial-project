package ir.piana.financial.commons.types;

public enum IranianBank {
    MARKAZI("636795", "001", "بانک مرکزی", "Bank Markazi", "CNT"),
    AYANDEH("636214", "001", "بانک آینده", "Bank Ayandeh", "AYN"),
    POST_BANK("627760", "001", "پست بانک", "Post Bank", "PST"),
    TOSEE_SADERAT("627648", "001", "بانک توسعه صادرات", "Bank Tose'e Saderat", "BTS"),
    KHAVAR_MIANEH("585947", "001", "بانک خاورمیانه", "Bank Khavar Mianeh", "KHM"),
    RESALAT("504172", "001", "بانک رسالت", "Bank Resalat", "RST"),
    SARMAYEH("639607", "001", "بانک سرمایه", "Bank Sarmayeh", "SAR"),
    SANAT_MADAN("627961", "001", "بانک صنعت و معدن", "Bank Sanat Va Madan", "BSM"),
    GARDESHGARI("505416", "001", "بانک گردشگری", "Bank Gardeshgari", "GAR"),
    MOASESEH_ETEBARI_TOSEE("628157", "001", "موسسه اعتباری توسعه", "Moaseseh Etebari Tose'e", "CID"),
    MOASESEH_KOSAR("525801", "001", "موسسه کوثر", "Moaseseh Kosar", "KSA"),
    MOASESEH_ASKARIYEH("606256", "001", "موسسه عسکریه", "Moaseseh Askariyeh", "ASK"),
    MELLI_IRAN("603799", "001", "بانک ملی ایران", "Bank Melli Iran", "BMI"),
    SEPAH("589210", "002", "بانک سپه", "Bank Sepah", "SEP"),
    KESHAVARZI("603770", "003", "بانک کشاورزی", "Bank Keshavarzi", "BKI"),
    MASKAN("628023", "005", "بانک مسکن", "Bank Maskan", "MSK"),
    SADERAT_IRAN("603769", "019", "بانک صادرات ایران", "Bank Saderat Iran", "BSI"),
    //TEJARAT = 627353
    TEJARAT("585983", "018", "بانک تجارت", "Bank Tejarat", "TEJ"),
    MELLAT("610433", "014", "بانک ملت", "Bank Mellat", "MEL"),
    //REFAH_KARGARAN = 502229
    REFAH_KARGARAN("589463", "013", "بانک رفاه کارگران", "Bank Refah Kargaran", "REF"),
    PASARGAD("502229", "054", "بانک پاسارگاد", "Bank Pasargad", "PAS"),
    //PARSIAN = 639194 , 627884
    PARSIAN("622106", "056", "بانک پارسیان", "Bank Parsian", "PAR"),
    EGHTESAD_NOVIN("627488", "057", "بانک اقتصاد نوین", "Bank Eghtesad Novin", "NOV"),
    KARAFARIN("627488", "058", "بانک کارآفرین", "Bank Karafarin", "KAR"),
    SAMAN("621986", "059", "بانک سامان", "Bank Saman", "SAM"),
    SHAHR("504706", "060", "بانک شهر", "Bank Shahr", "SHR"),
    DAY("502938", "062", "بانک دی", "Bank Day", "DAY"),
    ANSAR("636214", "061", "بانک انصار", "Bank Ansar", "ANS"),
    GHAVAMIN("636795", "063", "بانک قوامین", "Bank Ghavamin", ""),
    HEKMAT_IRANIAN("636214", "064", "بانک حکمت ایرانیان", "Bank Hekmat Iranian", "HEK"),
    SINA("639346", "066", "بانک سینا", "Bank Sina", "SIN"),
    IRAN_ZAMIN("639347", "067", "بانک ایران زمین", "Bank Iran Zamin", "IRZ"),
    TOSEE_TAAVON("502908", "068", "بانک توسعه تعاون", "Bank Tose’e Ta’avon", "BTT"),
    TAAVON("636214", "069", "بانک تعاون", "Bank Taavon", ""),
    QARZ_AL_HASANEH_MEHR("606373", "070", "بانک قرض‌الحسنه مهر ایران", "Bank Qarz Al-Hasaneh Mehr Iran", "MHR");

    private final String bin;
    private final String bankId;
    private final String persianName;
    private final String englishName;
    private final String abbrevation;

    IranianBank(String bin, String bankId, String persianName, String englishName, String abbrevation) {
        this.bin = bin;
        this.bankId = bankId;
        this.persianName = persianName;
        this.englishName = englishName;
        this.abbrevation = abbrevation;
    }

    public String getBin() {
        return bin;
    }

    public String getBankId() {
        return bankId;
    }

    public String getPersianName() {
        return persianName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getAbbrevation() {
        return abbrevation;
    }
}
