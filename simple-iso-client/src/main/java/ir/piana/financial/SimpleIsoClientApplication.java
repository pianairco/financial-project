package ir.piana.financial;

import ir.piana.financial.commons.boot.iso.channel.configs.ChannelInfoConf;
import ir.piana.financial.commons.boot.iso.client.config.ClientInfoConf;
import ir.piana.financial.commons.errors.FinancialWrappedException;
import ir.piana.financial.commons.iso.RequestResponseMatchKeyProvider;
import ir.piana.financial.commons.iso.channel.ISOChannelSerializer;
import ir.piana.financial.commons.iso.client.ISOSocketControllerAsClient;
import ir.piana.financial.commons.iso.header.ISOHeaderProcessor;
import ir.piana.financial.commons.iso.header.ISOHeaderProvider;
import ir.piana.financial.commons.iso.header.impl.Shetab7V98ISOHeaderProcessor;
import ir.piana.financial.commons.iso.server.ISOMsgRequestHandler;
import ir.piana.financial.commons.iso.server.ISOServerSocketManager;
import ir.piana.financial.commons.iso.server.config.ISOServerInfo;
import ir.piana.financial.commons.iso.server.console.ISOConsoleHandler;
import ir.piana.financial.commons.types.ISOPackagerType;
import ir.piana.financial.commons.types.ResponseHeaderProvideType;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication(scanBasePackages = "ir.piana.financial")
@EnableConfigurationProperties(value = {
        ChannelInfoConf.class,
        ClientInfoConf.class,
//        SimpleIsoClientApplication.MyChannelInfo.class,
//        SimpleIsoClientApplication.MyISOClientInfo.class,
        SimpleIsoClientApplication.MyConsoleHandlerInfo.class
})
public class SimpleIsoClientApplication {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) throws ISOException {
        SpringApplication.run(SimpleIsoClientApplication.class, args);
    }

    /*@ConfigurationProperties(prefix = "iso.channel-info")
    public static class MyChannelInfo extends ChannelInfo {
    }*/

    /*@Bean
    ISOChannelProvider isoChannelProvider(
            ConfigurableApplicationContext applicationContext,
            ChannelInfo channelInfo, ChannelInfoConf channelInfoConf) throws FinancialWrappedException {
        ISOChannelProvider isoChannelProvider = ISOChannelProvider.create(channelInfo);

        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();

        isoChannelProvider.getChannelSerializerEntry().forEach(entry -> {
            beanFactory.registerSingleton(entry.getKey(), entry.getValue());
        });

        return isoChannelProvider;
    }*/

    @Bean("shetab-7-iso-header-processor")
    ISOHeaderProcessor shetab7HeaderProcessor() {
        return new Shetab7V98ISOHeaderProcessor();
    }

    @Bean("shetab-7-iso-header-provider")
    ISOHeaderProvider shetab7HeaderProvider() {
        return new ISOHeaderProvider() {
            @Override
            public byte[] provide(ISOMsg requestIsoMsg, ISOMsg responseIsoMsg) {
                return new String("00008000000000589463000009500021D000000000000000000000000").getBytes(StandardCharsets.ISO_8859_1);
            }
        };
    }

    @Bean("shetab-7-match-key-provider")
    RequestResponseMatchKeyProvider matchKeyProviderBean() {
        return new RequestResponseMatchKeyProvider() {
            @Override
            public String matchKey(ISOMsg isoMsg) {
                if (isoMsg == null) {
                    return "";
                }
                return isoMsg.getString(7).trim()
                        .concat(isoMsg.getString(32).trim())
                        .concat(isoMsg.getString(37).trim())
                        .concat(isoMsg.getString(41).trim())
                        .concat(isoMsg.getString(42).trim());
            }
        };
    }

    /*@ConfigurationProperties(prefix = "iso.client-info")
    public static class MyISOClientInfo extends ISOClientInfo {
    }*/

    /*@Bean
    ISOSocketControllerAsClient isoClientSocketManager(
            MyISOClientInfo isoClientInfo, ApplicationContext applicationContext) throws IOException {
        return ISOSocketControllerAsClient.createAndStart(
                isoClientInfo.getHost(),
                isoClientInfo.getPort(),
                SocketConnectionType.byType(isoClientInfo.getSocketConnectionType()),
                isoClientInfo.getDurationInMillis(),
                applicationContext.getBean(isoClientInfo.getMatchKeyProviderBean(), RequestResponseMatchKeyProvider.class),
                applicationContext.getBean(isoClientInfo.getChannelName(), ISOChannelSerializer.class),
                ISOPackagerType.byName(isoClientInfo.getPackagerType()),
                isoClientInfo.getHeaderProviderBean() == null ? null :
                        applicationContext.getBean(isoClientInfo.getHeaderProviderBean(), ISOHeaderProvider.class),
                isoClientInfo.getHeaderProcessorBean() == null ? null :
                        applicationContext.getBean(isoClientInfo.getHeaderProcessorBean(), ISOHeaderProcessor.class),
                isoClientInfo.getLogHexOutIn()
        );
    }*/

    /*@Bean
    ISOMsgRequestHandler isoMessageRequestHandler(
            ApplicationContext applicationContext*//*, ISOSocketControllerAsClient isoSocketControllerAsClient*//*) {
        ISOSocketControllerAsClient isoSocketControllerAsClient = applicationContext.getBean(
                "iso_client_localhost_1023", ISOSocketControllerAsClient.class);
        return new ISOMsgRequestHandler() {
            @Override
            public ISOMsg handle(String correlationId, ISOMsg isoMsg) throws FinancialWrappedException {
                try {
                    Thread.sleep(10 * 60 * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                ISOMsg received = isoSocketControllerAsClient.send(isoMsg);

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                ISOMsg received2 = isoSocketControllerAsClient.send(isoMsg);
                return received;
            }

            @Override
            public ISOMsg handleException(String correlationId, ISOMsg requestIsoMsg, ISOMsg responseIsoMsg, Throwable throwable) throws FinancialWrappedException {
                return null;
            }
        };
    }*/

    @ConfigurationProperties(prefix = "iso.console-handler-info")
    public static class MyConsoleHandlerInfo extends ISOServerInfo {
    }

    /*@Bean
    ISOConsoleHandler isoConsoleHandler(MyConsoleHandlerInfo consoleHandlerInfo, ApplicationContext applicationContext) {
        return new ISOConsoleHandler(
                applicationContext.getBean(consoleHandlerInfo.getChannelName(), ISOChannelSerializer.class),
                ISOPackagerType.byName(consoleHandlerInfo.getPackagerType()),
                ResponseHeaderProvideType.from(consoleHandlerInfo.getResponseHeaderProvideType()),
                applicationContext.getBean(consoleHandlerInfo.getHeaderProviderBean(), ISOHeaderProvider.class),
                applicationContext.getBean(consoleHandlerInfo.getHeaderProcessorBean(), ISOHeaderProcessor.class),
                applicationContext.getBean(consoleHandlerInfo.getMessageHandlerBean(), ISOMsgRequestHandler.class),
                consoleHandlerInfo.getLogHexInOut()
        );
    }*/

    @Bean
    CommandLineRunner commandLineRunner(
            @Qualifier("shetab-7-ascii-channel-v93") ISOChannelSerializer isoChannelSerializer,
            ISOSocketControllerAsClient isoSocketControllerAsClient) {
        return args -> {
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

            ISOMsg isoMsg = isoChannelSerializer.readIsoMsg(new ByteArrayInputStream(bytes), ISOPackagerType.byName("shetab-7-ascii-v93"));

            ISOMsg received = isoSocketControllerAsClient.send(isoMsg);

            log.info("response is {}", received.hexString());

//            byte[] processed = isoConsoleHandler.process(new ByteArrayInputStream(bytes));

//            log.info("response is {}", new String(processed, StandardCharsets.ISO_8859_1));

    /*ISOPayload isoPayload = isoChannelSerializer.readIsoPayload(inputStream).createNewByIsoMsg(ISOPackagerType.SHETAB7_ASCII_V93);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    isoChannelSerializer.writeIsoPayload(isoPayload, outputStream);
    byte[] byteArray = outputStream.toByteArray();
    String s = new String(byteArray, StandardCharsets.ISO_8859_1);*/
            System.out.println("Starting Simple ISO Client Application...");
        };
    }

}
