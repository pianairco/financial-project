package ir.piana.financial.commons.iso.client;

import java.util.LinkedHashMap;
import java.util.Map;

public class ISOClientProvider {
    private Map<String, ISOSocketControllerAsClient> map;

    public ISOClientProvider(final LinkedHashMap<String, ISOSocketControllerAsClient> map) {
        this.map = map;
    }

    public ISOSocketControllerAsClient getPrimary() {
        return map.entrySet().iterator().next().getValue();
    }

    public ISOSocketControllerAsClient getIsoClient(String host, int port) {
        return map.get(host + "_" + port);
    }
}
