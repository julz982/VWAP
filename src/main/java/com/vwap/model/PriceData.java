package com.vwap.model;

import java.text.DecimalFormat;
import java.time.Instant;

import static com.vwap.util.FormattingUtils.*;

/**
 * Class representing a price update event.
 */
public final class PriceData {

    /* The timestamp when the price update occurred, in nanoseconds since epoch. */
    private final Instant timestamp;

    /* The currency pair identifier (e.g., "EUR/USD"). */
    private final String currencyPair;

    /* The price of the pairing. */
    private final double price;

    /* The amount purchased in the transaction. */
    private final long volume;

    /**
     * Constructs a new {@link PriceData}.
     *
     * @param timestamp the timestamp when the price update occurred, in nanoseconds since epoch
     * @param currencyPair the currency pair identifier (e.g., "EUR/USD")
     * @param price the price of the pairing
     * @param volume the amount purchased in the transaction
     */
    public PriceData(final Instant timestamp, final String currencyPair, final double price, final long volume) {
        this.timestamp = timestamp;
        this.currencyPair = currencyPair;
        this.price = price;
        this.volume = volume;
    }

    /**
     * Constructs a new {@link PriceData}.
     *
     * @param timestamp the timestamp when the price update occurred, in nanoseconds since epoch
     * @param currencyPair the currency pair identifier (e.g., "EUR/USD")
     * @param price the price of the pairing
     * @param volume the amount purchased in the transaction
     */
    public PriceData(final String timestamp, final String currencyPair, final double price, final String volume) {
        this.timestamp = timeAsInstant(timestamp);
        this.currencyPair = currencyPair;
        this.price = price;
        this.volume = volumeFormat(volume);
    }

    /**
     * @return the currency pair identifier (e.g., "EUR/USD")
     */
    public final String getCurrencyPair() {
        return currencyPair;
    }

    /**
     * @return the timestamp when the price update occurred, in nanoseconds
     * since epoch
     */
    public final Instant getTimestamp() {
        return timestamp;
    }

    /**
     * @return the price of the pairing
     */
    public final double getPrice() {
        return Double.parseDouble(new DecimalFormat("#.####").format(price));
    }

    /**
     * @return the amount purchased in the transaction
     */
    public final long getVolume() {
        return volume;
    }

    @Override
    public final String toString() {
        return "PriceData{" +
                "timestamp=" + timestamp +
                ", currencyPair='" + currencyPair + '\'' +
                ", price=" + price +
                ", volume=" + volume +
                '}';
    }
}
