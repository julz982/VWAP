package com.vwap;

import com.vwap.calculator.Calculator;
import com.vwap.model.PriceData;
import com.vwap.util.RandomCurrencyGeneration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Main application class for the VWAP Calculator.
 * Entry point that handles command line arguments and initiates processing.
 */
public final class Main {

    /* The logger for the class. */
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    /* Sample prices per second to send in. */
    private static final int PRICES_PER_SECOND = 5_000;

    /* Sample run time. */
    private static final int DURATION_SECONDS = 60;

    /* The calculator. */
    private static final Calculator calculator = new Calculator();

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        try(executor) {
            for (int i = 0; i < DURATION_SECONDS; i++) {
                long startTime = System.currentTimeMillis();
                executor.submit(() -> {
                    for (int j = 0; j < PRICES_PER_SECOND; j++) {
                        PriceData randomPriceData = RandomCurrencyGeneration.generateRandomPriceData();
                        calculator.addPriceUpdate(randomPriceData);
                    }
                });
                long endTime = System.currentTimeMillis();
                try {
                    // To ensure 5_000 updates per second for example case
                    Thread.sleep(1000 - (endTime - startTime));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    LOGGER.severe(e.toString());
                }
            }
        }
        finally {
            executor.shutdown();
        }
        System.exit(0);
    }
}