package com.stylight.seo;

import com.stylight.seo.domain.UrlService;
import com.stylight.seo.repository.InMemoryRepository;
import com.stylight.seo.util.AssertionUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@SpringBootTest
public class ConcurrencyTest {
    public static final int SIZE = 50;
    private final Logger log = LoggerFactory.getLogger(ConcurrencyTest.class);

    @Autowired
    private UrlService urlService;

    @Autowired
    private InMemoryRepository inMemoryRepository;

    @Test
    public void concurrencyTest() throws InterruptedException {
        int iterationsCount = 100_000;
        ExecutorService service = Executors.newFixedThreadPool(50);
        CountDownLatch latch = new CountDownLatch(iterationsCount);

        AtomicLong maxTime = new AtomicLong(0L);
        AtomicLong avgTime = new AtomicLong(0L);
        AtomicLong minTime = new AtomicLong(Long.MAX_VALUE);
        List<Map<String, String>> maps = buildData();

        for (int i = 0; i < iterationsCount; i++) {
            service.submit(() -> {
                long start = System.currentTimeMillis();
                Map<String, String> prettyUrls = urlService.getPrettyUrls(maps.get(ThreadLocalRandom.current().nextInt(maps.size())).keySet());
                long time = System.currentTimeMillis() - start;
                AssertionUtils.assertEach(inMemoryRepository.findAll(), prettyUrls);
                maxTime.getAndUpdate(m -> Math.max(m, time));
                avgTime.getAndAdd(time);
                minTime.getAndUpdate(m -> Math.min(m, time));

                latch.countDown();
            });
        }

        latch.await();
        log.info("Execution time: {} - {}, avg: {}", minTime, maxTime, avgTime.get() / iterationsCount);
    }

    private List<Map<String, String>> buildData() {
        List<Map<String, String>> list = new ArrayList<>(SIZE);
        Map<String, String> urlsMap = inMemoryRepository.findAll();
        int requestSize = 100;
        for (int i = 0; i < SIZE; i++) {
            int shift = ThreadLocalRandom.current().nextInt(urlsMap.size() - requestSize);
            Map<String, String> collect = urlsMap
                    .entrySet()
                    .stream()
                    .skip(shift)
                    .limit(requestSize)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            list.add(collect);
        }
        return list;
    }

}
