package com.tuto.app.multiThread;

import com.tuto.service.lazyload.threadSafe.ExpensiveResourceLazyLoadThreadSafe;
import com.tuto.utils.MemoryUtils;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static com.tuto.utils.MemoryUtils.*;

public class AppLazyLoadThreadSafeMultiThread {
    public static void main(String[] args) throws InterruptedException {
        System.out.println( "Run a snippet with lazy load implementation thread safe !!!" );
        System.out.println("------------------------------------------------------");

        lazyLoadThreadSafeMultiThread();
    }

    /**
     * compute average on all memory snapshot
     * @return memory snapshot that is an average
     */
    public static MemoryUtils.MemorySnapshot lazyLoadNoThreadSafeMultiThreadMemorySnapshot() throws InterruptedException {
        int threadCount = 16;
        ConcurrentHashMap<String, MemoryUtils.MemorySnapshot> hashMap = new ConcurrentHashMap<>();
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        AtomicInteger nbUseOConnection = new AtomicInteger(0);

        for(int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                Random random = new Random();
                try {
                    startLatch.await();
                    MemoryUtils.MemorySnapshot before = takeMemorySnapshot();
                    for(int k = 0; k < 50; k++) {
                        ExpensiveResourceLazyLoadThreadSafe lazyLoad = new ExpensiveResourceLazyLoadThreadSafe();
                        lazyLoad.doSomethingElse();

                        if(random.nextDouble() <= 0.01) {
                            lazyLoad.getProducts();
                        }

                        if(nbUseOConnection.get() < lazyLoad.getNbUseOfConnection()) {
                            nbUseOConnection.getAndSet(lazyLoad.getNbUseOfConnection());
                        }
                    }
                    MemoryUtils.MemorySnapshot after = takeMemorySnapshot();
                    hashMap.put("AppLazyLoadThreadSafeMultiThread-"+Thread.currentThread().getName(),
                            memorySnapshotDifference(before, after));
                } catch (InterruptedException e) {
                    System.out.println("Operation failed for " + Thread.currentThread().getName());
                } finally {
                    endLatch.countDown();
                }
            }).start();
        }
        System.out.println("Starting thread !!!");
        startLatch.countDown();
        endLatch.await();

        System.out.printf("Nb use of Connection is %d \n", nbUseOConnection.get());

        return averageMemorySnapshot(hashMap);
    }

    private static MemoryUtils.MemorySnapshot averageMemorySnapshot(ConcurrentHashMap<String, MemoryUtils.MemorySnapshot> hashMap) {
        double averageHeap = hashMap.values().stream()
                .mapToDouble(MemoryUtils.MemorySnapshot::heapUsed)
                .average()
                .orElse(0.0);

        double averageDuration = hashMap.values().stream()
                .mapToDouble(MemoryUtils.MemorySnapshot::timeMillisecond)
                .average()
                .orElse(0.0);

        return new MemoryUtils.MemorySnapshot(averageHeap, averageDuration);
    }

    private static void lazyLoadThreadSafeMultiThread() throws InterruptedException {
        int threadCount = 16;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        AtomicInteger nbUseOConnection = new AtomicInteger(0);

        MemoryUtils.MemorySnapshot before = takeMemorySnapshot();
        ExpensiveResourceLazyLoadThreadSafe lazyLoad = new ExpensiveResourceLazyLoadThreadSafe();

        for(int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    startLatch.await();
                    var products = lazyLoad.getProducts();
                    lazyLoad.doSomethingElse();

                    if(nbUseOConnection.get() < lazyLoad.getNbUseOfConnection()) {
                        nbUseOConnection.getAndSet(lazyLoad.getNbUseOfConnection());
                    }
                } catch (InterruptedException e) {
                    System.out.println("Operation failed for " + Thread.currentThread().getName());
                } finally {
                    endLatch.countDown();
                }
            }).start();
        }
        System.out.println("Starting thread !!!");
        startLatch.countDown();
        endLatch.await();

        System.out.printf("Nb use of Connection is %d \n", nbUseOConnection.get());

        MemoryUtils.MemorySnapshot after = takeMemorySnapshot();

        printMemoryDifference(before, after);
    }
}
