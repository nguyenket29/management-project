package com.hau.ketnguyen.it.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MemoryUtils {

    private static final Logger log = LoggerFactory.getLogger(MemoryUtils.class);

    public static Map<String, Object> getHeapInfo() {
        List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        Runtime runtime = Runtime.getRuntime();
        long heapSize = runtime.totalMemory();

        // Get maximum size of heap in bytes. The heap cannot grow beyond this size.// Any attempt will result in an OutOfMemoryException.
        long heapMaxSize = runtime.maxMemory();

        // Get amount of free memory within the heap in bytes. This size will increase // after garbage collection and decrease as new objects are created.
        long heapFreeSize = runtime.freeMemory();

        long heapUsedSize = runtime.totalMemory() - runtime.freeMemory();

        String heap_size = formatSize(heapSize);
        String heap_max_size = formatSize(heapMaxSize);
        String heap_free_size = formatSize(heapFreeSize);
        String heap_used_size = formatSize(heapUsedSize);

        Map<String, Object> maps = new LinkedHashMap<>();
        maps.put("runtime-args", inputArguments);
        maps.put("heap-max-size", heap_max_size);
        maps.put("heap-total-size", heap_size);
        maps.put("heap-used_size", heap_used_size);
        maps.put("heap-free-size", heap_free_size);
        return maps;
    }

    public static void printHeapInfo() {
        Map<String, Object> maps = getHeapInfo();
        for (Map.Entry<String, Object> entry : maps.entrySet()) {
            log.info("{}: {}", entry.getKey(), entry.getValue());
        }
    }

    private static String formatSize(long v) {
        if (v < 1024) return v + " B";
        int z = (63 - Long.numberOfLeadingZeros(v)) / 10;
        return String.format("%.1f %sB", (double)v / (1L << (z*10)), " KMGTPE".charAt(z));
    }
}
