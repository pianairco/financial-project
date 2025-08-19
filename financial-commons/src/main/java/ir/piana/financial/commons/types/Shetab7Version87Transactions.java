package ir.piana.financial.commons.types;

import java.util.*;
import java.util.stream.Collectors;

public enum Shetab7Version87Transactions implements ISOTransactionType {
    T100_FROM_SHETAB(new int[]{
            2, 3, 7, 11, 12, 13, 17, 18, 19, 22, 25, 26, 32, 33, 37, 41, 42, 43, 48, 62},
            new int[]{
                    14, 35, 52, 53, 55, 59, 60, 61}),
    T100_TO_SHETAB(new int[]{
            2, 3, 7, 11, 12, 13, 17, 18, 19, 22, 25, 26, 32, 37, 41, 42, 43, 48, 62, 100},
            new int[]{
                    14, 35, 52, 53, 55, 59, 60, 61}),
    T110_FROM_SHETAB(new int[]{
            2, 3, 7, 11, 12, 13, 15, 32, 33, 37, 39, 41, 42, 62},
            new int[]{
                    38, 44, 54, 55, 59, 61, 102, 124}),
    T110_TO_SHETAB(new int[]{
            2, 3, 7, 11, 12, 13, 15, 32, 37, 39, 41, 42, 62, 100},
            new int[]{
                    38, 44, 54, 55, 59, 61, 102, 124}),
    T200_FROM_SHETAB(new int[]{
            2, 3, 4, 6, 7, 10, 11, 12, 13, 17, 18, 19, 22, 25, 26, 32, 33, 37, 41, 42, 43, 48, 49, 51, 62},
            new int[]{
                    14, 35, 52, 53, 55, 59, 60, 61, 90, 95, 120}),
    T200_TO_SHETAB(new int[]{
            2, 3, 4, 7, 11, 12, 13, 17, 18, 19, 22, 25, 26, 32, 37, 41, 42, 43, 48, 49, 62, 100},
            new int[]{
                    14, 35, 52, 53, 55, 59, 60, 61, 90, 95}),
    T210_FROM_SHETAB(new int[]{
            2, 3, 4, 6, 7, 10, 11, 12, 13, 15, 32, 33, 37, 39, 41, 42, 43, 49, 51, 62},
            new int[]{
                    38, 44, 54, 55, 59, 61}),
    T210_TO_SHETAB(new int[]{
            2, 3, 4, 6, 7, 10, 11, 12, 13, 15, 32, 37, 39, 41, 42, 43, 49, 51, 62, 100},
            new int[]{
                    38, 44, 54, 55, 59, 61}),
    T220_FROM_SHETAB(new int[]{
            2, 3, 4, 6, 7, 10, 11, 12, 13, 17, 22, 25, 32, 33, 37, 41, 42, 48, 49, 51, 62, 90},
            new int[]{
                    61}),
    T230_TO_SHETAB(new int[]{
            2, 3, 4, 6, 7, 10, 11, 12, 13, 15, 32, 37, 39, 41, 42, 49, 51, 62, 100},
            new int[]{
                    61}),
    T400_FROM_SHETAB(new int[]{
            2, 3, 4, 6, 7, 10, 11, 12, 13, 17, 32, 33, 37, 41, 42, 49, 51, 62, 90},
            new int[]{
                    61, 95, 120}),
    T400_TO_SHETAB(new int[]{
            2, 3, 4, 7, 11, 12, 13, 17, 32, 37, 41, 42, 49, 62, 90, 100},
            new int[]{
                    61, 95}),
    T420_FROM_SHETAB(new int[]{
            2, 3, 4, 6, 7, 10, 11, 12, 13, 17, 32, 33, 37, 41, 42, 49, 51, 62, 90},
            new int[]{
                    61, 95, 120}),
    T420_TO_SHETAB(new int[]{
            2, 3, 4, 7, 11, 12, 13, 17, 32, 37, 41, 42, 49, 62, 90, 100},
            new int[]{
                    61, 95}),
    T410_FROM_SHETAB(new int[]{
            2, 3, 4, 6, 7, 10, 11, 12, 13, 15, 32, 33, 37, 39, 41, 42, 49, 51, 62},
            new int[]{
                    61}),
    T410_TO_SHETAB(new int[]{
            2, 3, 4, 6, 7, 10, 11, 12, 13, 15, 32, 37, 39, 41, 42, 49, 51, 62, 100},
            new int[]{
                    61}),
    T430_FROM_SHETAB(new int[]{
            2, 3, 4, 6, 7, 10, 11, 12, 13, 15, 32, 33, 37, 39, 41, 42, 49, 51, 62},
            new int[]{
                    61}),
    T430_TO_SHETAB(new int[]{
            2, 3, 4, 6, 7, 10, 11, 12, 13, 15, 32, 37, 39, 41, 42, 49, 51, 62, 100},
            new int[]{
                    61}),
    T500_FROM_SHETAB(new int[]{
            7, 11, 12, 13, 15, 32, 50, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 97, 99},
            new int[]{}),
    T520_FROM_SHETAB(new int[]{
            7, 11, 12, 13, 15, 32, 50, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 97, 99},
            new int[]{}),
    T510_TO_SHETAB(new int[]{
            7, 11, 12, 13, 32, 39, 66, 99},
            new int[]{}),
    T530_TO_SHETAB(new int[]{
            7, 11, 12, 13, 32, 39, 66, 99},
            new int[]{}),
    T502_FROM_SHETAB(new int[]{
            2, 7, 11, 12, 13, 15, 50, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 97, 99},
            new int[]{}),
    T522_FROM_SHETAB(new int[]{
            2, 7, 11, 12, 13, 15, 50, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 97, 99},
            new int[]{}),
    T512_TO_SHETAB(new int[]{
            2, 7, 11, 12, 13, 39, 66, 99},
            new int[]{}),
    T532_TO_SHETAB(new int[]{
            2, 7, 11, 12, 13, 39, 66, 99},
            new int[]{}),
    T800_FROM_SHETAB_CHANGE_FINANCIAL_DAY(new int[]{
            7, 11, 12, 13, 15, 33, 70},
            new int[]{}),
    T800_FROM_SHETAB_CHECK_CONNECTION(new int[]{
            7, 11, 12, 13, 33, 70},
            new int[]{}),
    T800_FROM_SHETAB_CHANGE_KEY(new int[]{
            7, 11, 12, 13, 15, 33, 53, 60, 70, 96},
            new int[]{}),
    T800_FROM_SHETAB_REFLECTIVE_TEST(new int[]{
            7, 11, 12, 13, 33, 70},
            new int[]{}),
    T800_TO_SHETAB_REFLECTIVE_TEST(new int[]{
            7, 11, 12, 13, 33, 70},
            new int[]{}),
    T800_TO_SHETAB_CHECK_CONNECTION(new int[]{
            7, 11, 12, 13, 33, 70},
            new int[]{}),
    T820_FROM_SHETAB_CHANGE_FINANCIAL_DAY(new int[]{
            7, 11, 12, 13, 15, 33, 70},
            new int[]{}),
    T820_FROM_SHETAB_CHECK_CONNECTION(new int[]{
            7, 11, 12, 13, 33, 70},
            new int[]{}),
    T820_FROM_SHETAB_CHANGE_KEY(new int[]{
            7, 11, 12, 13, 15, 33, 53, 60, 70, 96},
            new int[]{}),
    T820_FROM_SHETAB_REFLECTIVE_TEST(new int[]{
            7, 11, 12, 13, 33, 70},
            new int[]{}),
    T820_TO_SHETAB_REFLECTIVE_TEST(new int[]{
            7, 11, 12, 13, 33, 70},
            new int[]{}),
    T820_TO_SHETAB_CHECK_CONNECTION(new int[]{
            7, 11, 12, 13, 33, 70},
            new int[]{}),
    T810_FROM_SHETAB_CHANGE_FINANCIAL_DAY(new int[]{
            7, 11, 12, 13, 39, 70, 100},
            new int[]{}),
    T810_FROM_SHETAB_CHECK_CONNECTION(new int[]{
            7, 11, 12, 13, 39, 70, 100},
            new int[]{}),
    T810_FROM_SHETAB_CHANGE_KEY(new int[]{
            7, 11, 12, 13, 39, 70, 100},
            new int[]{}),
    T810_FROM_SHETAB_REFLECTIVE_TEST(new int[]{
            7, 11, 12, 13, 39, 70, 100},
            new int[]{}),
    T810_TO_SHETAB_REFLECTIVE_TEST(new int[]{
            7, 11, 12, 13, 39, 70, 100},
            new int[]{}),
    T810_TO_SHETAB_CHECK_CONNECTION(new int[]{
            7, 11, 12, 13, 39, 70, 100},
            new int[]{}),
    T830_FROM_SHETAB_CHANGE_FINANCIAL_DAY(new int[]{
            7, 11, 12, 13, 39, 70, 100},
            new int[]{}),
    T830_FROM_SHETAB_CHECK_CONNECTION(new int[]{
            7, 11, 12, 13, 39, 70, 100},
            new int[]{}),
    T830_FROM_SHETAB_CHANGE_KEY(new int[]{
            7, 11, 12, 13, 39, 70, 100},
            new int[]{}),
    T830_FROM_SHETAB_REFLECTIVE_TEST(new int[]{
            7, 11, 12, 13, 39, 70, 100},
            new int[]{}),
    T830_TO_SHETAB_REFLECTIVE_TEST(new int[]{
            7, 11, 12, 13, 39, 70, 100},
            new int[]{}),
    T830_TO_SHETAB_CHECK_CONNECTION(new int[]{
            7, 11, 12, 13, 39, 70, 100},
            new int[]{}),
    ;

