package ir.piana.financial.commons.structs;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class BlockingMap<T> {
    private final Map<String, BlockingQueue<T>> map;

    public BlockingMap(int capacity) {
         this.map = new LinkedHashMap<>(capacity);
    }

    public T take(String subject) throws InterruptedException {
        BlockingQueue<T> bq = map.computeIfAbsent(subject, k -> new LinkedBlockingQueue<>());
        return bq.take();
    }

    public T take(String subject, int howManyMilliseconds) throws InterruptedException {
        BlockingQueue<T> bq = map.computeIfAbsent(subject, k -> new LinkedBlockingQueue<>(1));
        return bq.poll(howManyMilliseconds, TimeUnit.MILLISECONDS);
    }

    public boolean offer(String subject, T object) {
        BlockingQueue<T> bq = map.computeIfAbsent(subject, k -> new LinkedBlockingQueue<>(1));
        return bq.offer(object);
    }
}
