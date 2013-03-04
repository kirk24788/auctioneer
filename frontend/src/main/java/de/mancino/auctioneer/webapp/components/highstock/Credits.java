package de.mancino.auctioneer.webapp.components.highstock;

import java.io.Serializable;

/**
 * Highchart by default puts a credits label in the lower right corner of the chart. This can be changed using these options.
 * 
 * @author mmancino
 */
@SuppressWarnings("unused")
public class Credits extends JSObject implements Serializable {
    @JSIgnore
    private static final long serialVersionUID = Highstock.VERSION;

    private boolean enabled = true;
    private String href = "http://www.highcharts.com";
    // XXX: position -> Position configuration for the credtis label...not yet supported
    // XXX: style -> CSS styles for the credits label...not yet supported
    private String text = "Highcharts.com";
    

    /**
     * Whether to show the credits text. Defaults to true.
     * 
     * @param enabled enabled
     * @return self
     */
    public Credits enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * The URL for the credits label. Defaults to "http://www.highcharts.com".
     * 
     * @param href href
     * @return self
     */
    public Credits href(String href) {
        this.href = href;
        return this;
    }

    /**
     * The text for the credits label. Defaults to "Highcharts.com".
     * 
     * @param text text
     * @return self
     */
    public Credits text(String text) {
        this.text = text;
        return this;
    }

    @Override
    protected int identLevel() {
        return 4;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toString(enabled);
    }
}
