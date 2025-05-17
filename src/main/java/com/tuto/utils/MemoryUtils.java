package com.tuto.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

public class MemoryUtils {
    public record MemorySnapshot (long heapUsed,
                                  long nonHeapUsed,
                                  long timeMillisecond) {}

    public static MemorySnapshot takeMemorySnapshot() {
        System.gc();
        long timeSnapShot = System.currentTimeMillis();

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        long heapUsed = memoryMXBean.getHeapMemoryUsage().getUsed();
        long nonHeapUsed = memoryMXBean.getNonHeapMemoryUsage().getUsed();

        return new MemorySnapshot(heapUsed, nonHeapUsed, timeSnapShot);
    }

    public static void printMemoryDifference(MemorySnapshot before, MemorySnapshot after) {
        System.out.println("-------------------------------------");
        System.out.println("Memory change report:");
        System.out.printf("Heap: %+f mb\n", (after.heapUsed - before.heapUsed) * Double.parseDouble("1e-6"));
        System.out.printf("Non-Heap: %+f mb\n", (after.nonHeapUsed - before.nonHeapUsed) * Double.parseDouble("1e-6"));
        System.out.printf("Time duration in second : %+f second\n", (after.timeMillisecond - before.timeMillisecond) * Double.parseDouble("1e-3"));
    }
}
