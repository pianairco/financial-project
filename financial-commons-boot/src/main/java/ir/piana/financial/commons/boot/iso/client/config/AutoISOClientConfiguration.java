package ir.piana.financial.commons.boot.iso.client.config;

import ir.piana.financial.commons.errors.FinancialWrappedException;
import ir.piana.financial.commons.iso.RequestResponseMatchKeyProvider;
import ir.piana.financial.commons.iso.channel.ISOChannelSerializer;
import ir.piana.financial.commons.iso.client.ISOClientProvider;
import ir.piana.financial.commons.iso.client.ISOSocketControllerAsClient;
import ir.piana.financial.commons.iso.client.config.ISOClientInfo;
import ir.piana.financial.commons.iso.header.ISOHeaderProcessor;
import ir.piana.financial.commons.iso.header.ISOHeaderProvider;
import ir.piana.financial.commons.iso.server.console.ISOConsoleHandler;
import ir.piana.financial.commons.types.ISOPackagerType;
import ir.piana.financial.commons.types.SocketConnectionType;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;

import java.io.IOException;
import java.util.LinkedHashMap;

@Configuration
//@ConditionalOnClass(ISOConsoleHandler.class)
//@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class AutoISOClientConfiguration {
    @Bean
//    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "iso.client-info-conf.enabled", havingValue = "true", matchIfMissing = false)
    public ISOClientProvider isoClientProvider(
            ConfigurableApplicationContext applicationContext, ClientInfoConf clientInfoConf) throws FinancialWrappedException, IOException {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();

        final LinkedHashMap<String, ISOSocketControllerAsClient> map = new LinkedHashMap<>();
        for (ISOClientInfo isoClientInfo : clientInfoConf.getIsoClientInfos()) {
            ISOSocketControllerAsClient isoSocketControllerAsClient = ISOSocketControllerAsClient.createAndStart(
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
            String isoClientName = "iso_client_".concat(isoClientInfo.getHost())
                    .concat("_")
                    .concat(String.valueOf(isoClientInfo.getPort()));
            beanFactory.registerSingleton(isoClientName, isoSocketControllerAsClient);
            map.put(isoClientName, isoSocketControllerAsClient);
        }
        return new ISOClientProvider(map);
    }

    @Bean
    @Primary
//    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "iso.client-info-conf.enabled", havingValue = "true")
    public ISOSocketControllerAsClient isoSocketControllerAsClient(
            ISOClientProvider isoClientProvider) throws FinancialWrappedException, IOException {
        return isoClientProvider.getPrimary();
    }
}
