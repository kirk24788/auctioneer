/**
 * 
 */
package de.mancino.auctioneer.webapp.components.highstock;

import java.io.Serializable;
import java.util.List;

import de.mancino.auctioneer.dto.components.Color;

/**
 * The actual series to append to the chart. In addition to the members listed below, any member of the plotOptions for 
 * that specific type of plot can be added to a series individually. For example, even though a general lineWidth is specified 
 * in plotOptions.series, an individual lineWidth can be specified for each series.
 * 
 * @author mmancino
 */
@SuppressWarnings("unused") // Attributes are accesses via reflection!
public class Series extends JSObject implements Serializable {
    @JSIgnore
    private static final long serialVersionUID = Highstock.VERSION;

    private JSArray<Series, Data> data;
    private Integer index;
    private String name;
    private String type;
    private Integer xAxis;
    private Integer yAxis;
    private Color color;
    private Integer legendIndex;
    private Integer lineWidth;
    private Boolean shadow;
    private Boolean showInLegend;
    private Boolean showCheckbox;
    private Boolean visible;

    /**
     * The actual series to append to the chart. In addition to the members listed below, any member of the plotOptions for 
     * that specific type of plot can be added to a series individually. For example, even though a general lineWidth is specified 
     * in plotOptions.series, an individual lineWidth can be specified for each series.
     * 
     * @param data initial data
     */
    public Series(final List<Data> data) {
        for(Data date : data) {
            data().add(date);
        }
    }

    /**
     * The actual series to append to the chart. In addition to the members listed below, any member of the plotOptions for 
     * that specific type of plot can be added to a series individually. For example, even though a general lineWidth is specified 
     * in plotOptions.series, an individual lineWidth can be specified for each series.
     */
    public Series() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int identLevel() {
        return 4;
    }

    /**
     * An array of data points for the series. The series object is expecting the points to be ordered from low to high. 
     * The reason for this is to increase performance. While in many cases the data is fetched from a server, 
     * it's also more convenient to sort on the server and thereby save on client resources.
     * 
     * @param x X
     * @param y Y
     * @return self
     */
    public Series data(final long x, final long y) {
        data().add(new Data(x,y));
        return this;
    }

    /**
     * An array of data points for the series. The series object is expecting the points to be ordered from low to high. 
     * The reason for this is to increase performance. While in many cases the data is fetched from a server, 
     * it's also more convenient to sort on the server and thereby save on client resources.
     * 
     * @return data
     */
    public JSArray<Series, Data> data() {
        if(data == null) {
            this.data = new JSArray<Series, Data>(this);
        }
        return this.data;
    }

    /**
     * The index of the series in the chart, affecting the internal index in the chart.series array, the visible Z index as 
     * well as the order in the legend. Defaults to undefined.
     *
     * @param index index
     * @return self
     */
    public Series index(Integer index) {
        this.index = index;
        return this;
    }

    /**
     * The name of the series as shown in the legend, tooltip etc. Defaults to "".
     *
     * @param name name
     * @return self
     */
    public Series name(String name) {
        this.name = name;
        return this;
    }

    /**
     * The type of series. Can be one of area, areaspline, bar, column, line, pie, scatter, spline, candlestick or ohlc. 
     * Since 1.1.7, types can also be arearange, areasplinerange and columnrange. Defaults to "line".
     *
     * @param type type
     * @return self
     */
    public Series type(String type) {
        this.type = type;
        return this;
    }

    /**
     * When using dual or multiple x axes, this number defines which xAxis the particular series is connected to. It refers 
     * to the index of the axis in the xAxis array, with 0 being the first. Defaults to 0.
     *
     * @param xAxis xAxis
     * @return self
     */
    public Series xAxis(Integer xAxis) {
        this.xAxis = xAxis;
        return this;
    }

    /**
     * When using dual or multiple y axes, this number defines which yAxis the particular series is connected to. It refers 
     * to the index of the axis in the yAxis array, with 0 being the first. Defaults to 0.
     *
     * @param yAxis yAxis
     * @return self
     */
    public Series yAxis(Integer yAxis) {
        this.yAxis = yAxis;
        return this;
    }

    /**
     * The main color of the series. In line type series it applies to the line and the point markers unless otherwise specified.
     * In bar type series it applies to the bars unless a color is specified per point. The default value is pulled 
     * from the options.colors array.
     *
     * @param color color
     * @return self
     */
    public Series color(Color color) {
        this.color = color;
        return this;
    }

    /**
     * The sequential index of the series within the legend. Defaults to 0.
     *
     * @param legendIndex legendIndex
     * @return self
     */
    public Series legendIndex(Integer legendIndex) {
        this.legendIndex = legendIndex;
        return this;
    }

    /**
     * Pixel with of the graph line. Defaults to 2.
     *
     * @param lineWidth lineWidth
     * @return self
     */
    public Series lineWidth(Integer lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    /**
     * Whether to apply a drop shadow to the graph line. Since 1.1.7 the shadow can be an object configuration containing color, 
     * offsetX, offsetY, opacity and width. Defaults to false.
     *
     * @param shadow shadow
     * @return self
     */
    public Series shadow(Boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    /**
     * Whether to display this particular series or series type in the legend. Defaults to true.
     *
     * @param showInLegend showInLegend
     * @return self
     */
    public Series showInLegend(Boolean showInLegend) {
        this.showInLegend = showInLegend;
        return this;
    }

    /**
     * If true, a checkbox is displayed next to the legend item to allow selecting the series. The state of the checkbox is 
     * determined by the selected option. Defaults to false.
     *
     * @param showCheckbox showCheckbox
     * @return self
     */
    public Series showCheckbox(Boolean showCheckbox) {
        this.showCheckbox = showCheckbox;
        return this;
    }

    /**
     *
     *
     * @param visible visible
     * @return self
     */
    public Series visible(Boolean visible) {
        this.visible = visible;
        return this;
    }


}
