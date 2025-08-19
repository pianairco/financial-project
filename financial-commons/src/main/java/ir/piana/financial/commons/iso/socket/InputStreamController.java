package ir.piana.financial.commons.iso.socket;

import ir.piana.financial.commons.types.SocketConnectionType;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;

class InputStreamController extends InputStream {
    private final BlockingQueue<SocketStateQueueDto> socketStateQueue;
    private final SocketConnectionType socketConnectionType;
    private final InputStream inputStream;

    public InputStreamController(
            InputStream inputStream,
            BlockingQueue<SocketStateQueueDto> socketStateQueue,
            SocketConnectionType socketConnectionType) {
        this.inputStream = inputStream;
        this.socketStateQueue = socketStateQueue;
        this.socketConnectionType = socketConnectionType;
    }

    @Override
    public int read() throws IOException {
        int read = this.inputStream.read();
        if (read == -1) {
            socketStateQueue.offer(new SocketStateQueueDto(SocketStateQueueDto.SocketState.ALREADY_CLOSED));
        } else {
            socketStateQueue.offer(new SocketStateQueueDto(SocketStateQueueDto.SocketState.READ));
        }
        return read;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int read = read(b);
        if (read == -1) {
            socketStateQueue.offer(new SocketStateQueueDto(SocketStateQueueDto.SocketState.ALREADY_CLOSED));
        } else {
            socketStateQueue.offer(new SocketStateQueueDto(SocketStateQueueDto.SocketState.READ));
        }
        return read;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int read = inputStream.read(b, off, len);
        if (read == -1) {
            socketStateQueue.offer(new SocketStateQueueDto(SocketStateQueueDto.SocketState.ALREADY_CLOSED));
        } else {
            socketStateQueue.offer(new SocketStateQueueDto(SocketStateQueueDto.SocketState.READ));
        }
        return read;
    }

    @Override
    public byte[] readAllBytes() throws IOException {
        throw new RuntimeException("NotSupported");
        /*byte[] read = inputStream.readAllBytes();
        socketStateQueue.offer(new SocketStateQueueDto(SocketStateQueueDto.SocketState.READ));
        return read;*/
    }

    @Override
    public byte[] readNBytes(int len) throws IOException {
        byte[] bytes = inputStream.readNBytes(len);
        if (bytes.length == 0) {
            socketStateQueue.offer(new SocketStateQueueDto(SocketStateQueueDto.SocketState.ALREADY_CLOSED));
        } else {
            socketStateQueue.offer(new SocketStateQueueDto(SocketStateQueueDto.SocketState.READ));
        }
        return bytes;
    }

    @Override
    public int readNBytes(byte[] b, int off, int len) throws IOException {
        int n = inputStream.readNBytes(b, off, len);
        if (n == 0) {
            socketStateQueue.offer(new SocketStateQueueDto(SocketStateQueueDto.SocketState.ALREADY_CLOSED));
        } else {
            socketStateQueue.offer(new SocketStateQueueDto(SocketStateQueueDto.SocketState.READ));
        }
        return n;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
        socketStateQueue.offer(new SocketStateQueueDto(SocketStateQueueDto.SocketState.IS_CLOSED));
    }
}
