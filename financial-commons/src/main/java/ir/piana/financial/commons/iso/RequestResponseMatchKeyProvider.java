package ir.piana.financial.commons.iso;

import org.jpos.iso.ISOMsg;

public interface RequestResponseMatchKeyProvider {
    String matchKey(ISOMsg isoMsg);
}
