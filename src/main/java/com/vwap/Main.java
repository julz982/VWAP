package com.vwap;

import com.vwap.calculator.Calculator;
import com.vwap.model.PriceData;
import com.vwap.util.RandomCurrencyGeneration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main application class for the VWAP Calculator.
 * Entry point that handles command line arguments and initiates processing.
 */
public final class Main {

    /* Sample prices to send in. */
    private static final int PRICES = 3_000;

    /* Number of threads to use. */
    private static final int NUMBER_OF_THREADS = 5;

    /* The calculator. */
    private static final Calculator calculator = new Calculator();

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        double startTime = System.currentTimeMillis();
        try (executor) {
                executor.submit(() -> {
                    for (int j = 0; j < PRICES; j++) {
                        PriceData randomPriceData = RandomCurrencyGeneration.generateRandomPriceData();
                        calculator.addPriceUpdate(randomPriceData);
                    }
                });
        } finally {
            executor.shutdown();
        }
        double endTime = System.currentTimeMillis();
        System.out.println(PRICES + " prices processed in " + (endTime - startTime) / 1000 + " seconds");
        System.exit(0);
    }
}