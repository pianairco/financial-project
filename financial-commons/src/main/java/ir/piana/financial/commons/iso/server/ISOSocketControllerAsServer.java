package ir.piana.financial.commons.iso.server;

import io.micrometer.core.instrument.MeterRegistry;
import ir.piana.financial.commons.iso.RequestResponseMatchKeyProvider;
import ir.piana.financial.commons.iso.channel.ISOChannelSerializer;
import ir.piana.financial.commons.iso.channel.ISOPayload;
import ir.piana.financial.commons.iso.header.ISOHeaderProcessor;
import ir.piana.financial.commons.iso.header.ISOHeaderProvider;
import ir.piana.financial.commons.iso.socket.SocketCloseNotifier;
import ir.piana.financial.commons.iso.socket.SocketController;
import ir.piana.financial.commons.types.ISOPackagerType;
import ir.piana.financial.commons.types.ResponseHeaderProvideType;
import ir.piana.financial.commons.types.SocketConnectionType;
import ir.piana.financial.commons.utilities.HexUtility;
import org.jpos.iso.ISOMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;
import java.util.concurrent.*;

public class ISOSocketControllerAsServer implements SocketCloseNotifier {
    private final Logger log = LoggerFactory.getLogger(ISOSocketControllerAsServer.class);

    private final SocketController socketController;
    private final RequestResponseMatchKeyProvider matchKeyProvider;
    private final ISOChannelSerializer isoChannelSerializer;
    private final ISOPackagerType isoPackagerType;
    private final ResponseHeaderProvideType responseHeaderProvideType;
    private final ISOHeaderProvider isoHeaderProvider;
    private final ISOHeaderProcessor isoHeaderProcessor;
    private final ISOMsgRequestHandler isoRequestHandler;
    private final boolean logHexInOut;
    private final ISOSocketControllerAsServerCloseNotifier closeNotifier;
    private final MeterRegistry meterRegistry;

    /*private final Counter totalReadMetricCounter;
    private final Counter totalWriteMetricCounter;*/

    public ISOSocketControllerAsServer(
            SocketController socketController,
            RequestResponseMatchKeyProvider matchKeyProvider,
            ISOChannelSerializer isoChannelSerializer,
            ISOPackagerType isoPackagerType,
            ResponseHeaderProvideType responseHeaderProvideType,
            ISOHeaderProvider isoHeaderProvider,
            ISOHeaderProcessor isoHeaderProcessor,
            ISOMsgRequestHandler isoRequestHandler,
            boolean logHexInOut,
            ISOSocketControllerAsServerCloseNotifier closeNotifier,
            MeterRegistry meterRegistry) {
        this.socketController = socketController;
        this.matchKeyProvider = matchKeyProvider;
        this.isoChannelSerializer = isoChannelSerializer;
        this.isoPackagerType = isoPackagerType;
        this.responseHeaderProvideType = responseHeaderProvideType;
        this.isoHeaderProvider = isoHeaderProvider;
        this.isoHeaderProcessor = isoHeaderProcessor;
        this.isoRequestHandler = isoRequestHandler;
        this.logHexInOut = logHexInOut;
        this.closeNotifier = closeNotifier;
        this.meterRegistry = meterRegistry;

        /*totalReadMetricCounter = Counter.builder(String.format("total_read_from_%s_%s",
                        socketController.getRemoteInetAddress().getHostAddress(),
                        socketController.getRemotePort()))
                .description(String.format("total read from client %s:%s",
                        socketController.getRemoteInetAddress().getHostAddress(),
                        socketController.getRemotePort()))
                .register(meterRegistry);

        totalWriteMetricCounter = Counter.builder(String.format("total_write_to_%s_%s",
                        socketController.getRemoteInetAddress().getHostAddress(),
                        socketController.getRemotePort()))
                .description(String.format("total write to client %s:%s",
                        socketController.getRemoteInetAddress().getHostAddress(),
                        socketController.getRemotePort()))
                .register(meterRegistry);*/
    }

    public static ISOSocketControllerAsServer createAndStart(
            Socket client,
            SocketConnectionType socketConnectionType,
            long durationInMillis,
            RequestResponseMatchKeyProvider matchKeyProvider,
            ISOChannelSerializer isoChannelSerializer,
            ISOPackagerType isoPackagerType,
            ResponseHeaderProvideType responseHeaderProvideType,
            ISOHeaderProvider isoHeaderProvider,
            ISOHeaderProcessor ISOHeaderProcessor,
            ISOMsgRequestHandler isoMsgRequestHandler,
            boolean logHexInOut,
            ISOSocketControllerAsServerCloseNotifier socketCloseNotifier,
            MeterRegistry meterRegistry
    ) throws SocketException {
        SocketController socketController = SocketController.createSocketController(
                client, socketConnectionType, durationInMillis, logHexInOut);
        ISOSocketControllerAsServer isoSocketControllerAsServer = new ISOSocketControllerAsServer(
                socketController,
                matchKeyProvider,
                isoChannelSerializer,
                isoPackagerType,
                responseHeaderProvideType,
                isoHeaderProvider,
                ISOHeaderProcessor,
                isoMsgRequestHandler,
                logHexInOut,
                socketCloseNotifier,
                meterRegistry
        );
        socketController.addCloseNotifier(isoSocketControllerAsServer);
        isoSocketControllerAsServer.start();
        return isoSocketControllerAsServer;
    }

