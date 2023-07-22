package eu.cafestube.cloudnet.service.status;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

public record ProcessSnapshot(
        long pid,
        double cpuUsage,
        double systemCpuUsage,
        long maxHeapMemory,
        long heapUsageMemory,
        long noHeapUsageMemory,
        long unloadedClassCount,
        long totalLoadedClassCount,
        int currentLoadedClassCount,
        @NonNull Collection<ThreadSnapshot> threads
) {

}