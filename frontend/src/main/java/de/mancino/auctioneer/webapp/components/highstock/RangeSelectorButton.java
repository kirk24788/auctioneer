/**
 * 
 */
package de.mancino.auctioneer.webapp.components.highstock;

import java.io.Serializable;

/**
 *
 * @author mmancino
 */
@SuppressWarnings("unused") // Attributes are accesses via reflection!
public class RangeSelectorButton extends JSObject implements Serializable {
    @JSIgnore
    private static final long serialVersionUID = Highstock.VERSION;

    private String type;
    private Integer count;
    private String text;
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected int identLevel() {
        return 6;
    }

    /**
     * Defines the timespan, can be one of 'millisecond', 'second', 'minute', 'day', 'week', 'month', 'ytd' (year to date), 
     * 'year' and 'all'.
     *
     * @param type type
     * @return self
     */
    public RangeSelectorButton type(String type) {
        this.type = type;
        return this;
    }

    /**
     * Defines how many units of the defined type to use.
     *
     * @param count count
     * @return self
     */
    public RangeSelectorButton count(Integer count) {
        this.count = count;
        return this;
    }

    /**
     * The text for the button itself.
     *
     * @param text text
     * @return self
     */
    public RangeSelectorButton text(String text) {
        this.text = text;
        return this;
    }

    
}
