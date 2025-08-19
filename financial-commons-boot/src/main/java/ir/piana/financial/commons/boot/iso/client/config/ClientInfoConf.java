package ir.piana.financial.commons.boot.iso.client.config;

import ir.piana.financial.commons.iso.client.config.ISOClientInfo;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "iso.client-info-conf")
public class ClientInfoConf {
    private List<ISOClientInfo> isoClientInfos;

    public List<ISOClientInfo> getIsoClientInfos() {
        return isoClientInfos;
    }

    public void setIsoClientInfos(List<ISOClientInfo> isoClientInfos) {
        this.isoClientInfos = isoClientInfos;
    }
}
