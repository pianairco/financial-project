package ir.piana.financial.commons.iso.server;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import ir.piana.financial.commons.iso.RequestResponseMatchKeyProvider;
import ir.piana.financial.commons.iso.channel.ISOChannelSerializer;
import ir.piana.financial.commons.iso.header.ISOHeaderProcessor;
import ir.piana.financial.commons.iso.header.ISOHeaderProvider;
import ir.piana.financial.commons.types.ISOPackagerType;
import ir.piana.financial.commons.types.ResponseHeaderProvideType;
import ir.piana.financial.commons.types.SocketConnectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public final class ISOServerSocketManager implements Closeable {
    private final Logger log;
    private boolean running = true;

    private final boolean isDaemon;
    private final int port;
    private final SocketConnectionType socketConnectionType;
    private final long durationInMillis;
    private final RequestResponseMatchKeyProvider matchKeyProvider;
    private final ISOChannelSerializer isoChannelSerializer;
    private final ISOPackagerType isoPackagerType;
    private final ResponseHeaderProvideType responseHeaderProvideType;
    private final ISOHeaderProvider isoHeaderProvider;
    private final ISOHeaderProcessor isoHeaderProcessor;
    private final ISOMsgRequestHandler isoRequestHandler;
    private final List<String> whiteHostList;
    private final boolean logHexInOut;
    private final MeterRegistry meterRegistry;


    private final Counter totalConnectionMetricCounter;
    private final Gauge activeSocketsMetricGauge;

    private List<ISOSocketControllerAsServer> isoSocketControllerAsServers = new ArrayList<>(3000);

    private ISOServerSocketManager(
            boolean isDaemon,
            int port,
            SocketConnectionType socketConnectionType,
            long durationInMillis,
            RequestResponseMatchKeyProvider matchKeyProvider,
            ISOChannelSerializer isoChannelSerializer,
            ISOPackagerType isoPackagerType,
            ResponseHeaderProvideType responseHeaderProvideType,
            ISOHeaderProvider isoHeaderProvider,
            ISOHeaderProcessor isoHeaderProcessor,
            ISOMsgRequestHandler isoRequestHandler,
            List<String> whiteHostList,
            boolean logHexInOut,
            MeterRegistry meterRegistry) {
        this.log = LoggerFactory.getLogger(String.format("server-socket-%d", port));
        this.port = port;
        this.isDaemon = isDaemon;
        this.socketConnectionType = socketConnectionType;
        this.durationInMillis = durationInMillis;
        this.matchKeyProvider = matchKeyProvider;
        this.isoChannelSerializer = isoChannelSerializer;
        this.isoPackagerType = isoPackagerType;
        this.responseHeaderProvideType = responseHeaderProvideType;
        this.isoHeaderProvider = isoHeaderProvider;
        this.isoHeaderProcessor = isoHeaderProcessor;
        this.isoRequestHandler = isoRequestHandler;
        this.whiteHostList = whiteHostList;
        this.logHexInOut = logHexInOut;
        this.meterRegistry = meterRegistry;

        totalConnectionMetricCounter = Counter.builder(String.format("total_conn_on_server_%s", port))
                .description(String.format("total connection accepted by server on port %s", port))
//                .tags("environment", "development")
                .register(meterRegistry);

        activeSocketsMetricGauge = Gauge.builder(
                        String.format("active_sockets_on_server_%s", port),
                        isoSocketControllerAsServers::size)
                .description(String.format("active connection accepted by server on port %s", port))
                .register(meterRegistry);
    }

    public static ISOServerSocketManager createAndStart(
            boolean isDaemon,
            int port,
            SocketConnectionType socketConnectionType,
            long durationInMillis,
            RequestResponseMatchKeyProvider matchKeyProvider,
            ISOChannelSerializer isoChannelSerializer,
            ISOPackagerType isoPackagerType,
            ResponseHeaderProvideType responseHeaderProvideType,
            ISOHeaderProvider isoHeaderProvider,
            ISOHeaderProcessor ISOHeaderProcessor,
            ISOMsgRequestHandler isoMsgRequestHandler,
            List<String> whiteHostList,
            boolean logHexInOut,
            MeterRegistry meterRegistry
    ) {
        ISOServerSocketManager isoServerSocketManager = new ISOServerSocketManager(
                isDaemon, port, socketConnectionType, durationInMillis, matchKeyProvider,
                isoChannelSerializer, isoPackagerType,
                responseHeaderProvideType, isoHeaderProvider, ISOHeaderProcessor,
                isoMsgRequestHandler, whiteHostList == null ? new ArrayList<>() : whiteHostList, logHexInOut,
                meterRegistry);
        isoServerSocketManager.start();
        return isoServerSocketManager;
    }


    private void start() {
        if (!isDaemon) {
            try (ExecutorService executor = Executors.newSingleThreadExecutor(runnable -> {
                Thread thread = new Thread(runnable, "ServerSocketAcceptThread");
                thread.setDaemon(isDaemon); // Keep the JVM alive until this thread finishes = false
                return thread;
            })) {
                executor.submit(this::acceptServer);
            }
        } else {
            Executors.newVirtualThreadPerTaskExecutor().submit(this::acceptServer);
        }
    }

    private void acceptServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("Creating server socket on port {}", serverSocket.getLocalPort());
            AtomicInteger count = new AtomicInteger(0);

            while (running) {
                // Wait for a client to connect

                final Socket client = serverSocket.accept();

                Executors.newVirtualThreadPerTaskExecutor().submit(() -> {
                    totalConnectionMetricCounter.increment();
                    int nThConnection = count.incrementAndGet();
                    try {
                        if (!whiteHostList.isEmpty() && !whiteHostList.contains(client.getInetAddress().getHostAddress())) {
                            client.close();
                            log.info("{}-th connection not contained in host-white-list, host: {}",
                                    nThConnection, client.getInetAddress().getHostName());
                            return null;
                        }
                        log.info("Accepted the {}-th connection, host: {}",
                                client.getInetAddress().getHostName(), nThConnection);

                        isoSocketControllerAsServers.add(ISOSocketControllerAsServer.createAndStart(
                                client, socketConnectionType, durationInMillis,
                                matchKeyProvider, isoChannelSerializer, isoPackagerType,
                                responseHeaderProvideType, isoHeaderProvider, isoHeaderProcessor,
                                isoRequestHandler, logHexInOut, isoSocketControllerAsServer ->
                                        isoSocketControllerAsServers.remove(isoSocketControllerAsServer),
                                meterRegistry
                        ));

                        return "";
                    } catch (Exception e) {
                        if (!running) {
                            log.error("Error accepting the connection, message: {}", e.getMessage());
                        }
                        log.error("Error accepting the {}-th connection, message: {}",
                                count.incrementAndGet(), e.getMessage());
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (IOException e) {
            log.error("Could not start server on port {}, message: {}", port, e.getMessage());
        } finally {
            log.info("Server socket on port:{} stopped.", port);
        }
    }

    @Override
    public void close() throws IOException {
        running = false;
        try (Socket socket = new Socket("localhost", port)) {
            // Connection is made just to unblock the accept() call
            log.info("Made a connection to self to unblock accept().");
        } catch (IOException e) {
            log.error("Error connecting to self to unblock accept(): " + e.getMessage());
            // If the server socket is already closed or inaccessible, this might fail.
            // The 'running' flag will still cause the loop to exit on the next iteration.
        }
    }
}
