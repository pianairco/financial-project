package ir.piana.financial.commons.structs;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DurationCheck {
    private Instant start;
    private final long durationInMillis;
    private final Runnable runnable;
    private ScheduledFuture<?> scheduled = null;

    public DurationCheck(long durationInMillis) {
        this.durationInMillis = durationInMillis;
        this.runnable = null;
    }

    public DurationCheck(Runnable runnable, long durationInMillis) {
        this.runnable = runnable;
        this.durationInMillis = durationInMillis;
        reset();
    }

    public void reset() {
        start = Instant.now();
        if (runnable != null) {
            boolean cancel = true;
            if (scheduled != null) {
                cancel = scheduled.cancel(false);
            }
            if (cancel) {
                scheduled = Executors.newSingleThreadScheduledExecutor()
                        .schedule(() -> Executors.newVirtualThreadPerTaskExecutor().execute(runnable),
                                durationInMillis, TimeUnit.MILLISECONDS);
            }
        }
    }

    public boolean isValid() {
        return durationInMillis < Duration.between(start, Instant.now()).toMillis();
    }
}
