package main.java.daa;

import main.java.daa.bench.Benchmark;

import java.io.IOException;

/**
 * Точка входа в программу.
 * Запускает полный бенчмарк и сохраняет CSV отчет в results/results.csv.
 */
public class Main {
    public static void main(String[] args) {
        try {
            Benchmark.main(args);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении результатов бенчмарка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
