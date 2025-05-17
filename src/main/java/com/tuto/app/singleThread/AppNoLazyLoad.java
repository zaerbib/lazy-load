package com.tuto.app.singleThread;

import com.tuto.service.nolazyload.ExpensiveResource;

import java.util.Random;

import static com.tuto.utils.MemoryUtils.*;

/**
 * Hello world!
 *
 */
@SuppressWarnings("all")
public class AppNoLazyLoad {
    public static void main( String[] args ) {
        System.out.println( "Run a snippet without lazy load implementation !!!" );
        System.out.println("------------------------------------------------------");

        noLazyLoadSingleThread();

    }

    private static void noLazyLoadSingleThread() {
        MemorySnapshot before = takeMemorySnapshot();
        Random random = new Random();

        for(int i = 0; i < 50; i++) {
            ExpensiveResource resource = new ExpensiveResource();
            resource.doSomethingElse();

            if(random.nextDouble() <= 0.01) {
                resource.getProducts();
            }
        }
        MemorySnapshot after = takeMemorySnapshot();
        printMemoryDifference(before, after);
    }

    public static MemorySnapshot noLazyLoadSingleThreadMemorySnapshot() {
        MemorySnapshot before = takeMemorySnapshot();
        Random random = new Random();

        for(int i = 0; i < 50; i++) {
            ExpensiveResource resource = new ExpensiveResource();
            resource.doSomethingElse();

            if(random.nextDouble() <= 0.01) {
                resource.getProducts();
            }
        }
        MemorySnapshot after = takeMemorySnapshot();
        return memorySnapshotDifference(before, after);
    }
}
