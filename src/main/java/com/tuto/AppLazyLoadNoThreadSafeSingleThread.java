package com.tuto;

import com.tuto.service.lazyload.nothreadSafe.ExpensiveResourceLazyLoad;
import com.tuto.utils.MemoryUtils;

import static com.tuto.utils.MemoryUtils.printMemoryDifference;
import static com.tuto.utils.MemoryUtils.takeMemorySnapshot;

public class AppLazyLoadNoThreadSafeSingleThread {
    public static void main(String[] args) {
        System.out.println( "Run a snippet with lazy load implementation no thread safe !!!" );
        System.out.println("------------------------------------------------------");

        lazyLoadNoThreadSafeSingleThread();
    }

    private static void lazyLoadNoThreadSafeSingleThread() {
        MemoryUtils.MemorySnapshot before = takeMemorySnapshot();
        for(int i = 0; i < 50; i++) {
            ExpensiveResourceLazyLoad resource = new ExpensiveResourceLazyLoad();
            resource.doSomethingElse();
        }
        MemoryUtils.MemorySnapshot after = takeMemorySnapshot();
        printMemoryDifference(before, after);
    }
}
