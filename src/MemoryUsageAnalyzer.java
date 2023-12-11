package src;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class MemoryUsageAnalyzer {
    static MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

    public static void PrintNonHeapMemoryUsage() {
        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
        System.out.println("Non-Heap Memory Usage");
        printMemoryUsage(nonHeapMemoryUsage);
    }
    public static void PrintHeapMemoryUsage() {
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        System.out.println("Heap Memory Usage");
        printMemoryUsage(heapMemoryUsage);
    }
    private static void printMemoryUsage(MemoryUsage memoryUsage) {
        System.out.println("   Initial: " + memoryUsage.getInit() / (1024 * 1024) + " MB");
        System.out.println("   Used: " + memoryUsage.getUsed() / (1024 * 1024) + " MB");
        System.out.println("   Committed: " + memoryUsage.getCommitted() / (1024 * 1024) + " MB");
        System.out.println("   Max: " + memoryUsage.getMax() / (1024 * 1024) + " MB");
        System.out.println();
    }
}
