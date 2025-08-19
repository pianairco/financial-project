package ir.piana.financial.commons.iso.server;

public class ServerProcessContext {
    private final String correlationId;
    private final byte[] inputBytes;
    private byte[] outputBytes;

    public ServerProcessContext(String correlationId, byte[] inputBytes) {
        this.correlationId = correlationId;
        this.inputBytes = inputBytes;
    }

    public byte[] getInputBytes() {
        return inputBytes;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public byte[] getOutputBytes() {
        return outputBytes;
    }

    public void setOutputBytes(byte[] outputBytes) {
        if (this.outputBytes == null) {
            this.outputBytes = outputBytes;
        }
    }
}
