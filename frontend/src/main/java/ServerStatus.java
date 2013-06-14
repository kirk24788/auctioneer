import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import de.mancino.armory.Armory;
import de.mancino.armory.exceptions.RequestException;
import de.mancino.armory.json.api.realm.Realm;
import de.mancino.armory.json.vault.AuctionFaction;
import de.mancino.prowl.Priority;
import de.mancino.prowl.Prowl;


public class ServerStatus {
    private final static Prowl PROWL = new Prowl("d620d5f46e7b07f5486649dfdfc1ba6ad336a800");
    private final static String SERVERNAME = "Antonidas";
    private final static Armory ARMORY = new Armory("mario@mancino-net.de", "-", "Loox", AuctionFaction.ALLIANCE, SERVERNAME);
    private final static long SLEEP_INTERVAL = 1000;
    private final static long PRINT_INTERVAL = 15000;
    private final static long MAX_REQUEST_TIME = 1000;
    private final static ExecutorService EXECUTOR = Executors.newFixedThreadPool(1);
    private static int SUCCESSFUL_FETCHES = 0;
    private static int TIMEOUT_FETCHES = 0;
    private static int INTERVAL_SUCCESSFUL_FETCHES = 0;
    private static int INTERVAL_TIMEOUT_FETCHES = 0;

    /**
     * @param args
     * @throws RequestException
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        long lastPrint = startTime;
        while(!isOnline()) {
            long curTime = System.currentTimeMillis();
            if((curTime-lastPrint) > PRINT_INTERVAL) {
                lastPrint = curTime;
                System.out.println("Waited for " + (curTime-startTime)/1000/60 + " minutes, " + (curTime-startTime)/1000%60 + " seconds...");
                System.out.println(" * Successful: " + INTERVAL_SUCCESSFUL_FETCHES + " (" + SUCCESSFUL_FETCHES + " total)");
                System.out.println(" *   TimeOuts: " + INTERVAL_TIMEOUT_FETCHES + " (" + TIMEOUT_FETCHES + " total)");
                INTERVAL_SUCCESSFUL_FETCHES = 0;
                INTERVAL_TIMEOUT_FETCHES = 0;
            }
            sleep();
        }
        System.err.println("SERVER ONLINE");
        PROWL.sendMessage(Priority.EMERGENCY, "Server-Status", SERVERNAME + " is online!");
    }

    private static void sleep() {
        try {
            Thread.sleep(SLEEP_INTERVAL);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted!");
        }
    }

    private static boolean isOnline() {
        FutureTask<Boolean> future = new FutureTask<Boolean>(
                new Callable<Boolean>()
                {
                    @Override
                    public Boolean call()
                    {
                        try {
                            for(Realm realm : ARMORY.api.getRealmStatus().realms) {
                                if(realm.name.equals(SERVERNAME)) {
                                    return realm.status;
                                }
                            }
                            throw new RuntimeException("Realm '" + SERVERNAME + "' not found!");
                        } catch (RequestException e) {
                            return false;
                        }
                    }
                });
        EXECUTOR.execute(future);
        try {
            Boolean b = future.get(MAX_REQUEST_TIME, TimeUnit.MILLISECONDS);
            SUCCESSFUL_FETCHES++;
            INTERVAL_SUCCESSFUL_FETCHES++;
            return b;
        } catch (InterruptedException e) {
            System.err.println("InterruptedException: " + e.getLocalizedMessage());
            return false;
        } catch (ExecutionException e) {
            System.err.println("ExecutionException: " + e.getLocalizedMessage());
            return false;
        } catch (TimeoutException e) {
            System.err.println("TimeoutException while fetching realm status");
            TIMEOUT_FETCHES++;
            INTERVAL_TIMEOUT_FETCHES++;
            return false;
        }
    }
}
