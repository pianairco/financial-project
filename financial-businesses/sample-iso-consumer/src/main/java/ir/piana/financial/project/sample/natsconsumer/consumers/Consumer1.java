package ir.piana.financial.project.sample.natsconsumer.consumers;

import ir.piana.financial.commons.errors.FinancialWrappedException;
import ir.piana.financial.broker.iso.channels.IsoChannelPayloads;
import ir.piana.financial.broker.iso.socket.IsoRequestHandler;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component("consumer-1")
public class Consumer1 implements IsoRequestHandler {
    Logger log = LoggerFactory.getLogger(Consumer1.class);
    private final ISOPackager isoPackager;

    public Consumer1(ISOPackager isoPackager) {
        this.isoPackager = isoPackager;
    }

    AtomicInteger count = new AtomicInteger(0);

    @Override
    public byte[] handle(byte[] isoPayload) throws FinancialWrappedException {
        log.info("{}-th received message", count.incrementAndGet());
        try {
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(isoPackager);
            isoMsg.unpack(isoPayload);
            isoMsg.setResponseMTI();
            isoMsg.set(39, "00");
            return isoMsg.pack();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public byte[] exceptionHandling(FinancialWrappedException exception, byte[] isoPayload) throws FinancialWrappedException {
        return new byte[0];
    }

    @Override
    public IsoChannelPayloads exceptionHandling(FinancialWrappedException exception, IsoChannelPayloads isoPayload) throws FinancialWrappedException {
        return null;
    }
}
