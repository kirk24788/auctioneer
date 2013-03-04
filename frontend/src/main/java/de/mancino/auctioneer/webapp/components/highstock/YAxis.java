/**
 * 
 */
package de.mancino.auctioneer.webapp.components.highstock;

import java.io.Serializable;

import de.mancino.auctioneer.dto.components.Color;

/**
 * The Y axis or value axis. In case of multiple axes, the yAxis node is an array of configuration objects.
 * 
 * @author mmancino
 */
@SuppressWarnings("unused") // Attributes are accesses via reflection!
public class YAxis extends JSObject implements Serializable {
    @JSIgnore
    private static final long serialVersionUID = Highstock.VERSION;

    // XXX: Many attributes missing!!!
    private Integer height;
    private Integer top;

    /**
     * The height of the Y axis in pixels. Defaults to null.
     *
     * @param height height
     * @return self
     */
    public YAxis height(Integer height) {
        this.height = height;
        return this;
    }

    /**
     * The top pixel position of the Y axis relative to the chart. Defaults to null.
     *
     * @param top top
     * @return self
     */
    public YAxis top(Integer top) {
        this.top = top;
        return this;
    }


    
    // XXX: Many attributes missing!!!
    private Color alternateGridColor;
    private Color gridLineColor;
    private String gridLineDashStyle;
    private String gridLineWidth;
    private Color lineColor;
    private Integer lineWidth;
    private Integer linkedTo;
    private Integer max;
    private Integer maxPadding;
    private Integer min;
    private Integer minPadding;
    private Integer minRange;
    private Integer minTickInterval;
    private Integer offset;
    private Boolean opposite;
    private Boolean ordinal;
    private Boolean reversed;
    private Boolean showEmpty;
    private Integer startOfWeek;
    private Title title;

    
    /**
     * {@inheritDoc}
     */
    @Override
    protected int identLevel() {
        return 4;
    }

    /**
     * When using an alternate grid color, a band is painted across the plot area between every other grid line. Defaults to null.
     *
     * @param alternateGridColor alternateGridColor
     * @return self
     */
    public YAxis alternateGridColor(Color alternateGridColor) {
        this.alternateGridColor = alternateGridColor;
        return this;
    }

    /**
     * Color of the grid lines extending the ticks across the plot area. Defaults to "#C0C0C0".
     *
     * @param gridLineColor gridLineColor
     * @return self
     */
    public YAxis gridLineColor(Color gridLineColor) {
        this.gridLineColor = gridLineColor;
        return this;
    }

    /**
     * The dash or dot style of the grid lines. For possible values, see 
     * <a href="http://jsfiddle.net/cSrgA/">this demonstration</a>. Defaults to Solid.
     *
     * @param gridLineDashStyle gridLineDashStyle
     * @return self
     */
    public YAxis gridLineDashStyle(String gridLineDashStyle) {
        this.gridLineDashStyle = gridLineDashStyle;
        return this;
    }

    /**
     * The width of the grid lines extending the ticks across the plot area. Defaults to 0.
     *
     * @param gridLineWidth gridLineWidth
     * @return self
     */
    public YAxis gridLineWidth(String gridLineWidth) {
        this.gridLineWidth = gridLineWidth;
        return this;
    }

    /**
     * The color of the line marking the axis itself. Defaults to "#C0D0E0".
     *
     * @param lineColor lineColor
     * @return self
     */
    public YAxis lineColor(Color lineColor) {
        this.lineColor = lineColor;
        return this;
    }

