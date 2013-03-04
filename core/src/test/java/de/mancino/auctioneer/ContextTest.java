package de.mancino.auctioneer;

import static de.mancino.auctioneer.dto.components.Currency.currency;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import de.mancino.auctioneer.dto.components.Currency;

public abstract class ContextTest {
    private final static boolean USE_HSQL_FOR_TESTS = true;
    protected static final Random RANDOM = new Random();
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1L);
    protected static AbstractApplicationContext context;

    @BeforeClass
    public static void initContext() {
        if(USE_HSQL_FOR_TESTS) {
            System.setProperty("jdbc.driver", "org.hsqldb.jdbcDriver");
            System.setProperty("jdbc.url", "jdbc:hsqldb:target/auctioneerTestDB_" + System.currentTimeMillis());
            System.setProperty("jdbc.user", "sa");
            System.setProperty("jdbc.password", "");
            System.setProperty("jdbc.ddl.dialect", "HSQL2");
            System.setProperty("jdbc.validation.query", "");
        }
        System.setProperty("tasks.delay.pricewatch","600000000");
        System.setProperty("tasks.delay.removeolddata","600000000");

        context = new FileSystemXmlApplicationContext("classpath:springconfig/auctioneer-db.xml");
    }

    @AfterClass
    public static void cleanupContext() {
        context.destroy();
    }

    protected static long nextId() {
        return ID_GENERATOR.getAndIncrement();
    }

    protected static byte[] nextByteArray() {
        final int arraySize = (RANDOM.nextInt(32)+1) * 8; // 8-256 Bytes
        byte array[] = new byte[arraySize];
        RANDOM.nextBytes(array);
        return array;
    }

    public static Currency createPrice() {
        return currency(RANDOM.nextInt(10), RANDOM.nextInt(99), RANDOM.nextInt(99));
    }
}
