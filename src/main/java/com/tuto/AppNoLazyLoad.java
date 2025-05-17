package com.tuto;

import com.tuto.service.nolazyload.ExpensiveResource;

import static com.tuto.utils.MemoryUtils.*;

/**
 * Hello world!
 *
 */
public class AppNoLazyLoad {
    public static void main( String[] args ) {
        System.out.println( "Run a snippet without lazy load implementation !!!" );
        System.out.println("------------------------------------------------------");

        noLazyLoadSingleThread();

    }

    private static void noLazyLoadSingleThread() {
        MemorySnapshot before = takeMemorySnapshot();
        for(int i = 0; i < 50; i++) {
            ExpensiveResource resource = new ExpensiveResource();
            resource.doSomethingElse();
        }
        MemorySnapshot after = takeMemorySnapshot();
        printMemoryDifference(before, after);
    }
}
