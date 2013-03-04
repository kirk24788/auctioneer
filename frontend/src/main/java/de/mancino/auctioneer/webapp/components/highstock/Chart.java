package de.mancino.auctioneer.webapp.components.highstock;

import java.io.Serializable;

import de.mancino.auctioneer.dto.components.Color;

/**
 * Options regarding the chart area and plot area as well as general chart options.
 * 
 * @author mmancino
 */
@SuppressWarnings("unused") // Attributes are accesses via reflection!
public class Chart extends JSObject implements Serializable {
    @JSIgnore
    private static final long serialVersionUID = Highstock.VERSION;

    private boolean alignTicks = true;
    private boolean animation = true;
    private Color backgroundColor = new Color("FFFFFF");
    private Color borderColor = new Color("4572A7");
    private Integer borderRadius = 5;
    private Integer borderWidth = 0;
    private String className = "";
    // XXX: events -> Event Listeners...not supported yet!
    private Integer height = null;
    private Boolean ignoreHiddenSeries;
    // XXX: margin -> Margin as Array...not supported yet - use marginXXX instead!
    private Integer marginBottom = null;
    private Integer marginLeft = null;
    private Integer marginRight = null;
    private Integer marginTop = null;
    private Boolean panning = true;
    private Color plotBackgroundColor = null;
    // XXX: plotBackgroundImage -> Background image...not supported yet!
    private Color plotBorderColor = new Color("C0C0C0");
    private Integer plotBorderWidth = 0;
    private Boolean plotShadow;
    private Boolean reflow;
    private String renderTo;
    // XXX: selectionMarkerFill -> RGBA Color...not supported yet!
    private Boolean shadow;
    private Integer spacingBottom = 15;
    private Integer spacingLeft = 10;
    private Integer spacingRight = 10;
    private Integer spacingTop = 10;
    // XXX: style -> CSS Style...not supported yet!
    private String type = "line";
    private Integer width = null;
    private String zoomType = null;
    
    /**
     * Options regarding the chart area and plot area as well as general chart options.
     */
    Chart(final String renderTo) {
        this.renderTo = renderTo;
    }
    
    /**
     * When using multiple axis, the ticks of two or more opposite axes will automatically be aligned by adding ticks to the 
     * axis or axes with the least ticks. This can be prevented by setting alignTicks to false. 
     * If the grid lines look messy, it's a good idea to hide them for the secondary axis by setting gridLineWidth to 0. 
     * Defaults to true.
     * 
     * @param alignTicks alignTicks
     * @return self
     */
    public Chart alignTicks(boolean alignTicks) {
        this.alignTicks = alignTicks;
        return this;
    }
    
    /**
     * Set the overall animation for all chart updating. Animation can be disabled throughout the chart by setting it to 
     * false here. It can be overridden for each individual API method as a function parameter. The only animation not affected by this option is the initial series animation, see plotOptions.series => animation.
     * 
     * @param animation animation
     * @return self
     */
    public Chart animation(boolean animation) {
        this.animation = animation;
        return this;
    }
    
    

