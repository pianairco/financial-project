package ir.piana.financial.commons.iso.server.config;

import java.util.List;

public class ISOServerInfo {
    private boolean daemon;
    private int port;
    private String socketConnectionType;
    private long durationInMillis;
    private String matchKeyProviderBean;
    private String channelName;
    private String packagerType;
    private String messageHandlerBean;
    private String responseHeaderProvideType;
    private String headerProviderBean;
    private String headerProcessorBean;
    private List<String> whiteHostList;
    private boolean logHexInOut;

    public ISOServerInfo() {
    }

    public boolean isDaemon() {
        return daemon;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
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

    public String getMessageHandlerBean() {
        return messageHandlerBean;
    }

    public void setMessageHandlerBean(String messageHandlerBean) {
        this.messageHandlerBean = messageHandlerBean;
    }

    public String getResponseHeaderProvideType() {
        return responseHeaderProvideType;
    }

    public void setResponseHeaderProvideType(String responseHeaderProvideType) {
        this.responseHeaderProvideType = responseHeaderProvideType;
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

    public List<String> getWhiteHostList() {
        return whiteHostList;
    }

    public void setWhiteHostList(List<String> whiteHostList) {
        this.whiteHostList = whiteHostList;
    }

    public boolean getLogHexInOut() {
        return logHexInOut;
    }

    public void setLogHexInOut(boolean logHexInOut) {
        this.logHexInOut = logHexInOut;
    }

}
