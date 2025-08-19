package ir.piana.financial.commons.iso.socket;

import ir.piana.financial.commons.types.SocketConnectionType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

public sealed interface SocketController permits BaseSocketController {
    InputStream getInputStream() throws IOException;
    OutputStream getOutputStream() throws IOException;
    InetAddress getRemoteInetAddress();
    int getRemotePort();
    void close() throws IOException;
    boolean isValid();

    void offerMessageReceived();
    void offerMessageSent();
//    void handle();

    void addCloseNotifier(SocketCloseNotifier socketCloseNotifier);

    static SocketController createSocketController(
            final Socket socket,
            SocketConnectionType socketConnectionType,
            long durationInMillis,
            /*RequestResponseMatchKeyProvider matchKeyProvider,
            ISOChannelSerializer isoChannelSerializer,
            ISOPackagerType isoPackagerType,
            ISOHeaderProcessor isoHeaderProcessor,
            ISOMsgRequestHandler isoRequestHandler,*/
            boolean logHexInOut) throws SocketException {
        socket.setSoTimeout((int)durationInMillis);
        return switch (socketConnectionType) {
            case AFTER_IDLE_TIMEOUT -> new AfterIdleTimeoutSocketController(socket, socketConnectionType, durationInMillis, logHexInOut);
            case PERMANENT ->  new PermanentSocketController(socket, socketConnectionType, logHexInOut);
            /*case DEFAULT,  ONE_TIME -> new OneTimeSocketController(socket, socketConnectionType, durationInMillis, matchKeyProvider,
                    isoChannelSerializer, isoPackagerType, isoHeaderProcessor, isoRequestHandler, logHexInOut);*/
            default -> new OneTimeSocketController(socket, socketConnectionType, durationInMillis, logHexInOut);
        };
    }
}
