package de.mancino.auctioneer.webapp.components.highstock;

import java.io.Serializable;

/**
 * Language object. The language object is global and it can"t be set on each chart initiation. Instead, use 
 * Highcharts.setOptions to set it before any chart is initiated.
 * 
 * @author mmancino
 */
@SuppressWarnings("unused") // Attributes are accesses via reflection!
public class Global extends JSObject implements Serializable {
    @JSIgnore
    private static final long serialVersionUID = Highstock.VERSION;

    private Boolean useUTC;
    private String VMLRadialGradientURL;
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected int identLevel() {
        return 4;
    }

    /**
     * Whether to use UTC time for axis scaling, tickmark placement and time display in Highcharts.dateFormat. Advantages of 
     * using UTC is that the time displays equally regardless of the user agent's time zone settings. Local time can 
     * be used when the data is loaded in real time or when correct Daylight Saving Time transitions are required. 
     * Defaults to true.
     * 
     * @param useUTC useUTC
     * @return self
     */
    public Global useUTC(Boolean useUTC) {
        this.useUTC = useUTC;
        return this;
    }

    /**
     * Path to the pattern image required by VML browsers in order to draw radial gradients. Defaults to 
     * http://code.highcharts.com/highstock/{version}/gfx/vml-radial-gradient.png.
     * 
     * @param VMLRadialGradientURL VMLRadialGradientURL
     * @return self
     */
    public Global VMLRadialGradientURL(String VMLRadialGradientURL) {
        this.VMLRadialGradientURL = VMLRadialGradientURL;
        return this;
    }
}
