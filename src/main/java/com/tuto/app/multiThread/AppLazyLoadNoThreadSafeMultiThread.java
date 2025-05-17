package com.tuto.app.multiThread;

import com.tuto.service.lazyload.nothreadSafe.ExpensiveResourceLazyLoad;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static com.tuto.utils.MemoryUtils.*;


public class AppLazyLoadNoThreadSafeMultiThread {
    public static void main(String[] args) throws InterruptedException {
        System.out.println( "Run a snippet with lazy load implementation no thread safe !!!" );
        System.out.println("------------------------------------------------------");

        lazyLoadNoThreadSafeMultiThread();
    }

    private static void lazyLoadNoThreadSafeMultiThread() throws InterruptedException {
        int threadCount = 16;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        AtomicInteger nbUseOConnection = new AtomicInteger(0);
        MemorySnapshot before = takeMemorySnapshot();
        ExpensiveResourceLazyLoad lazyLoad = new ExpensiveResourceLazyLoad();

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

        MemorySnapshot after = takeMemorySnapshot();

        printMemoryDifference(before, after);
    }
}


