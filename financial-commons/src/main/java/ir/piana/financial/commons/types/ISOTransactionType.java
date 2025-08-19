package ir.piana.financial.commons.types;

import java.util.List;

public interface ISOTransactionType {
    List<ISOFieldType> getMandatoryBits();
    List<ISOFieldType> getConditionalBits();
    List<ISOFieldType> getAllBits();
    int[] fields();
}
