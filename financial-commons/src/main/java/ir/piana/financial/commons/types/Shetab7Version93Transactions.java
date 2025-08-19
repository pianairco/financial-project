package ir.piana.financial.commons.types;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Shetab7Version93Transactions implements ISOTransactionType {
    T100_FROM_SHETAB(new int[]{
            2, 3, 7, 11, 12, 17, 19, 22, 24, 26, 32, 33, 37, 41, 42, 43, 48, 62},
            new int[]{
                    14, 35, 52, 53, 55, 59, 60, 61}),
    T100_TO_SHETAB(new int[]{
            2, 3, 7, 11, 12, 17, 19, 22, 24, 26, 32, 37, 41, 42, 43, 48, 62, 100},
            new int[]{
                    14, 35, 52, 53, 55, 59, 60, 61}),
    T110_FROM_SHETAB(new int[]{
            2, 3, 7, 11, 12, 15, 32, 33, 37, 39, 41, 42, 62, 102},
            new int[]{
                    38, 44, 54, 55, 59, 61, 124}),
    T110_TO_SHETAB(new int[]{
            2, 3, 7, 11, 12, 15, 32, 37, 39, 41, 42, 62, 100, 102},
            new int[]{
                    38, 44, 54, 55, 59, 61, 124}),
    T200_FROM_SHETAB(new int[]{
            2, 3, 4, 6, 7, 10, 11, 12, 17, 19, 22, 24, 26, 32, 33, 37, 41, 42, 43, 48, 49, 51, 62},
            new int[]{
                    14, 30, 35, 52, 53, 55, 56, 59, 60, 61, 120}),
    T200_TO_SHETAB(new int[]{
            2, 3, 4, 7, 11, 12, 17, 19, 22, 24, 26, 32, 37, 41, 42, 43, 48, 49, 62, 100},
            new int[]{
                    14, 30, 35, 52, 53, 55, 56, 59, 60, 61}),
    T210_FROM_SHETAB(new int[]{
            2, 3, 4, 6, 7, 10, 11, 12, 15, 32, 33, 37, 39, 41, 42, 49, 51, 62},
            new int[]{
                    38, 44, 54, 55, 59, 61}),
    T210_TO_SHETAB(new int[]{
            2, 3, 4, 6, 7, 10, 11, 12, 15, 32, 37, 39, 41, 42, 49, 51, 62, 100},
            new int[]{
                    38, 44, 54, 55, 59, 61}),
    T220_FROM_SHETAB(new int[]{
            2, 3, 4, 6, 7, 10, 11, 12, 17, 22, 24, 32, 33, 37, 41, 42, 48, 49, 51, 56, 62},
            new int[]{
                    61}),
    T230_TO_SHETAB(new int[]{
            2, 3, 4, 6, 7, 10, 11, 12, 15, 32, 37, 39, 41, 42, 49, 51, 62, 100},
            new int[]{
                    61}),
    T420_FROM_SHETAB(new int[]{
            2, 3, 4, 6, 7, 10, 11, 12, 17, 24, 25, 30, 32, 33, 37, 41, 42, 49, 51, 56, 62},
            new int[]{
                    61, 120}),
    T420_TO_SHETAB(new int[]{
            2, 3, 4, 7, 11, 12, 17, 24, 25, 30, 32, 37, 41, 42, 49, 56, 62, 100},
            new int[]{
                    61}),
    T430_FROM_SHETAB(new int[]{
            2, 3, 4, 6, 7, 10, 11, 12, 15, 32, 33, 37, 39, 41, 42, 49, 51, 62},
            new int[]{
                    61}),
    T430_TO_SHETAB(new int[]{
            2, 3, 4, 6, 7, 10, 11, 12, 15, 32, 37, 39, 41, 42, 49, 51, 62, 100},
            new int[]{
                    61}),
    T500_FROM_SHETAB(new int[]{
            7, 11, 12, 15, 32, 50, 74, 75, 76, 77, 78, 79, 80, 81, 86, 87, 88, 89, 97, 99, 109, 110},
            new int[]{}),
    T520_FROM_SHETAB(new int[]{
            7, 11, 12, 15, 32, 50, 74, 75, 76, 77, 78, 79, 80, 81, 86, 87, 88, 89, 97, 99, 109, 110},
            new int[]{}),
    T510_TO_SHETAB(new int[]{
            7, 11, 12, 32, 39, 99},
            new int[]{}),
    T530_TO_SHETAB(new int[]{
            7, 11, 12, 32, 39, 99},
            new int[]{}),
    T502_FROM_SHETAB(new int[]{
            2, 7, 11, 12, 15, 24, 50, 74, 75, 76, 77, 78, 79, 80, 81, 86, 87, 88, 89, 97, 99, 109, 110},
            new int[]{}),
    T522_FROM_SHETAB(new int[]{
            2, 7, 11, 12, 15, 24, 50, 74, 75, 76, 77, 78, 79, 80, 81, 86, 87, 88, 89, 97, 99, 109, 110},
            new int[]{}),
    T512_TO_SHETAB(new int[]{
            2, 7, 11, 12, 39, 99},
            new int[]{}),
    T532_TO_SHETAB(new int[]{
            2, 7, 11, 12, 39, 99},
            new int[]{}),
    T804_FROM_SHETAB_CHANGE_FINANCIAL_DAY(new int[]{
            7, 11, 12, 15, 24, 93, 94},
            new int[]{}),
    T804_FROM_SHETAB_CHECK_CONNECTION(new int[]{
            7, 11, 12, 24, 93, 94},
            new int[]{}),
    T804_FROM_SHETAB_CHANGE_KEY(new int[]{
            7, 11, 12, 24, 53, 60, 93, 94, 96},
            new int[]{}),
    T804_FROM_SHETAB_REFLECTIVE_TEST(new int[]{
            7, 11, 12, 24, 93, 94},
            new int[]{}),
    T804_TO_SHETAB_CHECK_CONNECTION(new int[]{
            7, 11, 12, 24, 93, 94},
            new int[]{}),
    T804_TO_SHETAB_REFLECTIVE_TEST(new int[]{
            7, 11, 12, 24, 93, 94},
            new int[]{}),
    T824_FROM_SHETAB_CHANGE_FINANCIAL_DAY(new int[]{
            7, 11, 12, 15, 24, 93, 94},
            new int[]{}),
    T824_FROM_SHETAB_CHECK_CONNECTION(new int[]{
            7, 11, 12, 24, 93, 94},
            new int[]{}),
    T824_FROM_SHETAB_CHANGE_KEY(new int[]{
            7, 11, 12, 24, 53, 60, 93, 94, 96},
            new int[]{}),
    T824_FROM_SHETAB_REFLECTIVE_TEST(new int[]{
            7, 11, 12, 24, 93, 94},
            new int[]{}),
    T824_TO_SHETAB_CHECK_CONNECTION(new int[]{
            7, 11, 12, 24, 93, 94},
            new int[]{}),
    T824_TO_SHETAB_REFLECTIVE_TEST(new int[]{
            7, 11, 12, 24, 93, 94},
            new int[]{}),
    T814_FROM_SHETAB_CHANGE_FINANCIAL_DAY(new int[]{
            7, 11, 12, 24, 39, 93, 94},
            new int[]{}),
    T814_FROM_SHETAB_CHECK_CONNECTION(new int[]{
            7, 11, 12, 24, 39, 93, 94},
            new int[]{}),
    T814_FROM_SHETAB_CHANGE_KEY(new int[]{
            7, 11, 12, 24, 39, 93, 94},
            new int[]{}),
    T814_FROM_SHETAB_REFLECTIVE_TEST(new int[]{
            7, 11, 12, 24, 39, 93, 94},
            new int[]{}),
    T814_TO_SHETAB_REFLECTIVE_TEST(new int[]{
            7, 11, 12, 24, 39, 93, 94},
            new int[]{}),
    T814_TO_SHETAB_CHECK_CONNECTION(new int[]{
            7, 11, 12, 24, 39, 93, 94},
            new int[]{}),
    T834_FROM_SHETAB_CHANGE_FINANCIAL_DAY(new int[]{
            7, 11, 12, 24, 39, 93, 94},
            new int[]{}),
    T834_FROM_SHETAB_CHECK_CONNECTION(new int[]{
            7, 11, 12, 24, 39, 93, 94},
            new int[]{}),
    T834_FROM_SHETAB_CHANGE_KEY(new int[]{
            7, 11, 12, 24, 39, 93, 94},
            new int[]{}),
    T834_FROM_SHETAB_REFLECTIVE_TEST(new int[]{
            7, 11, 12, 24, 39, 93, 94},
            new int[]{}),
    T834_TO_SHETAB_REFLECTIVE_TEST(new int[]{
            7, 11, 12, 24, 39, 93, 94},
            new int[]{}),
    T834_TO_SHETAB_CHECK_CONNECTION(new int[]{
            7, 11, 12, 24, 39, 93, 94},
            new int[]{}),
    ;

    private final List<ISOFieldType> mandatoryBits;
    private final List<ISOFieldType> conditionalBits;
    private final List<ISOFieldType> allBits;
    private final int[] allFields;

    Shetab7Version93Transactions(int[] mandatoryBits, int[] conditionalBits) {
        this.mandatoryBits = Arrays.stream(mandatoryBits)
                .mapToObj(Shetab7Version93Fields::fromBitNumber)
                .collect(Collectors.toList());
        this.conditionalBits = Arrays.stream(conditionalBits)
                .mapToObj(Shetab7Version93Fields::fromBitNumber)
                .collect(Collectors.toList());
        this.allBits = new ArrayList<>(this.mandatoryBits); // Initialize with elements from list1
        this.allBits.addAll(this.conditionalBits);
//        Collections.sort(this.allBits, Comparator.comparingInt(ISOFieldType::getBitNumber));
        this.allFields = new int[this.allBits.size() + 1];
        this.allFields[0] = Shetab7Version93Fields.MTI.getBitNumber();
        System.arraycopy(
                allBits.stream().mapToInt(ISOFieldType::getBitNumber).toArray(),
                0, this.allFields, 1, this.allBits.size() - 1);

        /*this.allFields = Stream.of(Shetab7Version93Fields.MTI.getBitNumber(),
                        allBits.stream().mapToInt(ISOFieldType::getBitNumber).toArray())
                .mapToInt(i -> (int) i).toArray();*/
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

    public static void main(String[] args) {
        int[] fields = Shetab7Version93Transactions.T210_TO_SHETAB.fields();
    }
}
