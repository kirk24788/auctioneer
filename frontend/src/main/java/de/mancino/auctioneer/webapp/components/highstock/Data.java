/**
 *
 */
package de.mancino.auctioneer.webapp.components.highstock;

import java.io.Serializable;

/**
 *
 * @author mmancino
 */
public class Data extends JSObject implements Serializable {
    private static final long serialVersionUID = Highstock.VERSION;
    public final long x;
    public final double y;

    public Data(final long x, final double y) {
        this.x = x;
        this.y = y;
    }

    public Data data(final long x, final long y) {
        return new Data(x,y);
    }

    public Data data(final long x, final double y) {
        return new Data(x,y);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected int identLevel() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }
}
