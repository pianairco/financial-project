package ir.piana.financial.commons.boot.iso.channel.configs;

import ir.piana.financial.commons.iso.channel.configs.ChannelInfo;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "iso.channel-info-conf")
public class ChannelInfoConf {
    private List<ChannelInfo> channelInfos;

    public List<ChannelInfo> getChannelInfos() {
        return channelInfos;
    }

    public void setChannelInfos(List<ChannelInfo> channelInfos) {
        this.channelInfos = channelInfos;
    }
}
