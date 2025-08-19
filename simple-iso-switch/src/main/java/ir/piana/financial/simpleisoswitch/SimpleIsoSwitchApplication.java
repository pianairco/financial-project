package ir.piana.financial.simpleisoswitch;

import io.micrometer.core.instrument.MeterRegistry;
import ir.piana.financial.commons.errors.FinancialWrappedException;
import ir.piana.financial.commons.iso.RequestResponseMatchKeyProvider;
import ir.piana.financial.commons.iso.channel.ISOChannelProvider;
import ir.piana.financial.commons.iso.channel.ISOChannelSerializer;
import ir.piana.financial.commons.iso.channel.configs.ChannelInfo;
import ir.piana.financial.commons.iso.header.ISOHeaderProcessor;
import ir.piana.financial.commons.iso.header.ISOHeaderProvider;
import ir.piana.financial.commons.iso.header.impl.Shetab7V98ISOHeaderProcessor;
import ir.piana.financial.commons.iso.header.impl.Shetab7V98ISOHeaderProvider;
import ir.piana.financial.commons.iso.server.ISOMsgRequestHandler;
import ir.piana.financial.commons.iso.server.ISOServerSocketManager;
import ir.piana.financial.commons.iso.server.config.ISOServerInfo;
import ir.piana.financial.commons.types.ISOPackagerType;
import ir.piana.financial.commons.types.ResponseHeaderProvideType;
import ir.piana.financial.commons.types.SocketConnectionType;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(value = {
        SimpleIsoSwitchApplication.MyChannelInfo.class,
        SimpleIsoSwitchApplication.MyIsoServerInfo.class
})
public class SimpleIsoSwitchApplication {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) throws ISOException {
        SpringApplication.run(SimpleIsoSwitchApplication.class, args);
    }

    @ConfigurationProperties(prefix = "iso.channel-info")
    public static class MyChannelInfo extends ChannelInfo {
    }

    @Bean
    ISOChannelProvider isoChannelProvider(
            ConfigurableApplicationContext applicationContext,
            ChannelInfo channelInfo) throws FinancialWrappedException {
        ISOChannelProvider isoChannelProvider = ISOChannelProvider.create(channelInfo);

        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();

        isoChannelProvider.getChannelSerializerEntry().forEach(entry -> {
            beanFactory.registerSingleton(entry.getKey(), entry.getValue());
        });

        return isoChannelProvider;
    }

    @Bean("shetab-7-iso-header-provider")
    ISOHeaderProvider shetab7HeaderProvider() {
        return new Shetab7V98ISOHeaderProvider();
    }

    @Bean("shetab-7-iso-header-processor")
    ISOHeaderProcessor shetab7HeaderProcessor() {
        return new Shetab7V98ISOHeaderProcessor();
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

    @ConfigurationProperties(prefix = "iso.server-info")
    public static class MyIsoServerInfo extends ISOServerInfo {
    }

    @Bean
    ISOServerSocketManager isoServerSocketManager(
            ISOServerInfo isoServerInfo, ApplicationContext applicationContext) throws ISOException {
        return ISOServerSocketManager.createAndStart(
                isoServerInfo.isDaemon(),
                isoServerInfo.getPort(),
                SocketConnectionType.byType(isoServerInfo.getSocketConnectionType()),
                isoServerInfo.getDurationInMillis(),
                applicationContext.getBean(isoServerInfo.getMatchKeyProviderBean(), RequestResponseMatchKeyProvider.class),
                applicationContext.getBean(isoServerInfo.getChannelName(), ISOChannelSerializer.class),
                ISOPackagerType.byName(isoServerInfo.getPackagerType()),
                ResponseHeaderProvideType.from(isoServerInfo.getResponseHeaderProvideType()),
                applicationContext.getBean(isoServerInfo.getHeaderProviderBean(), ISOHeaderProvider.class),
                applicationContext.getBean(isoServerInfo.getHeaderProcessorBean(), ISOHeaderProcessor.class),
                applicationContext.getBean(isoServerInfo.getMessageHandlerBean(), ISOMsgRequestHandler.class),
                isoServerInfo.getWhiteHostList(),
                isoServerInfo.getLogHexInOut(),
                applicationContext.getBean(MeterRegistry.class));
    }
}
