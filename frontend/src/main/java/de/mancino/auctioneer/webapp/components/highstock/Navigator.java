/**
 * 
 */
package de.mancino.auctioneer.webapp.components.highstock;

import java.io.Serializable;

import de.mancino.auctioneer.dto.components.Color;

/**
 * The navigator is a small series below the main series, displaying a view of the entire data set. It provides tools to 
 * zoom in and out on parts of the data as well as panning across the dataset.
 * 
 * @author mmancino
 */
@SuppressWarnings("unused") // Attributes are accesses via reflection!
public class Navigator extends JSObject implements Serializable {
    @JSIgnore
    private static final long serialVersionUID = Highstock.VERSION;

    private Integer baseSeries = 0;
    private boolean enabled = true;
    // XXX: handles -> ???...not yet supported
    private Integer height;
    private Integer margin;
    private Integer top;
    // XXX: maskFill -> RGBA...not yet supported
    private Color outlineColor = new Color("444444");
    private Integer outlineWidth = 2;
    // XXX: series -> not yet supported
    // XXX: xAxis -> not yet supported
    // XXX: yAxis -> not yet supported

    /**
     * {@inheritDoc}
     */
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

    /**
     * The top pixel position of the Y axis relative to the chart. Defaults to null.
     *
     * @param top top
     * @return self
     */
    public Navigator top(Integer top) {
        this.top = top;
        return this;
    }

    /**
     * An integer identifying the index to use for the base series, or a string representing the id of the series. Defaults to 0.
     * 
     * @param baseSeries baseSeries
     * @return self
     */
    public Navigator baseSeries(Integer baseSeries) {
        this.baseSeries = baseSeries;
        return this;
    }

    /**
     * Enable or disable the navigator. Defaults to true.
     * 
     * @param enabled enabled
     * @return self
     */
    public Navigator enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * The height of the navigator. Defaults to 40.
     * 
     * @param height height
     * @return self
     */
    public Navigator height(Integer height) {
        this.height = height;
        return this;
    }

    /**
     * The distance from the nearest element, the X axis or X axis labels. Defaults to 10.
     * 
     * @param margin margin
     * @return self
     */
    public Navigator margin(Integer margin) {
        this.margin = margin;
        return this;
    }

    /**
     * The color of the line marking the currently zoomed area in the navigator. Defaults to #444444.
     * 
     * @param outlineColor outlineColor
     * @return self
     */
    public Navigator outlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
        return this;
    }

    /**
     * The width of the line marking the currently zoomed area in the navigator. Defaults to 2.
     * 
     * @param outlineWidth outlineWidth
     * @return self
     */
    public Navigator outlineWidth(Integer outlineWidth) {
        this.outlineWidth = outlineWidth;
        return this;
    }
}