    /**
     * The width of the line marking the axis itself. Defaults to 1.
     *
     * @param lineWidth lineWidth
     * @return self
     */
    public YAxis lineWidth(Integer lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    /**
     * Index of another axis that this axis is linked to. When an axis is linked to a master axis, it will take the same 
     * extremes as the master, but as assigned by min or max or by setExtremes. It can be used to show additional 
     * info, or to ease reading the chart by duplicating the scales. Defaults to null.
     *
     * @param linkedTo linkedTo
     * @return self
     */
    public YAxis linkedTo(Integer linkedTo) {
        this.linkedTo = linkedTo;
        return this;
    }

    /**
     * The maximum value of the axis. If null, the max value is automatically calculated. If the endOnTick option is true, 
     * the max value might be rounded up. The actual maximum value is also influenced by chart.alignTicks. Defaults to null.
     *
     * @param max max
     * @return self
     */
    public YAxis max(Integer max) {
        this.max = max;
        return this;
    }

    /**
     * Padding of the max value relative to the length of the axis. A padding of 0.05 will make a 100px axis 5px longer. This 
     * is useful when you don't want the highest data value to appear on the edge of the plot area. When the axis' max 
     * option is set or a max extreme is set using axis.setExtremes(), the maxPadding will be ignored. Defaults to 0.
     *
     * @param maxPadding maxPadding
     * @return self
     */
    public YAxis maxPadding(Integer maxPadding) {
        this.maxPadding = maxPadding;
        return this;
    }

    /**
     * The minimum value of the axis. If null the min value is automatically calculated. If the startOnTick option is true, the 
     * min value might be rounded down. Defaults to null.
     *
     * @param min min
     * @return self
     */
    public YAxis min(Integer min) {
        this.min = min;
        return this;
    }

    /**
     * Padding of the min value relative to the length of the axis. A padding of 0.05 will make a 100px axis 5px longer. 
     * This is useful when you don't want the lowest data value to appear on the edge of the plot area. When the axis' min 
     * option is set or a min extreme is set using axis.setExtremes(), the minPadding will be ignored. Defaults to 0.
     *
     * @param minPadding minPadding
     * @return self
     */
    public YAxis minPadding(Integer minPadding) {
        this.minPadding = minPadding;
        return this;
    }

    /**
     * The minimum range to display. The entire axis will not be allowed to span over a smaller interval than this. For example, 
     * for a datetime axis the main unit is milliseconds. If minRange is set to 3600000, you can't zoom in more than to one hour.
     *
     * @param minRange minRange
     * @return self
     */
    public YAxis minRange(Integer minRange) {
        this.minRange = minRange;
        return this;
    }

    /**
     * The minimum tick interval allowed in axis values. For example on zooming in on an axis with daily data, this can be used 
     * to prevent the axis from showing hours. Defaults to undefined.
     *
     * @param minTickInterval minTickInterval
     * @return self
     */
    public YAxis minTickInterval(Integer minTickInterval) {
        this.minTickInterval = minTickInterval;
        return this;
    }

    /**
     * The distance in pixels from the plot area to the axis line. A positive offset moves the axis with it's line, labels and 
     * ticks away from the plot area. This is typically used when two or more axes are displayed on the same side of the plot. 
     * Defaults to 0.
     *
     * @param offset offset
     * @return self
     */
    public YAxis offset(Integer offset) {
        this.offset = offset;
        return this;
    }

    /**
     * Whether to display the axis on the opposite side of the normal. The normal is on the left side for vertical axes and 
     * bottom for horizontal, so the opposite sides will be right and top respectively. This is typically used with dual or 
     * multiple axes. Defaults to false.
     *
     * @param opposite opposite
     * @return self
     */
    public YAxis opposite(Boolean opposite) {
        this.opposite = opposite;
        return this;
    }

    /**
     * In an ordinal axis, the points are equally spaced in the chart regardless of the actual time or x distance between them. 
     * This means that missing data for nights or weekends will not take up space in the chart. Defaults to true.
     *
     * @param ordinal ordinal
     * @return self
     */
    public YAxis ordinal(Boolean ordinal) {
        this.ordinal = ordinal;
        return this;
    }

    /**
     * Whether to reverse the axis so that the highest number is closest to origo. Defaults to false.
     *
     * @param reversed reversed
     * @return self
     */
    public YAxis reversed(Boolean reversed) {
        this.reversed = reversed;
        return this;
    }

    /**
     * Whether to show the axis line and title when the axis has no data. Defaults to true.
     *
     * @param showEmpty showEmpty
     * @return self
     */
    public YAxis showEmpty(Boolean showEmpty) {
        this.showEmpty = showEmpty;
        return this;
    }

    /**
     * For datetime axes, this decides where to put the tick between weeks. 0 = Sunday, 1 = Monday. Defaults to 1.
     *
     * @param startOfWeek startOfWeek
     * @return self
     */
    public YAxis startOfWeek(Integer startOfWeek) {
        this.startOfWeek = startOfWeek;
        return this;
    }

    /**
     * The axis title, showing next to the axis line.
     *
     * @param title title
     * @return self
     */
    public YAxis title(String title) {
        this.title = new Title().text(title);
        return this;
    }
}
