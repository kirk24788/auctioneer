package de.mancino.auctioneer.webapp.components.highstock;

import java.io.Serializable;

import de.mancino.auctioneer.dto.components.Color;

/**
 * The legend is a box containing a symbol and name for each series item or point item in the chart.
 * 
 * @author mmancino
 */
@SuppressWarnings("unused") // Attributes are accesses via reflection!
public class Legend extends JSObject implements Serializable {
    @JSIgnore
    private static final long serialVersionUID = Highstock.VERSION;

    private String align = "center";
    private Color backgroundColor;
    private Color borderColor = new Color("909090"); 
    private Integer borderRadius = 5;
    private Integer borderWidth = 1;
    private boolean enabled = false;
    private boolean floating = false;
    // XXX: itemHiddenStyle -> CSS...not yet supported
    // XXX:itemHoverStyle -> CSS...not yet supported
    private Integer itemMarginBottom = 0;
    private Integer itemMarginTop = 0;
    // XXX:itemStyle -> CSS...not yet supported
    private Integer itemWidth = null;
    // XXX: labelFormatter -> Callback Function...not yet supported
    private String layout = "horizontal";
    private Integer margin = 15;
    private Integer maxHeight = null;
    // XXX: navigation -> Navigation Options...not yet supported
    private Integer padding = 8;
    private boolean reversed = false;
    private boolean shadow = false;
    /// XXX: style -> CSS...not yet supported
    private Integer symbolPadding = 5;
    private Integer symbolWidth = 30;
    private boolean useHTML = false;
    private String verticalAlign = "bottom";
    private Integer width = null;
    private Integer x = 0;
    private Integer y = 0;
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected int identLevel() {
        return 4;
    }

    /**
     * The horizontal alignment of the legend box within the chart area. Defaults to "center".
     * 
     * @param align align
     * @return self
     */
    public Legend align(String align) {
        this.align = align;
        return this;
    }

    /**
     * The background color of the legend, filling the rounded corner border. Defaults to null.
     * 
     * @param backgroundColor backgroundColor
     * @return self
     */
    public Legend backgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * The color of the drawn border around the legend. Defaults to #909090.
     * 
     * @param borderColor borderColor
     * @return self
     */
    public Legend borderColor(Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    /**
     * The border corner radius of the legend. Defaults to 5.
     * 
     * @param borderRadius borderRadius
     * @return self
     */
    public Legend borderRadius(Integer borderRadius) {
        this.borderRadius = borderRadius;
        return this;
    }

    /**
     * The width of the drawn border around the legend. Defaults to 1.
     * 
     * @param borderWidth borderWidth
     * @return self
     */
    public Legend borderWidth(Integer borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    /**
     * Enable or disable the legend. Defaults to false.
     * 
     * @param enabled enabled
     * @return self
     */
    public Legend enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * When the legend is floating, the plot area ignores it and is allowed to be placed below it. Defaults to false.
     * 
     * @param floating floating
     * @return self
     */
    public Legend floating(boolean floating) {
        this.floating = floating;
        return this;
    }

    /**
     * The pixel bottom margin for each legend item. Defaults to 0.
     * 
     * @param itemMarginBottom itemMarginBottom
     * @return self
     */
    public Legend itemMarginBottom(Integer itemMarginBottom) {
        this.itemMarginBottom = itemMarginBottom;
        return this;
    }

    /**
     * The pixel top margin for each legend item. Defaults to 0.
     * 
     * @param itemMarginTop itemMarginTop
     * @return self
     */
    public Legend itemMarginTop(Integer itemMarginTop) {
        this.itemMarginTop = itemMarginTop;
        return this;
    }

    /**
     * The width for each legend item. This is useful in a horizontal layout with many items when you want the items to 
     * align vertically. . Defaults to null.
     * 
     * @param itemWidth itemWidth
     * @return self
     */
    public Legend itemWidth(Integer itemWidth) {
        this.itemWidth = itemWidth;
        return this;
    }

    /**
     * The layout of the legend items. Can be one of "horizontal" or "vertical". Defaults to "horizontal".
     * 
     * @param layout layout
     * @return self
     */
    public Legend layout(String layout) {
        this.layout = layout;
        return this;
    }

    /**
     * If the plot area sized is calculated automatically and the legend is not floating, the legend margin is the space between 
     * the legend and the axis labels or plot area. Defaults to 15.
     * 
     * @param margin margin
     * @return self
     */
    public Legend margin(Integer margin) {
        this.margin = margin;
        return this;
    }

    /**
     * Maximum pixel height for the legend. When the maximum height is extended, navigation will show. Defaults to null.
     * 
     * @param maxHeight maxHeight
     * @return self
     */
    public Legend maxHeight(Integer maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    /**
     * The inner padding of the legend box. Defaults to 8.
     * 
     * @param padding padding
     * @return self
     */
    public Legend padding(Integer padding) {
        this.padding = padding;
        return this;
    }

    /**
     * Whether to reverse the order of the legend items compared to the order of the series or points as defined in the 
     * configuration object. Defaults to false.
     * 
     * @param reversed reversed
     * @return self
     */
    public Legend reversed(boolean reversed) {
        this.reversed = reversed;
        return this;
    }

    /**
     * Whether to apply a drop shadow to the legend. A backgroundColor also needs to be applied for this to take effect. 
     * Defaults to false.
     * 
     * @param shadow shadow
     * @return self
     */
    public Legend shadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    /**
     * The pixel padding between the legend item symbol and the legend item text. Defaults to 5.
     * 
     * @param symbolPadding symbolPadding
     * @return self
     */
    public Legend symbolPadding(Integer symbolPadding) {
        this.symbolPadding = symbolPadding;
        return this;
    }

    /**
     * The pixel width of the legend item symbol. Defaults to 30.
     * 
     * @param symbolWidth symbolWidth
     * @return self
     */
    public Legend symbolWidth(Integer symbolWidth) {
        this.symbolWidth = symbolWidth;
        return this;
    }

    /**
     * Whether to use HTML to render the legend item texts. Using HTML allows for advanced formatting, images and reliable 
     * bi-directional text rendering. Note that exported images won't respect the HTML, and that HTML won't 
     * respect Z-index settings. Defaults to false.
     * 
     * @param useHTML useHTML
     * @return self
     */
    public Legend useHTML(boolean useHTML) {
        this.useHTML = useHTML;
        return this;
    }

    /**
     * The vertical alignment of the legend box. Can be one of "top", "middle" or "bottom". Vertical position can be further
     * determined by the y option. Defaults to "bottom".
     * 
     * @param verticalAlign verticalAlign
     * @return self
     */
    public Legend verticalAlign(String verticalAlign) {
        this.verticalAlign = verticalAlign;
        return this;
    }
    
    /**
     * The width of the legend box, not including style.padding. . Defaults to null.
     * 
     * @param width width
     * @return self
     */
    public Legend width(Integer width) {
        this.width = width;
        return this;
    }

    /**
     * The x offset of the legend relative to its horizontal alignment align within chart.spacingLeft and chart.spacingRight. 
     * Negative x moves it to the left, positive x moves it to the right. The default value of 15 together with align: 
     * "center" puts it in the center of the plot area. Defaults to 0.
     * 
     * @param x x
     * @return self
     */
    public Legend x(Integer x) {
        this.x = x;
        return this;
    }

    /**
     * The vertical offset of the legend relative to it's vertical alignment verticalAlign within chart.spacingTop and 
     * chart.spacingBottom. Negative y moves it up, positive y moves it down. Defaults to 0.
     * 
     * @param y y
     * @return self
     */
    public Legend y(Integer y) {
        this.y = y;
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
