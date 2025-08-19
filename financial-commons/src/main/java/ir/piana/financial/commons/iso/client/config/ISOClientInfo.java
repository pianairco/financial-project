package ir.piana.financial.commons.iso.client.config;

public class ISOClientInfo {
    private String host;
    private int port;
    private String socketConnectionType;
    private long durationInMillis;
    private String matchKeyProviderBean;
    private String channelName;
    private String packagerType;
    private String headerProviderBean;
    private String headerProcessorBean;
    private boolean logHexOutIn;

    public ISOClientInfo() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getSocketConnectionType() {
        return socketConnectionType;
    }

    public void setSocketConnectionType(String socketConnectionType) {
        this.socketConnectionType = socketConnectionType;
    }

    public long getDurationInMillis() {
        return durationInMillis;
    }

    public void setDurationInMillis(long durationInMillis) {
        this.durationInMillis = durationInMillis;
    }

    public String getMatchKeyProviderBean() {
        return matchKeyProviderBean;
    }

    public void setMatchKeyProviderBean(String matchKeyProviderBean) {
        this.matchKeyProviderBean = matchKeyProviderBean;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getPackagerType() {
        return packagerType;
    }

    public void setPackagerType(String packagerType) {
        this.packagerType = packagerType;
    }

    public String getHeaderProviderBean() {
        return headerProviderBean;
    }

    public void setHeaderProviderBean(String headerProviderBean) {
        this.headerProviderBean = headerProviderBean;
    }

    public String getHeaderProcessorBean() {
        return headerProcessorBean;
    }

    public void setHeaderProcessorBean(String headerProcessorBean) {
        this.headerProcessorBean = headerProcessorBean;
    }

    public boolean getLogHexOutIn() {
        return logHexOutIn;
    }

    public void setLogHexOutIn(boolean logHexOutIn) {
        this.logHexOutIn = logHexOutIn;
    }
}
