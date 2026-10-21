package main.java.daa.bench;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Запись результатов бенчмарка в формат CSV.
 */
public class CsvWriter {

    public static void writeResults(File file, List<BenchmarkRow> rows) throws IOException {
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("algorithm,graph,n,m,time_ms,dsu_ops,edge_scans,comparisons,max_depth,result");
            for (BenchmarkRow row : rows) {
                writer.printf("%s,%s,%d,%d,%.3f,%d,%d,%d,%d,%d%n",
                        row.algorithm,
                        row.graphKind,
                        row.n,
                        row.m,
                        row.timeMs,
                        row.dsuOps,
                        row.edgeScans,
                        row.comparisons,
                        row.maxDepth,
                        row.result);
            }
        }
    }
}