    void start() {
        ThreadFactory factory = Thread.ofVirtual()
                .name("piana-v-thread-", 0)
                .uncaughtExceptionHandler((t, e) -> {
                    log.debug("exception occurred: {}", e.getMessage());
                    // Do nothing — suppress logging
                    // Or log somewhere else as needed
                })
                .factory();
        handleSocketInputFuture = Executors.newThreadPerTaskExecutor(factory).submit(
                () -> readServerInputStreamAndOfferReceivedRequest(socketController));
        handleInputQueueFuture = Executors.newThreadPerTaskExecutor(factory).submit(
                () -> takeAndHandleReceivedRequest(socketController));
        handleSocketOutputFuture = Executors.newThreadPerTaskExecutor(factory).submit(
                () -> takeAndSendPreparedResponse(socketController));
    }

    private final BlockingQueue<ServerProcessContext> receivedRequestBQ = new LinkedBlockingQueue<>();
    private final BlockingQueue<ServerProcessContext> preparedResponseBQ = new LinkedBlockingQueue<>();

    private Future<?> handleSocketInputFuture;
    private Future<?> handleInputQueueFuture;
    private Future<?> handleSocketOutputFuture;

    private void readServerInputStreamAndOfferReceivedRequest(SocketController socketController) {
        log.debug("handleSocketInput start {}", Thread.currentThread().getName());
        try (InputStream in = socketController.getInputStream()) {
            while (socketController.isValid()) {
                String correlationId = UUID.randomUUID().toString();
                byte[] bytes = isoChannelSerializer.readIsoPayloadBytes(in);
                if (bytes == null || bytes.length == 0) {
                    if (!socketController.isValid()) {
                        break;
                    }
                }
                /*totalReadMetricCounter.increment();*/
                socketController.offerMessageReceived();
                if (logHexInOut) {
                    log.info("in-req {}: {}", correlationId, HexUtility.bytesToHex(bytes));
                }
                receivedRequestBQ.offer(new ServerProcessContext(correlationId, bytes));
            }
        } catch (Exception e) {
            if (e instanceof SocketException && ((SocketException) e).getMessage().equals("Socket closed")) {
                log.warn("socket {} closed", socketController.getRemoteInetAddress().toString());
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    private void takeAndSendPreparedResponse(SocketController socketController) {
        try (OutputStream out = socketController.getOutputStream()) {
            while (socketController.isValid()) {
                final ServerProcessContext take;
                try {
                    take = preparedResponseBQ.take();
                } catch (InterruptedException e) {
                    log.debug("socket {} handleSocketOutput InterruptedException occurred", socketController.getRemoteInetAddress().toString());
                    throw new RuntimeException(e);
                }
                synchronized (out) {
                    isoChannelSerializer.writeIsoPayloadBytes(take.getOutputBytes(), out);
                    /*totalWriteMetricCounter.increment();*/
                    socketController.offerMessageSent();
                }
            }
        } catch (IOException e) {
            if (e instanceof SocketException && ((SocketException) e).getMessage().equals("Socket closed")) {
                log.warn("socket {} closed", socketController.getRemoteInetAddress().toString());
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    private void takeAndHandleReceivedRequest(SocketController socketController) {
        while (socketController.isValid()) {
            final ServerProcessContext take;
            try {
                take = receivedRequestBQ.take();
            } catch (InterruptedException e) {
                log.debug("socket {} handleInputQueue InterruptedException occurred", socketController.getRemoteInetAddress().toString());
                throw new RuntimeException(e);
            }
            try {
                ISOMsg isoMsg = isoChannelSerializer.readIsoMsg(take.getInputBytes(), isoPackagerType);
                if (logHexInOut) {
                    log.info("in-req {}: {}-{}", take.getCorrelationId(),
                            HexUtility.bytesToHex(isoMsg.getHeader()),
                            HexUtility.bytesToHex(take.getInputBytes()));
                }

                ISOMsg handle;
                try {
                    handle = isoRequestHandler.handle(take.getCorrelationId(), isoMsg);
                } catch (Throwable e) {
                    log.error("{}: {}", take.getCorrelationId(), e.getMessage());
                    handle = isoRequestHandler.handleException(
                            take.getCorrelationId(), isoMsg, null, e);
                }
                byte[] header;
                if (responseHeaderProvideType == ResponseHeaderProvideType.PROVIDE_BY_PROVIDER_BEAN) {
                    header = isoHeaderProvider.provide(isoMsg, handle);
                } else {
                    header = handle.getHeader();
                }
                ISOPayload responseIsoPayload = new ISOPayload(header, handle, isoPackagerType);

                if (logHexInOut) {
                    log.info("handled-res {}: {}-{}", take.getCorrelationId(),
                            HexUtility.bytesToHex(responseIsoPayload.headerBytes()),
                            HexUtility.bytesToHex(responseIsoPayload.messageBytes()));
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                isoChannelSerializer.writeIsoPayload(responseIsoPayload, byteArrayOutputStream);

                take.setOutputBytes(byteArrayOutputStream.toByteArray());
                preparedResponseBQ.offer(take);
            } catch (Throwable e) {
                log.debug("socket {} an exception occurred", socketController.getRemoteInetAddress().toString(), e);
            }
        }
    }

    @Override
    public void notifyClose(Socket socket) {
        handleSocketInputFuture.cancel(true);
        handleInputQueueFuture.cancel(true);
        handleSocketOutputFuture.cancel(true);
        closeNotifier.notify(this);
    }
}
