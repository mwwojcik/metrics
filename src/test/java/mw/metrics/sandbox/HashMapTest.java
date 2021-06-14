package mw.metrics.sandbox;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Timed;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest(classes = {MetricsTestsConfiguration.class})
@Import(MetricsTestsConfiguration.class)
public class HashMapTest {

    @Autowired
    private MetricRegistry metricRegistry;
    public static final int MAX_ELEM = 1_000_000;
    private Map<Key, Integer> notChunkedMap = new HashMap<>();
    private Map<HashedKey, Integer> chunkedMap = new HashMap<>();

    private Long notChunkedLastInsertTime=Long.valueOf(0);
    private Long chunkedLastInsertTime=Long.valueOf(0);

    @BeforeEach
    private void init() {

        metricRegistry.register("NOT_CHUNKED_MAP_SIZE", (Gauge<Integer>) () -> notChunkedMap.size());
        metricRegistry.register("NOT_CHUNKED_GET_TIME", (Gauge<Long>) () -> notChunkedGetTiming());
        metricRegistry.register("NOT_CHUNKED_INSERT_TIME", (Gauge<Long>) () -> notChunkedInsertTiming());

        metricRegistry.register("CHUNKED_MAP_SIZE", (Gauge<Integer>) () -> chunkedMap.size());
        metricRegistry.register("CHUNKED_GET_TIME", (Gauge<Long>) () -> chunkedGetTiming());
        metricRegistry.register("CHUNKED_INSERT_TIME", (Gauge<Long>) () -> chunkedInsertTiming());

    }

    private Long chunkedInsertTiming() {
        System.out.println("Chunked Map PUT Time Saved!");
        return chunkedLastInsertTime;
    }

    private Long notChunkedInsertTiming() {
        System.out.println("Not Chunked Map PUT Time Saved!");
        return notChunkedLastInsertTime;
    }

    private long notChunkedGetTiming() {
        Random r = new Random();
        var index = r.nextInt(notChunkedMap.size());
        Instant startNotChunked = Instant.now();
        var resNotChunked = notChunkedMap.get(new Key(index));
        Instant stopNotChunked = Instant.now();
        System.out.println("Not Chunked Map Get Time Saved!");
        return Duration.between(startNotChunked, stopNotChunked).toNanos();
    }

    private long chunkedGetTiming() {
        Random r = new Random();
        var index = r.nextInt(chunkedMap.size());
        Instant startChunked = Instant.now();
        var resNotChunked = chunkedMap.get(new HashedKey(index));
        Instant stopChunked = Instant.now();
        System.out.println("Chunked Map Get Time Saved!");
        return Duration.between(startChunked, stopChunked).toNanos();

    }

    @DisplayName("should observe hashmap behaviour")
    @Test
    void shouldObserveHashmapBehaviour() {
        for (int i = 0; i < MAX_ELEM; i++) {
            Instant notChunkedStart = Instant.now();
            notChunkedMap.put(new Key(i), i);
            Instant notChunkedStop = Instant.now();
            notChunkedLastInsertTime=Duration.between(notChunkedStart, notChunkedStop).toNanos();


            Instant chunkedStart = Instant.now();
            chunkedMap.put(new HashedKey(i), i);
            Instant chunkedStop = Instant.now();
            chunkedLastInsertTime=Duration.between(chunkedStart, chunkedStop).toNanos();

            performSomeHeapOperations();
        }

    }

    private void performSomeHeapOperations() {
        BigDecimal init = BigDecimal.TEN;
        var multiply = init.multiply(BigDecimal.valueOf(2));
        var divide = multiply.divide(BigDecimal.valueOf(2));

    }

    class Key {

        Integer value;

        public Key(Integer i) {
            value = i;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Key key = (Key) o;
            return value == key.value;
        }

        @Override
        public int hashCode() {
            return 1;
        }
    }

    class HashedKey {

        Integer value;

        public HashedKey(Integer value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            HashedKey hashedKey = (HashedKey) o;
            return value.equals(hashedKey.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

}
