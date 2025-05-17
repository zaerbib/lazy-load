package com.tuto.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

public class MemoryUtils {
    public record MemorySnapshot (double heapUsed,
                                  double timeMillisecond) {}

    public static MemorySnapshot takeMemorySnapshot() {
        System.gc();
        long timeSnapShot = System.currentTimeMillis();

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        long heapUsed = memoryMXBean.getHeapMemoryUsage().getUsed();

        return new MemorySnapshot(heapUsed, timeSnapShot);
    }

    public static void printMemoryDifference(MemorySnapshot before, MemorySnapshot after) {
        System.out.println("-------------------------------------");
        System.out.println("Memory change report:");
        System.out.printf("Heap: %+f mb\n", (after.heapUsed - before.heapUsed) * Double.parseDouble("1e-6"));
        System.out.printf("Time duration in second : %+f second\n", (after.timeMillisecond - before.timeMillisecond) * Double.parseDouble("1e-3"));
    }

    public static MemorySnapshot memorySnapshotDifference(MemorySnapshot before, MemorySnapshot after) {
        return new MemorySnapshot(after.heapUsed - before.heapUsed,
                after.timeMillisecond - before.timeMillisecond);
    }
}
