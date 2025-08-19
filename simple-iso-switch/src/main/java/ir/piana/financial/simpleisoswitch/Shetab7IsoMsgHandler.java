package ir.piana.financial.simpleisoswitch;

import ir.piana.financial.commons.errors.FinancialWrappedException;
import ir.piana.financial.commons.iso.server.ISOMsgRequestHandler;
import ir.piana.financial.commons.types.Shetab7Version93Transactions;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.springframework.stereotype.Component;

@Component("shetab-7-message-handler")
public class Shetab7IsoMsgHandler implements ISOMsgRequestHandler {
    @Override
    public ISOMsg handle(String correlationId, ISOMsg isoMsg) throws FinancialWrappedException {
        try {
            ISOMsg responseIsoMsg = (ISOMsg) isoMsg.clone(Shetab7Version93Transactions.T210_TO_SHETAB.fields());
            responseIsoMsg.setResponseMTI();
            responseIsoMsg.set(15, "040511");
            responseIsoMsg.set(39, "000");
            responseIsoMsg.recalcBitMap();
            return responseIsoMsg;
        } catch (ISOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ISOMsg handleException(
            String correlationId, ISOMsg requestIsoMsg, ISOMsg responseIsoMsg, Throwable throwable)
            throws FinancialWrappedException {
        return null;
    }
}
