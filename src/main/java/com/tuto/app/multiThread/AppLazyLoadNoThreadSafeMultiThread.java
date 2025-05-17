package com.tuto.app.multiThread;

import com.tuto.service.lazyload.nothreadSafe.ExpensiveResourceLazyLoadNoThreadSafe;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static com.tuto.utils.MemoryUtils.*;


public class AppLazyLoadNoThreadSafeMultiThread {

    public static void main(String[] args) throws InterruptedException {
        System.out.println( "Run a snippet with lazy load implementation no thread safe !!!" );
        System.out.println("------------------------------------------------------");

        lazyLoadNoThreadSafeMultiThread();
    }

    /**
     * compute average on all memory snapshot
     * @return memory snapshot that is an average
     */
    public static MemorySnapshot lazyLoadNoThreadSafeMultiThreadMemorySnapshot() throws InterruptedException {
        int threadCount = 16;
        ConcurrentHashMap<String, MemorySnapshot> hashMap = new ConcurrentHashMap<>();
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        AtomicInteger nbUseOConnection = new AtomicInteger(0);

        for(int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                Random random = new Random();
                try {
                    startLatch.await();
                    MemorySnapshot before = takeMemorySnapshot();
                    for(int k = 0; k < 50; k++) {
                        ExpensiveResourceLazyLoadNoThreadSafe lazyLoad = new ExpensiveResourceLazyLoadNoThreadSafe();
                        lazyLoad.doSomethingElse();

                        if(random.nextDouble() <= 0.01) {
                            lazyLoad.getProducts();
                        }

                        if(nbUseOConnection.get() < lazyLoad.getNbUseOfConnection()) {
                            nbUseOConnection.getAndSet(lazyLoad.getNbUseOfConnection());
                        }
                    }
                    MemorySnapshot after = takeMemorySnapshot();
                    hashMap.put("AppLazyLoadNoThreadSafeMultiThread-"+Thread.currentThread().getName(),
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

    private static MemorySnapshot averageMemorySnapshot(ConcurrentHashMap<String, MemorySnapshot> hashMap) {
        double averageHeap = hashMap.values().stream()
                .mapToDouble(MemorySnapshot::heapUsed)
                .average()
                .orElse(0.0);

        double averageDuration = hashMap.values().stream()
                .mapToDouble(MemorySnapshot::timeMillisecond)
                .average()
                .orElse(0.0);

        return new MemorySnapshot(averageHeap, averageDuration);
    }

    /**
     * just print result to console
     * @throws InterruptedException
     */
    private static void lazyLoadNoThreadSafeMultiThread() throws InterruptedException {
        int threadCount = 16;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        AtomicInteger nbUseOConnection = new AtomicInteger(0);
        MemorySnapshot before = takeMemorySnapshot();

        for(int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                Random random = new Random();
                try {
                    startLatch.await();
                    for(int k = 0; k < 50; k++) {
                        ExpensiveResourceLazyLoadNoThreadSafe lazyLoad = new ExpensiveResourceLazyLoadNoThreadSafe();
                        lazyLoad.doSomethingElse();

                        if(random.nextDouble() <= 0.01) {
                            lazyLoad.getProducts();
                        }

                        if(nbUseOConnection.get() < lazyLoad.getNbUseOfConnection()) {
                            nbUseOConnection.getAndSet(lazyLoad.getNbUseOfConnection());
                        }
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

        MemorySnapshot after = takeMemorySnapshot();

        printMemoryDifference(before, after);
    }
}


