package de.mancino.auctioneer.dto.components;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class CurrencyTest {
    @Test
    public void testComponentsToPrice() {
        Currency p = new Currency(10,45,99);
        assertEquals(104599, p.toLong());
        assertEquals("10g45s99c", p.toString());
        assertEquals(99, p.getCopper());
        assertEquals(45, p.getSilver());
        assertEquals(10, p.getGold());
    }

    @Test
    public void testLongToPrice() {
        Currency p = new Currency(104599);
        assertEquals(104599, p.toLong());
        assertEquals("10g45s99c", p.toString());
        assertEquals(99, p.getCopper());
        assertEquals(45, p.getSilver());
        assertEquals(10, p.getGold());
    }

    @Test
    public void testIntegerOverflowToLong() {
        // A Int wären maximal 214.748 Gold ( -> (2^31)/100/100) darstellbar!
        Currency p1 = new Currency(2147474949);
        assertEquals(2147474949, p1.toLong());
        assertEquals("214747g49s49c", p1.toString());
        Currency p2 = new Currency(2147474949);
        assertEquals(2147474949, p2.toLong());
        assertEquals("214747g49s49c", p2.toString());
        long sum = p1.toLong() + p2.toLong();
        assertEquals(4294949898L, sum);
        Currency p3 = new Currency(sum);
       // assertEquals(4294949898L, p3.toLong());
       // assertEquals("429494g98s98c", p1.toString());
        assertEquals(429494, p3.getGold());
        assertEquals(98, p3.getSilver());
        assertEquals(98, p3.getCopper());
    }

    @Test
    public void testNegativeToString() {
        Currency p1 = new Currency(-1025);
        assertEquals(-1025, p1.toLong());
        assertEquals("-0g10s25c", p1.toString());
        Currency p2 = new Currency(-2147474949);
        assertEquals(-2147474949, p2.toLong());
        assertEquals("-214747g49s49c", p2.toString());
    }
}
