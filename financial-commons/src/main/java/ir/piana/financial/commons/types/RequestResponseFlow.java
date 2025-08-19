package ir.piana.financial.commons.types;

public enum RequestResponseFlow {
    DEFAULT("default"),
    SYNC("sync"),
    ASYNC("async"),
    ;

    private final String type;

    RequestResponseFlow(String type) {
        this.type = type;
    }

    public static RequestResponseFlow byType(String type) {
        for (RequestResponseFlow flow : RequestResponseFlow.values()) {
            if (flow.type.equals(type)) {
                return flow;
            }
        }
        return DEFAULT;
    }
}
