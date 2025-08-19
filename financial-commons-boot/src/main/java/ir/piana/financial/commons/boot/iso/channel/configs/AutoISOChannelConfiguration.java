package ir.piana.financial.commons.boot.iso.channel.configs;

import ir.piana.financial.commons.errors.FinancialWrappedException;
import ir.piana.financial.commons.iso.channel.ISOChannelProvider;
import ir.piana.financial.commons.iso.channel.ISOChannelSerializer;
import ir.piana.financial.commons.iso.client.ISOClientProvider;
import ir.piana.financial.commons.iso.server.console.ISOConsoleHandler;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;

import java.util.Map;

//@Configuration
@Configuration
//@ConditionalOnClass(ISOConsoleHandler.class)
//@AutoConfigureBefore({CommandLineRunner.class, ISOConsoleHandler.class})
//@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class AutoISOChannelConfiguration {
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "iso.channel-info-conf.enabled", havingValue = "true", matchIfMissing = false)
    public ISOChannelProvider isoChannelProvider(
            ConfigurableApplicationContext applicationContext, ChannelInfoConf channelInfoConf) throws FinancialWrappedException {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();

        ISOChannelProvider isoChannelProvider = ISOChannelProvider.create(channelInfoConf.getChannelInfos());
        for (Map.Entry<String, ISOChannelSerializer> entry : isoChannelProvider.getChannelSerializerEntry()) {
            beanFactory.registerSingleton(entry.getKey(), entry.getValue());
        }
        return isoChannelProvider;
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    ISOChannelSerializer isoChannelSerializer(ApplicationContext applicationContext, ISOChannelProvider isoChannelProvider) {
        return isoChannelProvider.getPrimary();
    }
}
