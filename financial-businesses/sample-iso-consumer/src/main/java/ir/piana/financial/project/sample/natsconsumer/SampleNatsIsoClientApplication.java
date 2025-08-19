package ir.piana.financial.project.sample.natsconsumer;

import io.nats.client.JetStreamApiException;
import ir.piana.financial.broker.messagebrokers.MessageBrokersService;
import ir.piana.financial.broker.messagebrokers.conf.MessageBrokersConfig;
import ir.piana.financial.commons.types.Shetab7Version93Fields;
import org.jpos.iso.ISOBasePackager;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.packager.ISO87BPackager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
@EnableConfigurationProperties(value = {
        SampleNatsIsoClientApplication.MyMessageBrokersConfig.class
})
public class SampleNatsIsoClientApplication {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) throws ISOException {


        ISOBasePackager isoPackager = Shetab7Version93Fields.getIsoAsciiPackager();
//        ISOBasePackager isoPackager = new ISO87APackager();
        byte[] bytes = new String("02007670A541A8E1B815166280231390019650500000000000103000000000103000072813123300000001266818250707080000072836421140121324C280000009581672100069500013762802FA6BB214B0B2C62EA76EF9E434EA19120693820360982100001121001601000000667Refah Bank            Tehran       THRIR010010157171371502184852851062      00        TC02111800000023720692114518000000000064530239364364CF1D605FB4071F8A05020201020208000000000000000000000000000000000000000000000000000000000000000000000000000000000073020002222000000000171321.52.126.10300320667F6111228E1039196E028F036724000A9A20A98F8F4394C").getBytes();
        ISOMsg isoMsg = new ISOMsg();
        isoPackager.unpack(isoMsg, bytes);


        SpringApplication.run(SampleNatsIsoClientApplication.class, args);
    }

    @ConfigurationProperties(prefix = "message-brokers")
    public static class MyMessageBrokersConfig extends MessageBrokersConfig {
    }

    @Bean
    ISOPackager isoPackager() {
        return new ISO87BPackager();
    }

    @Bean
    MessageBrokersService messageBrokersService(
            ApplicationContext applicationContext,
            MessageBrokersConfig messageBrokersConfig
    ) throws JetStreamApiException, IOException, InterruptedException {
        return new MessageBrokersService(messageBrokersConfig, applicationContext);
    }
}
