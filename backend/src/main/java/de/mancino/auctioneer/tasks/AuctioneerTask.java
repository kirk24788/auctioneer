package de.mancino.auctioneer.tasks;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mancino.auctioneer.bo.ErrorLogBO;

public abstract class AuctioneerTask extends TimerTask {
    /**
     * Logger instance of this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AuctioneerTask.class);

    protected static final long MILLIS_PER_HOUR = 60L * 60L * 1000L;
    protected static final long MILLIS_PER_DAY = MILLIS_PER_HOUR * 24L;
    protected static final long MILLIS_PER_MONTH = MILLIS_PER_DAY * 31L;
    protected final ErrorLogBO errorLogBO;

    public AuctioneerTask(final ErrorLogBO errorLogBO) {
        this.errorLogBO = errorLogBO;
    }

    @Override
    public final void run() {
        final long timeStart = System.currentTimeMillis();
        LOG.info("Starting Task '{}'...", getTaskTitle());
        try {
            runInternal();
            final long duration = System.currentTimeMillis() - timeStart;
            LOG.info("...Finished Task '{}' - duration: {}", getTaskTitle(), formatDuration(duration));
        } catch (Throwable t) {
            final long duration = System.currentTimeMillis() - timeStart;
            errorLogBO.addException(t);
            LOG.error("...FAILED TASK '" + getTaskTitle() + "' - duration: " + formatDuration(duration),t);
        }
    }

    protected String formatDuration(final long duration) {
        final long ms = duration%1000L;
        final long sec = (duration/1000L)%60;
        final long min = (duration/60000L)%60;
        final long hour = duration/3600000L;
        if(hour>0 || min>0) {
            return fillZeros(hour,2) + ":" + fillZeros(min,2);
        } else if (sec>0) {
            return sec + "," + fillZeros(ms, 3) + " sec";
        } else {
            return ms + " ms";
        }
    }

    protected String fillZeros(final long number, final int width) {
        if(width<=0) {
            throw new IllegalArgumentException("Width must be > 0!");
        }
        return String.format("%0" + width + "d", number);
    }

    public abstract String getTaskTitle();

    protected abstract void runInternal();
}
