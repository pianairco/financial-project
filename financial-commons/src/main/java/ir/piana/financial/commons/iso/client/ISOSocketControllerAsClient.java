package ir.piana.financial.commons.iso.client;

import ir.piana.financial.commons.errors.FinancialExceptionType;
import ir.piana.financial.commons.errors.FinancialWrappedException;
import ir.piana.financial.commons.iso.RequestResponseMatchKeyProvider;
import ir.piana.financial.commons.iso.channel.ISOChannelSerializer;
import ir.piana.financial.commons.iso.channel.ISOPayload;
import ir.piana.financial.commons.iso.header.ISOHeaderProcessor;
import ir.piana.financial.commons.iso.header.ISOHeaderProvider;
import ir.piana.financial.commons.iso.socket.SocketCloseNotifier;
import ir.piana.financial.commons.iso.socket.SocketController;
import ir.piana.financial.commons.structs.BlockingMap;
import ir.piana.financial.commons.types.ISOPackagerType;
import ir.piana.financial.commons.types.SocketConnectionType;
import ir.piana.financial.commons.utilities.HexUtility;
import org.jpos.iso.ISOMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.*;

public final class ISOSocketControllerAsClient implements Closeable, SocketCloseNotifier {
    private final Logger log;
    private final RequestResponseMatchKeyProvider matchKeyProvider;
    private final ISOChannelSerializer isoChannelSerializer;
    private final ISOPackagerType isoPackagerType;
    private final ISOHeaderProvider isoHeaderProvider;
    private final ISOHeaderProcessor isoHeaderProcessor;
    private final BlockingMap<ISOMsg> isoMsgBlockingMap;

    private final boolean logHexOutIn;
    private final SocketController socketController;

    private ISOSocketControllerAsClient(
            SocketController socketController,
            RequestResponseMatchKeyProvider matchKeyProvider,
            ISOChannelSerializer isoChannelSerializer,
            ISOPackagerType isoPackagerType,
            ISOHeaderProvider isoHeaderProvider,
            ISOHeaderProcessor isoHeaderProcessor,
            boolean logHexOutIn) {
        this.log = LoggerFactory.getLogger(String.format("client-to-%s-%d",
                socketController.getRemoteInetAddress().getHostAddress(), socketController.getRemotePort()));
        this.socketController = socketController;
        this.matchKeyProvider = matchKeyProvider;
        this.isoChannelSerializer = isoChannelSerializer;
        this.isoPackagerType = isoPackagerType;
        this.isoHeaderProvider = isoHeaderProvider;
        this.isoHeaderProcessor = isoHeaderProcessor;
        this.isoMsgBlockingMap = new BlockingMap<>(2000);
        this.logHexOutIn = logHexOutIn;
    }

    public static ISOSocketControllerAsClient createAndStart(
            String host,
            int port,
            SocketConnectionType socketConnectionType,
            long durationInMillis,
            RequestResponseMatchKeyProvider matchKeyProvider,
            ISOChannelSerializer isoChannelSerializer,
            ISOPackagerType isoPackagerType,
            ISOHeaderProvider isoHeaderProvider,
            ISOHeaderProcessor isoHeaderProcessor,
            boolean logHexOutIn
    ) throws IOException {
        ISOSocketControllerAsClient isoSocketControllerAsClient = new ISOSocketControllerAsClient(
                SocketController.createSocketController(
                        new Socket(host, port), socketConnectionType, durationInMillis, logHexOutIn),
                matchKeyProvider,
                isoChannelSerializer,
                isoPackagerType,
                isoHeaderProvider,
                isoHeaderProcessor,
                logHexOutIn);
        isoSocketControllerAsClient.start();
        return isoSocketControllerAsClient;
    }

    private Future<?> handleSocketOutputFuture;
    private Future<?> handleSocketInputFuture;
    private Future<?> handleInputQueueFuture;

