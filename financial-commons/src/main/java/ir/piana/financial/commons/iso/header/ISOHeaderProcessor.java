package ir.piana.financial.commons.iso.header;

public interface ISOHeaderProcessor {
    boolean isCorrect(byte[] requestHeader);
}
