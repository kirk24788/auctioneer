package de.mancino.auctioneer.webapp.components.highstock;

import java.io.Serializable;
import java.util.Random;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

import de.mancino.auctioneer.dto.components.Color;
import de.mancino.auctioneer.webapp.CashLogPage;

/*


            "$(function() {    window.chart = new Highcharts.StockChart(\n" +
            "  { chart: {renderTo: 'highchart', \n" +
            "            backgroundColor: '#ffffff'},\n" +
            "    rangeSelector: { buttons: [{ type: 'hour', count: 24, text: '24h'}, \n" +
            "                               { type: 'day', count: 7, text: '1w'}, \n" +
            "                               { type: 'all', count: 1, text: 'Alle' }],\n" +
            "                     selected: 1,\n" +
            "                     inputEnabled: false },\n" +
            "    navigator: {enabled: false},\n" +
            "    scrollbar: {enabled: false},\n" +
            "    title: { text: '' },\n" +
            "    legend: { enabled: true,\n" +
            "              floating: false },\n" +
            "    xAxis: { maxZoom: 60 * 60 * 1000 },\n" +
            "    yAxis: [{ title: { text: 'Gold pro Char' },\n" +
            "              min: 0,\n" +
            "              height: 200,\n" +
            "              lineWidth: 2},\n" +
            "            { title: { text: 'Gold Total' },\n" +
            "              top: 250,\n" +
            "              height: 100,\n" +
            "              offset: 0,\n" +
            "              lineWidth: 2}],\n" +
            "   series: seriesOptions});});\n";


 */

/**
 * Highstock Chart Object for Wicket.
 * 
 * For documentation see: http://api.highcharts.com/highstock
 *
 * @author mmancino
 */
public class Highstock extends JSObject implements Serializable {
    public static final long VERSION = 182L;
    public static final String GLOBALS_ID = "highstock_globals_" + VERSION;
    private static final long serialVersionUID = VERSION;

    public static final CompressedResourceReference JQUERY_JS = new CompressedResourceReference(CashLogPage.class, "js/jquery-1.8.2.js");
    public static final CompressedResourceReference HIGHCHART_BASE_JS = new CompressedResourceReference(CashLogPage.class, "js/highstock-1.2.5/highstock.js");
    public static final CompressedResourceReference EXPORTING_JS = new CompressedResourceReference(CashLogPage.class, "js/highstock-1.2.5/exporting.js");


    private String id;

    private Chart chart;
    private JSArray<Highstock, Color> colors;
    private Credits credits;
    private Exporting exporting;
    private static Global global;
    // XXX: labels -> not yet supported
    private static Lang lang;
    private Legend legend;
    // XXX: loading -> not yet supported
    // XXX: navigation -> not yet supported
    private Navigator navigator;
    // XXX: plotOptions -> not yet supported
    private RangeSelector rangeSelector;
    private Scrollbar scrollbar;
    // XXX: series -> not yet supported
    // XXX: subtitle -> not yet supported
    private Title title;
    // XXX: tooltip -> not yet supported
    private JSArray<Highstock, XAxis> xAxis;
    private JSArray<Highstock, YAxis> yAxis;
    private JSArray<Highstock, Series> series;

    public Highstock() {
        id = "highstock_" + String.valueOf(new Random().nextInt(10000));
    }

    public Highstock(final String id) {
        this.id = id;
    }

    public void render(final IHeaderResponse response) {
        response.renderJavascriptReference(JQUERY_JS);
        response.renderJavascriptReference(HIGHCHART_BASE_JS);
        response.renderJavascriptReference(EXPORTING_JS);
        response.renderJavascript(printGlobals(), GLOBALS_ID);
        response.renderJavascript(printStockChart(), id);
    }

    /**
     * Language object. The language object is global and it can't be set on each chart initiation.
     * 
     * @return Lang
     */
    public static Lang lang() {
        if(lang == null) {
            lang = new Lang();
        }
        return lang;
    }

    /**
     * Language object. The language object is global and it can't be set on each chart initiation.
     * 
     * @param lang Lang
     */
    public static void lang(Lang lang) {
        Highstock.lang = lang;
    }

    /**
     * Options regarding the chart area and plot area as well as general chart options.
     * 
     * @param renderTo The HTML element where the chart will be rendered. If it is a string, the element by that id is used.
     * @return Chart
     */
    public Chart chart(final String renderTo) {
        if(this.chart == null) {
            this.chart = new Chart(renderTo);
        }
        this.chart.renderTo(renderTo);
        return this.chart;
    }

    /**
     * An array containing the default colors for the chart's series. When all colors are used, new colors are pulled from the start again.
     * Defaults to colors: <tt>['#efefef']</tt>.
     * Original Default:<tt>['#4572A7', '#AA4643', '#89A54E', '#80699B', '#3D96AE', '#DB843D', '#92A8CD', '#A47D7C', '#B5CA92']</tt>
     * @return colors
     */
    public JSArray<Highstock, Color> colors() {
        if(this.colors == null) {
            this.colors = new JSArray<Highstock, Color>(this);
        }
        return this.colors;
    }

