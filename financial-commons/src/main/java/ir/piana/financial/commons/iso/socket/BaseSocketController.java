package ir.piana.financial.commons.iso.socket;

import ir.piana.financial.commons.types.SocketConnectionType;
import ir.piana.financial.commons.utilities.SocketUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

abstract sealed class BaseSocketController implements SocketController
        permits OneTimeSocketController, AfterIdleTimeoutSocketController, PermanentSocketController {
    protected static final Logger log = LoggerFactory.getLogger(BaseSocketController.class);
    protected final Socket socket;
    private final SocketConnectionType socketConnectionType;
    protected final BlockingQueue<SocketStateQueueDto> socketStateQueue = new LinkedBlockingQueue<>();
    private final long durationInMillis;
    private final boolean logHexInOut;
    private String uniqueId;

    private final List<SocketCloseNotifier> closeNotifiers = new ArrayList<>();

    public BaseSocketController(
            Socket socket,
            SocketConnectionType socketConnectionType,
            long durationInMillis,
            boolean logHexInOut) {
        this.uniqueId = SocketUtility.generateSocketId(socket);
        this.socket = socket;
        this.socketConnectionType = socketConnectionType;
        this.durationInMillis = durationInMillis;
        this.logHexInOut = logHexInOut;
        Executors.newVirtualThreadPerTaskExecutor().execute(() -> {
            boolean proceed = true;
            while (proceed) {
                try {
                    SocketStateQueueDto socketStateQueueDto = socketStateQueue.take();
                    /*if (socketStateQueueDto.socketState() == SocketStateQueueDto.SocketState.OS_CLOSED)
                        log.info("socket {} closed", uniqueId);*/
                    if (socketStateQueueDto.socketState() == SocketStateQueueDto.SocketState.ALREADY_CLOSED) {
                        close();
                    } else {
                        captureChangeState(socketStateQueueDto);
                    }
                } catch (Throwable e) {
                    log.error(e.getMessage(), e);
                } finally {
                    proceed = !this.socket.isClosed();
                }
            }
        });
    }

    @Override
    public final InputStream getInputStream() throws IOException {
        return new InputStreamController(socket.getInputStream(), socketStateQueue, socketConnectionType);
//        return new InputStreamController(new BufferedInputStream(socket.getInputStream()), socketStateQueue, socketConnectionType);
    }

    @Override
    public final OutputStream getOutputStream() throws IOException {
        return new OutputStreamController(socket.getOutputStream(), socketStateQueue, socketConnectionType);
    }

    @Override
    public final InetAddress getRemoteInetAddress() {
        return this.socket.getInetAddress();
    }


    @Override
    public int getRemotePort() {
        return socket.getPort();
    }

    @Override
    public final void close() throws IOException {
        log.info("socket {} ready to close", socket.getInetAddress().toString());
        try {
            if(!this.socket.isClosed()) {
                this.socket.getOutputStream().flush();
                this.socket.getOutputStream().close();
            }
        } catch (Exception e) {
            if (e instanceof SocketException && e.getMessage().equals("Socket closed")) {
                log.info("socket {} already closed", socket.getInetAddress().toString());
            } else
                log.error(e.getMessage(), e);
        }
        /*try {
            this.socket.getInputStream().close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }*/
        try {
            if (!socket.isClosed()) {
                this.socket.close();
            }
        } catch (Exception e) {
            if (e instanceof SocketException && e.getMessage().equals("Socket closed")) {
                log.info("socket {} already closed", socket.getInetAddress().toString());
            } else
                log.error(e.getMessage(), e);
        }

        this.closeNotifiers.forEach(n -> {
            n.notifyClose(socket);
        });
    }

    @Override
    public void offerMessageReceived() {
        this.captureChangeState(new SocketStateQueueDto(SocketStateQueueDto.SocketState.RECEIVED));
//        socketStateQueue.offer(new SocketStateQueueDto(SocketStateQueueDto.SocketState.RECEIVED));
    }

    @Override
    public void offerMessageSent() {
        this.captureChangeState(new SocketStateQueueDto(SocketStateQueueDto.SocketState.SENT));
//        return this.socketStateQueue.offer(new SocketStateQueueDto(SocketStateQueueDto.SocketState.SENT));
    }

    @Override
    public final void addCloseNotifier(SocketCloseNotifier socketCloseNotifier) {
        this.closeNotifiers.add(socketCloseNotifier);
    }

    abstract void captureChangeState(SocketStateQueueDto socketStateQueueDto);
}
