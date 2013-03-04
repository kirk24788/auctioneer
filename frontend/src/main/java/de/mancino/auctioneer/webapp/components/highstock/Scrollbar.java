/**
 * 
 */
package de.mancino.auctioneer.webapp.components.highstock;

import java.io.Serializable;

import de.mancino.auctioneer.dto.components.Color;

/**
 *
 * @author mmancino
 */
/**
 *
 * @author mmancino
 */
@SuppressWarnings("unused")
public class Scrollbar extends JSObject implements Serializable {
    @JSIgnore
    private static final long serialVersionUID = Highstock.VERSION;

    private Color barBackgroundColor;
    private Color barBorderColor = new Color("666666");
    private Integer barBorderRadius = 2;
    private Integer barBorderWidth = 1;
    private Color buttonArrowColor = new Color("666666");
    private Color buttonBackgroundColor;
    private Color buttonBorderColor = new Color("666666");
    private Integer buttonBorderRadius = 2;
    private Integer buttonBorderWidth = 1;
    private boolean enabled;
    private Integer height;
    private Integer minWidth = 6;
    private Color rifleColor = new Color("666666");
    private Color trackBackgroundColor;
    private Color trackBorderColor;
    private Integer trackBorderRadius = 0;
    private Integer trackBorderWidth = 1;

    
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected int identLevel() {
        return 4;
    }



    /**
     * The background color of the scrollbar itself. Defaults to a gray gradient.
     * 
     * @param barBackgroundColor barBackgroundColor
     * @return self
     */
    public Scrollbar barBackgroundColor(Color barBackgroundColor) {
        this.barBackgroundColor = barBackgroundColor;
        return this;
    }



    /**
     * The color of the bar's border. Defaults to #666666.
     * 
     * @param barBorderColor barBorderColor
     * @return self
     */
    public Scrollbar barBorderColor(Color barBorderColor) {
        this.barBorderColor = barBorderColor;
        return this;
    }



    /**
     * The border rounding radius of the bar. Defaults to 2.
     * 
     * @param barBorderRadius barBorderRadius
     * @return self
     */
    public Scrollbar barBorderRadius(Integer barBorderRadius) {
        this.barBorderRadius = barBorderRadius;
        return this;
    }



    /**
     * The width of the bar's border. Defaults to 1.
     * 
     * @param barBorderWidth barBorderWidth
     * @return self
     */
    public Scrollbar barBorderWidth(Integer barBorderWidth) {
        this.barBorderWidth = barBorderWidth;
        return this;
    }



    /**
     * The color of the small arrow inside the scrollbar buttons. Defaults to #666.
     * 
     * @param buttonArrowColor buttonArrowColor
     * @return self
     */
    public Scrollbar buttonArrowColor(Color buttonArrowColor) {
        this.buttonArrowColor = buttonArrowColor;
        return this;
    }
    
    /**
     * The color of scrollbar buttons. Defaults to a gray gradient.
     * 
     * @param buttonBackgroundColor buttonBackgroundColor
     * @return self
     */
    public Scrollbar buttonBackgroundColor(Color buttonBackgroundColor) {
        this.buttonBackgroundColor = buttonBackgroundColor;
        return this;
    }



    /**
     * The color of the border of the scrollbar buttons. Defaults to #666.
     * 
     * @param buttonBorderColor buttonBorderColor
     * @return self
     */
    public Scrollbar buttonBorderColor(Color buttonBorderColor) {
        this.buttonBorderColor = buttonBorderColor;
        return this;
    }



    /**
     * The corner radius of the scrollbar buttons. Defaults to 2.
     * 
     * @param buttonBorderRadius buttonBorderRadius
     * @return self
     */
    public Scrollbar buttonBorderRadius(Integer buttonBorderRadius) {
        this.buttonBorderRadius = buttonBorderRadius;
        return this;
    }



    /**
     * The border width of the scrollbar buttons. Defaults to 1.
     * 
     * @param buttonBorderWidth buttonBorderWidth
     * @return self
     */
    public Scrollbar buttonBorderWidth(Integer buttonBorderWidth) {
        this.buttonBorderWidth = buttonBorderWidth;
        return this;
    }



    /**
     * Enable or disable the scrollbar. Defaults to true.
     * 
     * @param enabled enabled
     * @return self
     */
    public Scrollbar enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }



    /**
     * The height of the scrollbar. The height also applies to the width of the scroll arrows so that they are always squares. 
     * Defaults to 20 for touch devices and 14 for mouse devices.
     * 
     * @param height height
     * @return self
     */
    public Scrollbar height(Integer height) {
        this.height = height;
        return this;
    }



    /**
     * The minimum width of the scrollbar. Defaults to 6.
     * 
     * @param minWidth minWidth
     * @return self
     */
    public Scrollbar minWidth(Integer minWidth) {
        this.minWidth = minWidth;
        return this;
    }



    /**
     * The color of the small rifles in the middle of the scrollbar. Defaults to #666666.
     * 
     * @param rifleColor rifleColor
     * @return self
     */
    public Scrollbar rifleColor(Color rifleColor) {
        this.rifleColor = rifleColor;
        return this;
    }



    /**
     * The color of the track background. The default is a gray gradient.
     * 
     * @param trackBackgroundColor trackBackgroundColor
     * @return self
     */
    public Scrollbar trackBackgroundColor(Color trackBackgroundColor) {
        this.trackBackgroundColor = trackBackgroundColor;
        return this;
    }



    /**
     * The color of the border of the scrollbar track.
     * 
     * @param trackBorderColor trackBorderColor
     * @return self
     */
    public Scrollbar trackBorderColor(Color trackBorderColor) {
        this.trackBorderColor = trackBorderColor;
        return this;
    }



    /**
     * The corner radius of the border of the scrollbar track. Defaults to 0.
     * 
     * @param trackBorderRadius trackBorderRadius
     * @return self
     */
    public Scrollbar trackBorderRadius(Integer trackBorderRadius) {
        this.trackBorderRadius = trackBorderRadius;
        return this;
    }



    /**
     * The width of the border of the scrollbar track. Defaults to 1.
     * 
     * @param trackBorderWidth trackBorderWidth
     * @return self
     */
    public Scrollbar trackBorderWidth(Integer trackBorderWidth) {
        this.trackBorderWidth = trackBorderWidth;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toString(enabled);
    }
}
