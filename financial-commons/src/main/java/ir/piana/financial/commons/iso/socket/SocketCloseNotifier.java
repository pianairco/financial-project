package ir.piana.financial.commons.iso.socket;

import java.net.Socket;

public interface SocketCloseNotifier {
    void notifyClose(Socket socket);
}
