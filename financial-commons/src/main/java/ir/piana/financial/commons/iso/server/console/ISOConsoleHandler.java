package ir.piana.financial.commons.iso.server.console;

import ir.piana.financial.commons.iso.channel.ISOChannelSerializer;
import ir.piana.financial.commons.iso.channel.ISOPayload;
import ir.piana.financial.commons.iso.header.ISOHeaderProcessor;
import ir.piana.financial.commons.iso.header.ISOHeaderProvider;
import ir.piana.financial.commons.iso.server.ISOMsgRequestHandler;
import ir.piana.financial.commons.types.ISOPackagerType;
import ir.piana.financial.commons.types.ResponseHeaderProvideType;
import ir.piana.financial.commons.utilities.HexUtility;
import org.jpos.iso.ISOMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.UUID;

public class ISOConsoleHandler {
    private final Logger log;

    private final ISOChannelSerializer isoChannelSerializer;
    private final ISOPackagerType isoPackagerType;
    private final ResponseHeaderProvideType responseHeaderProvideType;
    private final ISOHeaderProvider isoHeaderProvider;
    private final ISOHeaderProcessor isoHeaderProcessor;
    private final ISOMsgRequestHandler isoRequestHandler;
    private final boolean logHexInOut;

    public ISOConsoleHandler(
            ISOChannelSerializer isoChannelSerializer,
            ISOPackagerType isoPackagerType,
            ResponseHeaderProvideType responseHeaderProvideType,
            ISOHeaderProvider isoHeaderProvider,
            ISOHeaderProcessor isoHeaderProcessor,
            ISOMsgRequestHandler isoRequestHandler,
            boolean logHexInOut) {
        this.responseHeaderProvideType = responseHeaderProvideType;
        this.log = LoggerFactory.getLogger(ISOConsoleHandler.class);
        this.isoChannelSerializer = isoChannelSerializer;
        this.isoPackagerType = isoPackagerType;
        this.isoHeaderProvider = isoHeaderProvider;
        this.isoHeaderProcessor = isoHeaderProcessor;
        this.isoRequestHandler = isoRequestHandler;
        this.logHexInOut = logHexInOut;
    }

    public byte[] process(InputStream in) {
        try {
            ISOPayload requestIsoPayload = null;

            ISOMsg responseIsoMsg = null;
            String correlationId = UUID.randomUUID().toString();
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                try {
                    requestIsoPayload = isoChannelSerializer.readIsoPayload(in).createNewByIsoMsg(isoPackagerType);
                    if (logHexInOut) {
                        log.info("in-req {}: {}-{}", correlationId,
                                requestIsoPayload.headerBytes() == null ? "" : HexUtility.bytesToHex(requestIsoPayload.headerBytes()),
                                HexUtility.bytesToHex(requestIsoPayload.messageBytes()));
                    }
                    responseIsoMsg = isoRequestHandler.handle(correlationId, requestIsoPayload.isoMsg());
                } catch (Throwable e) {
                    log.error("{}: {}", correlationId, e.getMessage());
                    responseIsoMsg = isoRequestHandler.handleException(
                            correlationId,
                            requestIsoPayload == null ? null : requestIsoPayload.isoMsg(),
                            null, e);
                }

                byte[] responseHeader = isoHeaderProvider.provide(requestIsoPayload.isoMsg(), null);

                ISOPayload responseIsoPayload = new ISOPayload(responseHeader, responseIsoMsg, isoPackagerType);
                if (logHexInOut) {
                    log.info("handled-res {}: {}-{}", correlationId,
                            HexUtility.bytesToHex(responseIsoPayload.headerBytes()),
                            HexUtility.bytesToHex(responseIsoPayload.messageBytes()));
                }

                isoChannelSerializer.writeIsoPayload(responseIsoPayload, os);
                if (logHexInOut) {
                    log.info("out-res {}: {}-{}", correlationId,
                            HexUtility.bytesToHex(responseIsoPayload.headerBytes()),
                            HexUtility.bytesToHex(responseIsoPayload.messageBytes()));
                }

                return os.toByteArray();
            } catch (Throwable/* | FinancialWrappedException*/ e) {
                                    /*try {
                                        if (isoMessageRequestHandler instanceof ISOMessageRequestHandler) {
                                            handleMessageException(correlationId, requestPayload);
                                        } else {
                                            handlePayloadException(correlationId, requestPayload, responsePayload, clientSocket, e);
                                        }
                                        isoRequestHandler.handleException(
                                                correlationId, requestPayload, responsePayload, clientSocket, e);
                                    } catch (FinancialWrappedException ex) {
                                        log.error("{}: {}", correlationId, ex.getMessage());
                                    }*/
                log.error("{}: {}", correlationId, e.getMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
