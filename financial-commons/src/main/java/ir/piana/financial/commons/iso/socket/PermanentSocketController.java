package ir.piana.financial.commons.iso.socket;

import ir.piana.financial.commons.types.SocketConnectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;

final class PermanentSocketController extends BaseSocketController {
    private static final Logger log = LoggerFactory.getLogger(PermanentSocketController.class);

    public PermanentSocketController(
            Socket socket,
            SocketConnectionType socketConnectionType,
            boolean logHexInOut) {
        super(socket, socketConnectionType, 0, logHexInOut);
    }

    @Override
    void captureChangeState(SocketStateQueueDto socketStateQueueDto) {
        log.debug("{} captured", socketStateQueueDto.socketState());
    }

    @Override
    public boolean isValid() {
        return !socket.isClosed();
    }
}
