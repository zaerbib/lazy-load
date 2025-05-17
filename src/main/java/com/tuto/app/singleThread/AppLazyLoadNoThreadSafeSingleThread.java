package com.tuto.app.singleThread;

import com.tuto.service.lazyload.nothreadSafe.ExpensiveResourceLazyLoadNoThreadSafe;
import com.tuto.utils.MemoryUtils;

import java.util.Random;

import static com.tuto.utils.MemoryUtils.*;

public class AppLazyLoadNoThreadSafeSingleThread {
    public static void main(String[] args) {
        System.out.println( "Run a snippet with lazy load implementation no thread safe !!!" );
        System.out.println("------------------------------------------------------");

        lazyLoadNoThreadSafeSingleThread();
    }

    private static void lazyLoadNoThreadSafeSingleThread() {
        MemoryUtils.MemorySnapshot before = takeMemorySnapshot();
        Random random = new Random();

        for(int i = 0; i < 50; i++) {
            ExpensiveResourceLazyLoadNoThreadSafe resource = new ExpensiveResourceLazyLoadNoThreadSafe();
            resource.doSomethingElse();

            if(random.nextDouble() <= 0.01) {
                resource.getProducts();
            }
        }
        MemoryUtils.MemorySnapshot after = takeMemorySnapshot();
        printMemoryDifference(before, after);
    }

    private static MemoryUtils.MemorySnapshot lazyLoadNoThreadSafeSingleThreadMemorySnapshot() {
        MemoryUtils.MemorySnapshot before = takeMemorySnapshot();
        Random random = new Random();

        for(int i = 0; i < 50; i++) {
            ExpensiveResourceLazyLoadNoThreadSafe resource = new ExpensiveResourceLazyLoadNoThreadSafe();
            resource.doSomethingElse();

            if(random.nextDouble() <= 0.01) {
                resource.getProducts();
            }
        }
        MemoryUtils.MemorySnapshot after = takeMemorySnapshot();
        return memorySnapshotDifference(before, after);
    }
}
