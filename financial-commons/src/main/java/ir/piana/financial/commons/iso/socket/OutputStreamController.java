package ir.piana.financial.commons.iso.socket;

import ir.piana.financial.commons.iso.socket.SocketStateQueueDto.SocketState;
import ir.piana.financial.commons.types.SocketConnectionType;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;

class OutputStreamController extends OutputStream {
    private final BlockingQueue<SocketStateQueueDto> socketStateQueue;
    private final SocketConnectionType socketConnectionType;
    private final OutputStream outputStream;

    public OutputStreamController(
            OutputStream outputStream,
            BlockingQueue<SocketStateQueueDto> socketStateQueue,
            SocketConnectionType socketConnectionType) {
        this.socketStateQueue = socketStateQueue;
        this.socketConnectionType = socketConnectionType;
        this.outputStream = outputStream;
    }

    @Override
    public void write(int b) throws IOException {
        this.outputStream.write(b);
        socketStateQueue.offer(new SocketStateQueueDto(SocketState.WRITE));
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.outputStream.write(b, off, len);
        socketStateQueue.offer(new SocketStateQueueDto(SocketState.WRITE));
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.outputStream.write(b);
        socketStateQueue.offer(new SocketStateQueueDto(SocketState.WRITE));
    }

    @Override
    public void close() throws IOException {
        this.outputStream.close();
        this.socketStateQueue.offer(new SocketStateQueueDto(SocketState.OS_CLOSED));
    }
}