    /**
     * The background color or gradient for the outer chart area. Defaults to "#FFFFFF".
     * 
     * @param backgroundColor backgroundColor
     * @return self
     */
    public Chart backgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * The color of the outer chart border. The border is painted using vector graphic techniques to allow rounded corners. 
     * Defaults to "#4572A7".
     * 
     * @param borderColor borderColor
     * @return self
     */
    public Chart borderColor(Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    /**
     * The corner radius of the outer chart border. Defaults to 5.
     * 
     * @param borderRadius borderRadius
     * @return self
     */
    public Chart borderRadius(Integer borderRadius) {
        this.borderRadius = borderRadius;
        return this;
    }

    /**
     * The pixel width of the outer chart border. The border is painted using vector graphic techniques to allow rounded 
     * corners. Defaults to 0.
     * 
     * @param borderWidth borderWidth
     * @return self
     */
    public Chart borderWidth(Integer borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    /**
     * A CSS class name to apply to the charts container div, allowing unique CSS styling for each chart. Defaults to "".
     * 
     * @param className className
     * @return self
     */
    public Chart className(String className) {
        this.className = className;
        return this;
    }

    /**
     * An explicit height for the chart. By default the height is calculated from the offset height of the containing element. 
     * Defaults to null.
     * 
     * @param height height
     * @return self
     */
    public Chart height(Integer height) {
        this.height = height;
        return this;
    }

    /**
     * If true, the axes will scale to the remaining visible series once one series is hidden. If false, hiding and showing 
     * a series will not affect the axes or the other series. For stacks, once one series within the stack is hidden, 
     * the rest of the stack will close in around it even if the axis is not affected. Defaults to true.
     * 
     * @param ignoreHiddenSeries ignoreHiddenSeries
     * @return self
     */
    public Chart ignoreHiddenSeries(boolean ignoreHiddenSeries) {
        this.ignoreHiddenSeries = ignoreHiddenSeries;
        return this;
    }

    /**
     * The margin between the bottom outer edge of the chart and the plot area. Use this to set a fixed pixel value for 
     * the margin as opposed to the default dynamic margin. See also spacingBottom. Defaults to null.
     * 
     * @param marginBottom marginBottom
     * @return self
     */
    public Chart marginBottom(Integer marginBottom) {
        this.marginBottom = marginBottom;
        return this;
    }

    /**
     * The margin between the left outer edge of the chart and the plot area. Use this to set a fixed pixel value for the 
     * margin as opposed to the default dynamic margin. See also spacingLeft. Defaults to null.
     * 
     * @param marginLeft marginLeft
     * @return self
     */
    public Chart marginLeft(Integer marginLeft) {
        this.marginLeft = marginLeft;
        return this;
    }

    /**
     * The margin between the right outer edge of the chart and the plot area. Use this to set a fixed pixel value for the 
     * margin as opposed to the default dynamic margin. See also spacingRight. Defaults to null.
     * 
     * @param marginRight marginRight
     * @return self
     */
    public Chart marginRight(Integer marginRight) {
        this.marginRight = marginRight;
        return this;
    }

    /**
     * The margin between the top outer edge of the chart and the plot area. Use this to set a fixed pixel value for the 
     * margin as opposed to the default dynamic margin. See also spacingTop. Defaults to null.
     * 
     * @param marginTop marginTop
     * @return self
     */
    public Chart marginTop(Integer marginTop) {
        this.marginTop = marginTop;
        return this;
    }

    /**
     * Allow panning the zoomed area by click and drag on the chart. When the zoomType option is set, panning is disabled. 
     * Defaults to true
     * 
     * @param panning panning
     * @return self
     */
    public Chart panning(boolean panning) {
        this.panning = panning;
        return this;
    }

    /**
     * The background color or gradient for the plot area. Defaults to null.
     * 
     * @param plotBackgroundColor plotBackgroundColor
     * @return self
     */
    public Chart plotBackgroundColor(Color plotBackgroundColor) {
        this.plotBackgroundColor = plotBackgroundColor;
        return this;
    }

    /**
     * The color of the inner chart or plot area border. Defaults to "#C0C0C0".
     * 
     * @param plotBorderColor
     * @return self
     */
    public Chart plotBorderColor(Color plotBorderColor) {
        this.plotBorderColor = plotBorderColor;
        return this;
    }

    /**
     * The pixel width of the plot area border. Defaults to 0.
     * 
     * @param plotBorderWidth
     * @return self
     */
    public Chart plotBorderWidth(Integer plotBorderWidth) {
        this.plotBorderWidth = plotBorderWidth;
        return this;
    }

    /**
     * Whether to apply a drop shadow to the plot area. Requires that plotBackgroundColor be set. Defaults to false.
     * 
     * @param plotShadow plotShadow
     * @return self
     */
    public Chart plotShadow(boolean plotShadow) {
        this.plotShadow = plotShadow;
        return this;
    }

    /**
     * Whether to reflow the chart to fit the width of the container div on resizing the window. Defaults to true.
     * 
     * @param reflow reflow
     * @return self
     */
    public Chart reflow(boolean reflow) {
        this.reflow = reflow;
        return this;
    }

    /**
     * Whether to apply a drop shadow to the outer chart area. Requires that backgroundColor be set. Defaults to false.
     * 
     * @param shadow shadow
     * @return self
     */
    public Chart shadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    /**
     * The space between the bottom edge of the chart and the content (plot area, axis title and labels, title, subtitle or 
     * legend in top position). Defaults to 15.
     * 
     * @param spacingBottom spacingBottom
     * @return
     */
    public Chart spacingBottom(Integer spacingBottom) {
        this.spacingBottom = spacingBottom;
        return this;
    }

    /**
     * The space between the left edge of the chart and the content (plot area, axis title and labels, title, subtitle or 
     * legend in top position). Defaults to 10.
     * 
     * @param spacingLeft spacingLeft
     * @return self
     */
    public Chart spacingLeft(Integer spacingLeft) {
        this.spacingLeft = spacingLeft;
        return this;
    }

    /**
     * The space between the right edge of the chart and the content (plot area, axis title and labels, title, subtitle or 
     * legend in top position). Defaults to 10.
     * 
     * @param spacingRight spacingRight
     * @return self
     */
    public Chart spacingRight(Integer spacingRight) {
        this.spacingRight = spacingRight;
        return this;
    }

    /**
     * The space between the top edge of the chart and the content (plot area, axis title and labels, title, subtitle or 
     * legend in top position). Defaults to 10.
     * 
     * @param spacingTop spacingTop
     * @return self
     */
    public Chart spacingTop(Integer spacingTop) {
        this.spacingTop = spacingTop;
        return this;
    }

    /**
     * The default series type for the chart. Can be one of line, spline, area, areaspline, column, scatter, ohlc and 
     * candlestick. Since 1.1.7, types can also be arearange, areasplinerange and columnrange. 
     * Defaults to "line".
     * 
     * @param type type
     * @return self
     */
    public Chart type(String type) {
        this.type = type;
        return this;
    }

    /**
     * An explicit width for the chart. By default the width is calculated from the offset width of the containing element. 
     * Defaults to null.
     * 
     * @param width width
     * @return self
     */
    public Chart width(Integer width) {
        this.width = width;
        return this;
    }

    /**
     * Decides in what dimentions the user can zoom by dragging the mouse. Can be one of x, y or xy. 
     * Defaults to "".
     * 
     * @param zoomType zoomType
     * @return self
     */
    public Chart zoomType(String zoomType) {
        this.zoomType = zoomType;
        return this;
    }

    @Override
    protected int identLevel() {
        return 4;
    }

    /**
     * The HTML element where the chart will be rendered. If it is a string, the element by that id is used. The 
     * HTML element can also be passed by direct reference. Defaults to null.
     * 
     * @param renderTo renderTo
     * @return self
     */
    public Chart renderTo(String renderTo) {
        this.renderTo = renderTo;
        return this;
    }
}
