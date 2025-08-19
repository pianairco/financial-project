package ir.piana.financial.simpleisoclient;

import ir.piana.financial.commons.iso.server.console.ISOConsoleHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//@Component
//@DependsOn("isoChannelProvider")
public class MyCommandLineRunner /*implements CommandLineRunner*/ {
    private static final Logger log = LoggerFactory.getLogger(MyCommandLineRunner.class);

    private final ISOConsoleHandler isoConsoleHandler;

    public MyCommandLineRunner(ISOConsoleHandler isoConsoleHandler) {
        this.isoConsoleHandler = isoConsoleHandler;
    }

    /*@Override*/
    public void run(String... args) throws Exception {
        Map<String, String> map = Stream.of(args).map(arg -> {
            int i = arg.indexOf("=");
            String key = arg.substring(0, i);
            String value = arg.substring(i + 1);
            return new String[]{key, value};
        }).collect(Collectors.toMap(
                parts -> parts[0], // Key mapper: the first element of the array
                parts -> parts[1]  // Value mapper: the second element of the array
        ));

        String toHost = map.get("to-host");
        String toPort = map.get("to-port");
        byte[] bytes = map.get("iso-msg").getBytes(StandardCharsets.ISO_8859_1);

        try (Socket socket = new Socket(toHost, Integer.parseInt(toPort))) {
            // Connection established
            log.info("Connected to server: {}:{}", toHost, toPort);

            try (OutputStream os = socket.getOutputStream()) {
                os.write(bytes);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }

            try (InputStream is = socket.getInputStream()) {

            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }

        InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(bytes));

        byte[] outputByteArray = isoConsoleHandler.process(inputStream);
        String s = new String(outputByteArray, StandardCharsets.ISO_8859_1);

        /*ISOPayload isoPayload = isoChannelSerializer.readIsoPayload(inputStream).createNewByIsoMsg(ISOPackagerType.SHETAB7_ASCII_V93);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        isoChannelSerializer.writeIsoPayload(isoPayload, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        String s = new String(byteArray, StandardCharsets.ISO_8859_1);*/
        System.out.println("Starting Simple ISO Client Application...");
    }
}