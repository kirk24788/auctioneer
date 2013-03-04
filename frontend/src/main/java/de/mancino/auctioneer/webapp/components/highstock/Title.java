/**
 * 
 */
package de.mancino.auctioneer.webapp.components.highstock;

import java.io.Serializable;

/**
 * The chart's main title.
 * 
 * @author mmancino
 */
@SuppressWarnings("unused") // Attributes are accesses via reflection!
public class Title extends JSObject implements Serializable {
    @JSIgnore
    private static final long serialVersionUID = Highstock.VERSION;

    private String align;
    private Boolean floating;
    private Integer margin;
    // style -> CSS .. not yet supported
    private String text;
    private Boolean useHTML;
    private String verticaAlign;
    private Integer x;
    private Integer y;

    /**
     * {@inheritDoc}
     */
    @Override
    protected int identLevel() {
        return 4;
    }

    /**
     * The horizontal alignment of the title. Can be one of "left", "center" and "right". Defaults to "center".
     *
     * @param align align
     * @return self
     */
    public Title align(String align) {
        this.align = align;
        return this;
    }

    /**
     * When the title is floating, the plot area will not move to make space for it. Defaults to false.
     *
     * @param floating floating
     * @return self
     */
    public Title floating(boolean floating) {
        this.floating = floating;
        return this;
    }

    /**
     * The margin between the title and the plot area, or if a subtitle is present, the margin between the subtitle and the 
     * plot area. Defaults to 15.
     *
     * @param margin margin
     * @return self
     */
    public Title margin(Integer margin) {
        this.margin = margin;
        return this;
    }

    /**
     * The title of the chart. To disable the title, set the text to null. Defaults to null.
     *
     * @param text text
     * @return self
     */
    public Title text(String text) {
        this.text = text;
        return this;
    }

    /**
     * Whether to use HTML to render the title text. Using HTML allows for advanced formatting, images and reliable 
     * bi-directional text rendering. Note that exported images won't respect the HTML, and that HTML won't respect 
     * Z-index settings. Defaults to false.
     *
     * @param useHTML useHTML
     * @return self
     */
    public Title useHTML(boolean useHTML) {
        this.useHTML = useHTML;
        return this;
    }

    /**
     * The vertical alignment of the title. Can be one of "top", "middle" and "bottom". Defaults to "top".
     *
     * @param verticaAlign verticaAlign
     * @return self
     */
    public Title verticaAlign(String verticaAlign) {
        this.verticaAlign = verticaAlign;
        return this;
    }

    /**
     * The x position of the title relative to the alignment within chart.spacingLeft and chart.spacingRight. Defaults to 0.
     *
     * @param x x
     * @return self
     */
    public Title x(Integer x) {
        this.x = x;
        return this;
    }

    /**
     * The y position of the title relative to the alignment within chart.spacingTop and chart.spacingBottom. Defaults to 15.
     *
     * @param y y
     * @return self
     */
    public Title y(Integer y) {
        this.y = y;
        return this;
    }
}
