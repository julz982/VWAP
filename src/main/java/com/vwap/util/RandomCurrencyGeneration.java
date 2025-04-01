package com.vwap.util;

import com.vwap.model.PriceData;

import java.time.Instant;
import java.util.List;
import java.util.Random;

/**
 * Class to simulate a stream of data.
 */
public final class RandomCurrencyGeneration {

    /* Random generation for validation/testing purposes. */
    private static final Random RANDOM = new Random();

    /**
     * Used only for the purpose of this exercise to simulate a stream of price data.
     *
     * @return randomly generated {@link PriceData}
     */
    public static PriceData generateRandomPriceData() {

        List<String> CURRENCY_PAIRS = List.of("AUD/USD", "USD/JPY", "NZD/GBP",
                "AUD/GBP", "EUR/GBP", "EUR/USD", "AUD/JPY");

        String currencyPair = CURRENCY_PAIRS.get(RANDOM.nextInt(CURRENCY_PAIRS.size()));
        double price = getRandomPrice(currencyPair);
        int volume = RANDOM.nextInt(10_000_000) + 1;
        return new PriceData(Instant.now(), currencyPair, price, volume);
    }

    /**
     * Used only for the purpose of this exercise to simulate a stream of price data.
     *
     * @return randomly generated {@link PriceData}
     */
    private static double getRandomPrice(String currencyPair) {
        return switch (currencyPair) {
            case "AUD/USD" -> 0.63 + (RANDOM.nextDouble() * 0.1);
            case "USD/JPY" -> 150.06 + (RANDOM.nextDouble() * 5.0);
            case "NZD/GBP" -> 0.44 + (RANDOM.nextDouble() * 0.01);
            case "AUD/GBP" -> 0.49 + (RANDOM.nextDouble() * 0.01);
            case "EUR/GBP" -> 0.84 + (RANDOM.nextDouble() * 0.07);
            case "EUR/USD" -> 1.08 + (RANDOM.nextDouble() * 0.01);
            case "AUD/JPY" -> 94.27 + (RANDOM.nextDouble() * 3.00);
            default -> 1.0;
        };
    }
}
