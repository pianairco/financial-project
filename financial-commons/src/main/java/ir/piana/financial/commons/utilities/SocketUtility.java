package ir.piana.financial.commons.utilities;

import java.net.Socket;

public class SocketUtility {
    public class SimpleSocketId {
        public static String getSocketId(Socket socket) {
            return socket.getLocalAddress().getHostAddress() + ":" +
                    socket.getLocalPort() + "-" +
                    socket.getInetAddress().getHostAddress() + ":" +
                    socket.getPort();
        }
    }

    public static String generateSocketId(Socket socket) {
        if (socket == null || socket.isClosed()) {
            return "INVALID_SOCKET";
        }

        // Get socket properties
        String localAddr = socket.getLocalAddress().getHostAddress();
        int localPort = socket.getLocalPort();
        String remoteAddr = socket.getInetAddress().getHostAddress();
        int remotePort = socket.getPort();
        long timestamp = System.currentTimeMillis();

        // Create a unique string combining these properties
        String socketInfo = String.format("%s:%d-%s:%d-%d",
                localAddr, localPort, remoteAddr, remotePort, timestamp);

        // Hash the string to create a fixed-length ID
        return HashUtility.getSHA256Hash(socketInfo);
    }
}
