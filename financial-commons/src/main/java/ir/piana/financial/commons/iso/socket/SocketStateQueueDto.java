package ir.piana.financial.commons.iso.socket;

import java.util.Date;

public record SocketStateQueueDto(
        SocketState socketState, Date date) {
    public SocketStateQueueDto(SocketState socketState) {
        this(socketState, new Date());
    }

    public enum SocketState {
        READ,
        RECEIVED,
        WRITE,
        SENT,
        OS_CLOSED,
        IS_CLOSED,
        ALREADY_CLOSED,
        ;
    }
}
