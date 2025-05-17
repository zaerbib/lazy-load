package com.tuto.app.singleThread;

import com.tuto.service.lazyload.threadSafe.ExpensiveResourceLazyLoadThreadSafe;
import com.tuto.utils.MemoryUtils;

import java.util.Random;

import static com.tuto.utils.MemoryUtils.*;

public class AppLazyLoadThreadSafeSingleThread {

    public static void main(String[] args) {
        System.out.println( "Run a snippet with lazy load implementation thread safe !!!" );
        System.out.println("------------------------------------------------------");

        lazyLoadThreadSafeSingleThread();
    }

    private static void lazyLoadThreadSafeSingleThread() {
        MemoryUtils.MemorySnapshot before = takeMemorySnapshot();
        Random random = new Random();

        for(int i = 0; i < 50; i++) {
            ExpensiveResourceLazyLoadThreadSafe resource =
                    new ExpensiveResourceLazyLoadThreadSafe();
            resource.doSomethingElse();

            if(random.nextDouble() <= 0.01) {
                resource.getProducts();
            }
        }
        MemoryUtils.MemorySnapshot after = takeMemorySnapshot();
        printMemoryDifference(before, after);
    }

    public static  MemoryUtils.MemorySnapshot lazyLoadThreadSafeSingleThreadMemorySnapshot() {
        MemoryUtils.MemorySnapshot before = takeMemorySnapshot();
        Random random = new Random();

        for(int i = 0; i < 50; i++) {
            ExpensiveResourceLazyLoadThreadSafe resource =
                    new ExpensiveResourceLazyLoadThreadSafe();
            resource.doSomethingElse();

            if(random.nextDouble() <= 0.01) {
                resource.getProducts();
            }
        }
        MemoryUtils.MemorySnapshot after = takeMemorySnapshot();
        return memorySnapshotDifference(before, after);
    }
}