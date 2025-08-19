package ir.piana.financial.commons.iso.socket;

import ir.piana.financial.commons.types.SocketConnectionType;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

final class OneTimeSocketController extends BaseSocketController {
    private AtomicBoolean oneSend = new  AtomicBoolean(false);
    private AtomicBoolean oneReceive = new  AtomicBoolean(false);

    public OneTimeSocketController(
            Socket socket,
            SocketConnectionType socketConnectionType,
            long durationInMillis,
            boolean logHexInOut) {
        super(socket, socketConnectionType, durationInMillis, logHexInOut);
    }

    @Override
    void captureChangeState(SocketStateQueueDto socketStateQueueDto) {
        log.debug("{} captured", socketStateQueueDto.socketState());
        switch (socketStateQueueDto.socketState()) {
            case RECEIVED:
                oneReceive.set(true);
                break;
            case SENT:
                oneSend.set(true);
                break;
        }
        if (oneSend.get() && oneReceive.get()) {
            try {
                log.debug("OneTimeSocketController ready to close its underling socket");
                close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean isValid() {
        log.debug("oneTimeSocket isValid {}", (!oneReceive.get() || !oneSend.get()) && !socket.isClosed());
        return (!oneReceive.get() || !oneSend.get()) && !socket.isClosed();
    }
}
