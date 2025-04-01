package com.vwap.calculator;

import org.junit.Test;

import com.vwap.calculator.Calculator;
import com.vwap.model.PriceData;
import org.junit.Test;
import java.time.Instant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CalculatorTest {

    @Test
    public void testSingleCurrencyPair() {
        Calculator calculator = new Calculator();
        // Current time
        Instant now = Instant.now();

        // Add one price update
        PriceData update1 = new PriceData(now, "EUR/USD", 1.1, 100);
        calculator.addPriceUpdate(update1);
        double vwap1 = calculator.getCurrentVWAP("EUR/USD").getPrice();
        assertEquals(1.1, vwap1, 0.0001);

        // Add another update for the same currency pair
        PriceData update2 = new PriceData(now.plusSeconds(60), "EUR/USD", 1.2, 200);
        calculator.addPriceUpdate(update2);
        double vwap2 = calculator.getCurrentVWAP("EUR/USD").getPrice();

        // VWAP = (1.1*100 + 1.2*200) / (100+200) = 1.167
        assertEquals(1.1667, vwap2, 0.0001);
    }

    @Test
    public void testMultipleCurrencyPairs() {
        Calculator calculator = new Calculator();
        // Current time
        Instant now = Instant.now();

        // Add updates for two different currency pairs
        calculator.addPriceUpdate(new PriceData(now, "EUR/USD", 1.1, 100));
        calculator.addPriceUpdate(new PriceData(now, "GBP/USD", 1.3, 200));

        // Check VWAPs for both pairs
        assertEquals(1.1, calculator.getCurrentVWAP("EUR/USD").getPrice(), 0.0001);
        assertEquals(1.3, calculator.getCurrentVWAP("GBP/USD").getPrice(), 0.0001);
    }

    @Test
    public void testOneHourWindow() {
        Calculator calculator = new Calculator();
        // Current time
        Instant now = Instant.now();
        System.err.println("now: " + now);

        // Add one update now
        PriceData update1 = new PriceData(now, "EUR/USD", 1.1000, (long) 100000);
        calculator.addPriceUpdate(update1);

        // Add another update from 59 minutes ago (should be included)
        Instant fiftyNineMinutesAgo = now.minusSeconds(59 * 60);
        PriceData update2 = new PriceData(fiftyNineMinutesAgo, "EUR/USD", 1.0900, 100000);
        calculator.addPriceUpdate(update2);

        // Add another update from 61 minutes ago (should be excluded)
        Instant sixtyOneMinutesAgo = now.minusSeconds(61 * 60);
        PriceData update3 = new PriceData(sixtyOneMinutesAgo, "EUR/USD", 1.0800, 100000);
        calculator.addPriceUpdate(update3);

        // Expected VWAP should only include the first two updates
        double expectedVWAP = ((1.1000 * 100000) + (1.0900 * 100000)) / (100000 + 100000);
        assertEquals(expectedVWAP, calculator.getCurrentVWAP("EUR/USD").getPrice(), 0.0001);

        // Total volume should be 200000 (not 300000)
        assertEquals(200000, calculator.getCurrentVWAP("EUR/USD").getVolume());
    }

    @Test
    public void testEmptyData() {
        Calculator calculator = new Calculator();

        // No data added, should return null
        assertTrue(calculator.getAllVWAPs().isEmpty());
    }
}