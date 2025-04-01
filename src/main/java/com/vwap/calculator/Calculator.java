package com.vwap.calculator;

import com.vwap.model.PriceData;

import java.text.DecimalFormat;
import java.time.Instant;
import java.util.Collections;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;

/**
 * Class to calculate the VWAP (Volume Weighted Average Price).
 * <p>
 * Maintains a sliding window of price updates for each currency pair and calculates
 * the VWAP based on the last hour of data.
 */
public final class Calculator {

    /* Nanos per hour. */
    private static final long ONE_HOUR_IN_NANOS = 3_600_000_000_000L;

    /* The logger for the class. */
    private static final Logger LOGGER =
            Logger.getLogger(Calculator.class.getName());

    /* Thread safe mapping of currency pair to PriceData queue. */
    private final Map<String, Deque<PriceData>> currencyPairData =
            new ConcurrentHashMap<>();

    /* Thread safe mapping of currency pair to VWAPResult. */
    private final Map<String, PriceData> latestVWAP = new ConcurrentHashMap<>();

    /**
     * Adds a new price update to the system and triggers VWAP recalculation.
     * Removes data older than 1 hour.
     *
     * @param update The price update to add
     */
    public void addPriceUpdate(PriceData update) {
        final String updateCurrencyPair = update.getCurrencyPair();

        // Get or create the data queue for the currency pair
        final Deque<PriceData> priceData = currencyPairData.computeIfAbsent(
                updateCurrencyPair, k -> new ConcurrentLinkedDeque<>());

        // Add the new update
        priceData.addLast(update);

        // Remove updates older than 1 hour
        final Instant cutoffTime = Instant.now().minusNanos(ONE_HOUR_IN_NANOS);
        while (!priceData.isEmpty() && update.getTimestamp().isBefore(cutoffTime)) {
            priceData.removeFirst();
        }

        // Calculate VWAP
        final Deque<PriceData> currencyPairQueue = currencyPairData.get(update.getCurrencyPair());

        if (currencyPairQueue == null || currencyPairQueue.isEmpty()) {
            return;
        }

        double priceVolumeSum = 0.0;
        long volumeSum = 0;

        for (PriceData pairUpdate : currencyPairQueue) {
            priceVolumeSum += pairUpdate.getPrice() * pairUpdate.getVolume();
            volumeSum += pairUpdate.getVolume();
        }

        final double vwap = volumeSum > 0 ? priceVolumeSum / volumeSum : 0.0;

        // Store the result
        latestVWAP.put(updateCurrencyPair, new PriceData(Instant.now(), updateCurrencyPair, vwap, volumeSum));

        // Log the result
        LOGGER.info("VWAP for " + updateCurrencyPair + ": " + new DecimalFormat("#.####").format(vwap));
    }

    /**
     * Returns the latest VWAP result for a specific currency pair.
     *
     * @param currencyPair The currency pair to get VWAP for
     * @return The latest VWAP result, or null if no data exists for this currency pair
     */
    public PriceData getCurrentVWAP(String currencyPair) {
        return latestVWAP.get(currencyPair);
    }

    /**
     * Returns an unmodifiable map of all current VWAP results.
     *
     * @return Map of currency pairs to their latest VWAP results
     */
    public Map<String, PriceData> getAllVWAPs() {
        return Collections.unmodifiableMap(latestVWAP);
    }
}