    /**
     * Highchart by default puts a credits label in the lower right corner of the chart. This can be changed using these options.
     * 
     * @return Credits
     */
    public Credits credits() {
        if(this.credits == null) {
            this.credits = new Credits();
        }
        return this.credits;
    }
    
    /**
     * Options for the exporting module.
     * 
     * @return Exporting
     */
    public Exporting exporting() {
        if(this.exporting == null) {
            this.exporting = new Exporting();
        }
        return this.exporting;
    }
    
    /**
     * Global options that don't apply to each chart. These options, like the lang options, must be set using the 
     * Highcharts.setOptions method.
     * 
     * @return Global
     */
    public static Global global() {
        if(Highstock.global == null) {
            Highstock.global = new Global();
        }
        return Highstock.global;
    }

    /**
     * The legend is a box containing a symbol and name for each series item or point item in the chart.
     * 
     * @return Legend
     */
    public Legend legend() {
        if(this.legend == null) {
            this.legend = new Legend();
        }
        return this.legend;
    }

    /**
     * The navigator is a small series below the main series, displaying a view of the entire data set. It provides tools to 
     * zoom in and out on parts of the data as well as panning across the dataset.
     * 
     * @return Navigator
     */
    public Navigator navigator() {
        if(this.navigator == null) {
            this.navigator = new Navigator();
        }
        return this.navigator;
    }


    /**
     * The range selector is a tool for selecting ranges to display within the chart. It provides buttons to select 
     * preconfigured ranges in the chart, like 1 day, 1 week, 1 month etc. It also provides input boxes where 
     * min and max dates can be manually input.
     * 
     * @return RangeSelector
     */
    public RangeSelector rangeSelector() {
        if(this.rangeSelector == null) {
            this.rangeSelector = new RangeSelector();
        }
        return this.rangeSelector;
    }

    /**
     * The scrollbar is a means of panning over the X axis of a chart.
     * 
     * @return Scrollbar
     */
    public Scrollbar scrollbar() {
        if(this.scrollbar == null) {
            this.scrollbar = new Scrollbar();
        }
        return this.scrollbar;
    }

    /**
     * The actual series to append to the chart. In addition to the members listed below, any member of the plotOptions for 
     * that specific type of plot can be added to a series individually. For example, even though a general lineWidth is 
     * specified in plotOptions.series, an individual lineWidth can be specified for each series.
     * 
     * @return Scrollbar
     */
    public JSArray<Highstock, Series> series() {
        if(this.series == null) {
            this.series = new JSArray<Highstock, Series>(this);
        }
        return this.series;
    }

    /**
     * The chart's main title.
     * 
     * @return Title
     */
    public Title title() {
        if(this.title == null) {
            this.title = new Title();
        }
        return this.title;
    }


    /**
     * The X axis or category axis. Normally this is the horizontal axis, though if the chart is inverted this is the vertical 
     * axis. In case of multiple axes, the xAxis node is an array of configuration objects.
     * 
     * @return xAxis
     */
    public JSArray<Highstock, XAxis> xAxis() {
        if(this.xAxis == null) {
            this.xAxis = new JSArray<Highstock, XAxis>(this);
        }
        return this.xAxis;
    }

    /**
     * The Y axis or value axis. In case of multiple axes, the yAxis node is an array of configuration objects.
     * 
     * @return xAxis
     */
    public JSArray<Highstock, YAxis> yAxis() {
        if(this.yAxis == null) {
            this.yAxis = new JSArray<Highstock, YAxis>(this);
        }
        return this.yAxis;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return printGlobals() + LINE_BREAK + printStockChart();
    };

    public String printGlobals() {
        final StringBuffer globals = new StringBuffer();
        globals.append("$(function() {").append(LINE_BREAK);
        globals.append("Highcharts.setOptions({").append(LINE_BREAK);
        globals.append(printObject("lang"));
        globals.append(printObject("global"));
        globals.append("})});").append(LINE_BREAK);
        return globals.toString();
    }

    public String printStockChart() {
        final StringBuffer stockchart = new StringBuffer();

        stockchart.append("$(function() {").append(LINE_BREAK);
        stockchart.append("window.chart = new Highcharts.StockChart({").append(LINE_BREAK);
        stockchart.append(printObject("chart"));
        stockchart.append(printObject("colors"));
        stockchart.append(printObject("credits"));
        stockchart.append(printObject("exporting"));
        stockchart.append(printObject("legend"));
        stockchart.append(printObject("navigator"));
        stockchart.append(printObject("rangeSelector"));
        stockchart.append(printObject("scrollbar"));
        stockchart.append(printObject("title"));
        stockchart.append(printObject("xAxis"));
        stockchart.append(printObject("yAxis"));
        stockchart.append(printObject("series"));

        /*
         
                "   series: seriesOptions});});\n";
         */
        stockchart.append("})});").append(LINE_BREAK);
        return stockchart.toString();
    }

    @Override
    protected int identLevel() {
        return 2;
    }
}
