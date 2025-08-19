package ir.piana.financial.commons.iso.server;

import ir.piana.financial.commons.errors.FinancialWrappedException;
import org.jpos.iso.ISOMsg;

public interface ISOMsgRequestHandler {
    ISOMsg handle(String correlationId, ISOMsg isoMsg) throws FinancialWrappedException;
    ISOMsg handleException(String correlationId,
                               ISOMsg requestIsoMsg, ISOMsg responseIsoMsg,
                               Throwable throwable) throws FinancialWrappedException;
}
