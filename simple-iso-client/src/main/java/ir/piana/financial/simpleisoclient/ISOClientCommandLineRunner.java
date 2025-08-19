package ir.piana.financial.simpleisoclient;

import ir.piana.financial.commons.iso.client.ISOSocketControllerAsClient;
import ir.piana.financial.commons.iso.server.console.ISOConsoleHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//@Component
//@DependsOn({"isoChannelProvider", "isoClientProvider"})
public class ISOClientCommandLineRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(ISOClientCommandLineRunner.class);

    private final ISOConsoleHandler isoConsoleHandler;

    public ISOClientCommandLineRunner(
            ISOConsoleHandler isoConsoleHandler) {
        this.isoConsoleHandler = isoConsoleHandler;
    }

    @Override
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

        log.info("request is {}", new String(bytes, StandardCharsets.ISO_8859_1));

        byte[] processed = isoConsoleHandler.process(new ByteArrayInputStream(bytes));

        log.info("response is {}", new String(processed, StandardCharsets.ISO_8859_1));

        /*ISOPayload isoPayload = isoChannelSerializer.readIsoPayload(inputStream).createNewByIsoMsg(ISOPackagerType.SHETAB7_ASCII_V93);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        isoChannelSerializer.writeIsoPayload(isoPayload, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        String s = new String(byteArray, StandardCharsets.ISO_8859_1);*/
        System.out.println("Starting Simple ISO Client Application...");
    }
}