    private final List<ISOFieldType> mandatoryBits;
    private final List<ISOFieldType> conditionalBits;
    private final List<ISOFieldType> allBits;
    private final int[] allFields;

    Shetab7Version87Transactions(int[] mandatoryBits, int[] conditionalBits) {
        this.mandatoryBits = Arrays.stream(mandatoryBits)
                .mapToObj(Shetab7Version87Fields::fromBitNumber)
                .collect(Collectors.toList());
        this.conditionalBits = Arrays.stream(conditionalBits)
                .mapToObj(Shetab7Version87Fields::fromBitNumber)
                .collect(Collectors.toList());
        this.allBits = new ArrayList<>(this.mandatoryBits); // Initialize with elements from list1
        this.allBits.addAll(this.conditionalBits);
        Collections.sort(this.allBits, Comparator.comparingInt(ISOFieldType::getBitNumber));
        this.allFields = allBits.stream().map(ISOFieldType::getBitNumber).mapToInt(Integer::intValue).toArray();
    }

    @Override
    public List<ISOFieldType> getMandatoryBits() {
        return mandatoryBits;
    }

    @Override
    public List<ISOFieldType> getConditionalBits() {
        return conditionalBits;
    }

    @Override
    public List<ISOFieldType> getAllBits() {
        return allBits;
    }

    @Override
    public int[] fields() {
        return allFields;
    }
}
