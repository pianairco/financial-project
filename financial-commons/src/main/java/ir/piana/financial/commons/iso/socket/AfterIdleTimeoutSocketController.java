package ir.piana.financial.commons.iso.socket;

import ir.piana.financial.commons.structs.DurationCheck;
import ir.piana.financial.commons.types.SocketConnectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

final class AfterIdleTimeoutSocketController extends BaseSocketController {
    private static final Logger log = LoggerFactory.getLogger(AfterIdleTimeoutSocketController.class);
//    private final DurationCheck durationCheck;

    public AfterIdleTimeoutSocketController(
            Socket socket,
            SocketConnectionType socketConnectionType,
            long durationInMillis,
            boolean logHexInOut) {
        super(socket, socketConnectionType, durationInMillis, logHexInOut);
        /*durationCheck = new DurationCheck(() -> {
            try {
                close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }, durationInMillis);*/
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
