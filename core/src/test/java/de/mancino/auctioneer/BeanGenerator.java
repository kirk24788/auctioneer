package de.mancino.auctioneer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.PriceSample;
import de.mancino.auctioneer.dto.PriceWatch;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.dto.components.PriceWatchId;

public class BeanGenerator {
    public static final int PRICE_SAMPLE_COUNT = 5;//4 * 24 * 31; // 4 samples every day for 31 days
    public static final long PRICE_SAMPLE_TIMEDIFF = 15 * 60 * 1000; // 15 minute time diff
    private static final Random random = new Random();
    private static final AtomicInteger itemId = new AtomicInteger(10);


    public static PriceWatch generatePriceWatch(final ArmoryItem armoryItem) {
        final PriceWatch priceWatch = new PriceWatch(armoryItem);
        return priceWatch;
    }


    public static PriceSample generatePriceSample(final PriceWatchId priceWatchId) {
        return generatePriceSample(priceWatchId, System.currentTimeMillis());
    }

    public static PriceSample generatePriceSample(final PriceWatchId priceWatchId, final long timestamp) {
        final PriceSample sample = new PriceSample(priceWatchId, timestamp);
        sample.setMinimumPrice(new Currency((random.nextInt(10)+1),(random.nextInt(100)),(random.nextInt(100))));
        sample.setAveragePrice(new Currency((random.nextInt(3)+1)*sample.getMinimumPrice().toLong()));
        sample.setMedianPrice(new Currency((long) (sample.getAveragePrice().toLong() * 0.9)));
        sample.setSampleSize(5 + random.nextInt(100));
        return sample;
    }

    public static List<PriceSample> generateAllPriceSamples(final PriceWatchId priceWatchId) {
        List<PriceSample> samples = new ArrayList<PriceSample>();
        long end=System.currentTimeMillis();
        for(long timestamp=(end-(PRICE_SAMPLE_COUNT*PRICE_SAMPLE_TIMEDIFF)) ; timestamp < end ; timestamp+=PRICE_SAMPLE_TIMEDIFF) {
            samples.add(generatePriceSample(priceWatchId, timestamp));
        }
        return samples;
    }
}