    private void start() {
        try {
            ThreadFactory factory = Thread.ofVirtual()
                    .name("piana-v-thread-", 0)
                    .uncaughtExceptionHandler((t, e) -> {
                        log.debug("exception occurred: {}", e.getMessage());
                        // Do nothing — suppress logging
                        // Or log somewhere else as needed
                    })
                    .factory();
            ExecutorService executorService = Executors.newThreadPerTaskExecutor(factory);

            handleSocketOutputFuture = executorService.submit(
                    () -> takeAndSendPreparedRequest(socketController));
            handleSocketInputFuture = executorService.submit(
                    () -> readClientInputStreamAndOfferReceivedResponse(socketController));
            handleInputQueueFuture = executorService.submit(
                    () -> takeAndHandleReceivedResponse(socketController));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            try {
                socketController.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public ISOMsg send(ISOMsg isoMsg) throws FinancialWrappedException {
        byte[] header;
        if (isoHeaderProvider != null) {
            header = isoHeaderProvider.provide(isoMsg, null);
        } else {
            header = isoMsg.getHeader();
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            isoChannelSerializer.writeIsoPayload(new ISOPayload(header, isoMsg, isoPackagerType), outputStream);
            preparedRequestBQ.offer(outputStream.toByteArray());

            try {
                ISOMsg take = isoMsgBlockingMap.take(matchKeyProvider.matchKey(isoMsg));
                return take;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw FinancialExceptionType.ISOError.returnException(e.getMessage());
        }
    }

    private final BlockingQueue<byte[]> preparedRequestBQ = new LinkedBlockingQueue<>();
    private final BlockingQueue<byte[]> receivedResponseBQ = new LinkedBlockingQueue<>();

    private void readClientInputStreamAndOfferReceivedResponse(SocketController socketController) {
        try (InputStream in = socketController.getInputStream()) {
            while (socketController.isValid()) {
                byte[] bytes = isoChannelSerializer.readIsoPayloadBytes(in);
                socketController.offerMessageReceived();
                if (logHexOutIn) {
                    log.info("in-req {}", HexUtility.bytesToHex(bytes));
                }
                receivedResponseBQ.offer(bytes);
            }
        } catch (IOException e) {
            if (e instanceof SocketException && ((SocketException) e).getMessage().equals("Socket closed")) {
                log.warn("socket {} closed", socketController.getRemoteInetAddress().toString());
            } else {
                log.error("socket {} closed", socketController.getRemoteInetAddress().toString());
                /*throw new RuntimeException(e);*/
            }
        }
    }

    private void takeAndSendPreparedRequest(SocketController socketController) {
        try (OutputStream out = socketController.getOutputStream()) {
            while (socketController.isValid()) {
                final byte[] take;
                try {
                    take = preparedRequestBQ.take();
                    synchronized (out) {
                        isoChannelSerializer.writeIsoPayloadBytes(take, out);
                        socketController.offerMessageSent();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            if (e instanceof SocketException && ((SocketException) e).getMessage().equals("Socket closed")) {
                log.warn("socket {} closed", socketController.getRemoteInetAddress().toString());
            }
            throw new RuntimeException(e);
        }
    }

    private void takeAndHandleReceivedResponse(SocketController socketController) {
        while (socketController.isValid()) {
            try {
                byte[] take = receivedResponseBQ.take();
                if (take == null)
                    return;

                ISOMsg isoMsg = isoChannelSerializer.readIsoMsg(take, isoPackagerType);
                if (logHexOutIn) {
                    log.info("in-req header: {}, totalBytes: {}",
                            isoMsg.getHeader() == null ? "" : HexUtility.bytesToHex(isoMsg.getHeader()),
                            HexUtility.bytesToHex(take));
                }

                String matchKey = matchKeyProvider.matchKey(isoMsg);
                isoMsgBlockingMap.offer(matchKey, isoMsg);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (FinancialWrappedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void close() throws IOException {
        socketController.close();
    }

    @Override
    public void notifyClose(Socket socket) {
        handleSocketOutputFuture.cancel(true);
        handleSocketInputFuture.cancel(true);
        handleInputQueueFuture.cancel(true);
    }
}
