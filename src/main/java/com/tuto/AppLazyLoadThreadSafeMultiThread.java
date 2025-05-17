package com.tuto;

import com.tuto.service.lazyload.nothreadSafe.ExpensiveResourceLazyLoad;
import com.tuto.service.lazyload.threadSafe.ExpensiveResourceLazyLoadThreadSafe;
import com.tuto.utils.MemoryUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static com.tuto.utils.MemoryUtils.printMemoryDifference;
import static com.tuto.utils.MemoryUtils.takeMemorySnapshot;

public class AppLazyLoadThreadSafeMultiThread {
    public static void main(String[] args) throws InterruptedException {
        System.out.println( "Run a snippet with lazy load implementation no thread safe !!!" );
        System.out.println("------------------------------------------------------");

        lazyLoadThreadSafeMultiThread();
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
