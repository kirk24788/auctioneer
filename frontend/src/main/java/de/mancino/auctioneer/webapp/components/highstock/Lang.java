package de.mancino.auctioneer.webapp.components.highstock;

import java.io.Serializable;

/**
 * Language object. The language object is global and it can"t be set on each chart initiation. Instead, use 
 * Highcharts.setOptions to set it before any chart is initiated.
 * 
 * @author mmancino
 */
@SuppressWarnings("unused") // Attributes are accesses via reflection!
public class Lang extends JSObject implements Serializable {
    @JSIgnore
    private static final long serialVersionUID = Highstock.VERSION;
    @JSIgnore
    private static final int ARRAY_IDENT = 6;
    @JSIgnore
    public static final Lang GERMAN = new Lang().decimalPoint(".").downloadJPEG("JPEG Bild herunterladen")
    .downloadPDF("PDF Dokument herunterladen").downloadPNG("PNG Bild herunterladen").downloadSVG("SVG Vektor Grafik herunterladen")
    .exportButtonTitle("Exportiere Raster oder Vektor Bild").loading("Lade...")
    .months("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember")
    .printButtonTitle("Diagramm drucken")
    .rangeSelectorFrom("Von").rangeSelectorTo("Bis").rangeSelectorZoom("Zoom").resetZoom("Zoom zurücksetzen")
    .resetZoomTitle("Zoom Level 1:1").thousandsSep("")
    .weekdays("Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag");

    private String decimalPoint = ".";
    private String downloadJPEG = "Download JPEG image";
    private String downloadPDF = "Download JPEG document";
    private String downloadPNG = "Download JPEG image";
    private String downloadSVG = "Download JPEG vector image";
    private String exportButtonTitle = "Export to raster or vector image";
    private String loading = "Loading...";
    private JSArray<Lang, String> months = new JSArray<Lang, String>(this, "January", "February", "March", "April", "May", "June", 
            "July", "August", "September", "October", "November", "December");
    private JSArray<Lang, String> numericSymbols = new JSArray<Lang, String>(this, "k", "M", "G", "T", "P", "E");
    private String printButtonTitle = "Print the chart";
    private String rangeSelectorFrom = "From";
    private String rangeSelectorTo = "To";
    private String rangeSelectorZoom = "Zoom";
    private String resetZoom = "Reset zoom";
    private String resetZoomTitle = "Reset zoom level 1:1";
    private String thousandsSep = "";
    private JSArray<Lang, String> weekdays = new JSArray<Lang, String>(this, "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", 
            "Friday", "Saturday");

    /**
     * {@inheritDoc}
     */
    @Override
    protected int identLevel() {
        return 4;
    }

    /**
     * The default decimal point used in the Highcharts.numberFormat method unless otherwise specified in the function arguments. 
     * Defaults to ".".
     * 
     * @param decimalPoint decimalPoint
     * @return self
     */
    public Lang decimalPoint(String decimalPoint) {
        this.decimalPoint = decimalPoint;
        return this;
    }

    /**
     * Exporting module only. The text for the JPEG download menu item. Defaults to "Download JPEG image".
     * 
     * @param downloadJPEG downloadJPEG
     * @return self
     */
    public Lang downloadJPEG(String downloadJPEG) {
        this.downloadJPEG = downloadJPEG;
        return this;
    }

    /**
     * Exporting module only. The text for the PDF download menu item. Defaults to "Download PDF document".
     * 
     * @param downloadPDF
     * @return self
     */
    public Lang downloadPDF(String downloadPDF) {
        this.downloadPDF = downloadPDF;
        return this;
    }

    /**
     * Exporting module only. The text for the PNG download menu item. Defaults to "Download PNG image".
     * 
     * @param downloadPNG
     * @return self
     */
    public Lang downloadPNG(String downloadPNG) {
        this.downloadPNG = downloadPNG;
        return this;
    }

    /**
     * Exporting module only. The text for the SVG download menu item. Defaults to "Download SVG vector image".
     * 
     * @param downloadSVG
     * @return self
     */
    public Lang downloadSVG(String downloadSVG) {
        this.downloadSVG = downloadSVG;
        return this;
    }

    /**
     * Exporting module only. The tooltip text for the export button. Defaults to "Export to raster or vector image".
     * 
     * @param exportButtonTitle
     * @return self
     */
    public Lang exportButtonTitle(String exportButtonTitle) {
        this.exportButtonTitle = exportButtonTitle;
        return this;
    }

    /**
     * The loading text that appears when the chart is set into the loading state following a call to chart.showLoading. Defaults to Loading....
     * 
     * @param loading
     * @return self
     */
    public Lang loading(String loading) {
        this.loading = loading;
        return this;
    }

    /**
     * Months names. Defaults to ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 
     * 'October', 'November', 'December']
     * 
     * @param january january
     * @param february february
     * @param march march
     * @param april april
     * @param may may
     * @param june june
     * @param july july
     * @param august august
     * @param september september
     * @param october october
     * @param november november
     * @param december december
     * @return self
     */
    public Lang months(String january, String february, String march, String april, String may, String june, 
            String july, String august, String september, String october, String november, String december) {
        this.months = new JSArray<Lang, String>(this, january, february, march, april, may, june, 
                july, august, september, october, november, december);
        return this;
    }

    /**
     * <a href="http://en.wikipedia.org/wiki/Metric_prefix">Metric prefixes</a> used to shorten high numbers in axis labels. 
     * Replacing any of the positions with null causes the full number to be written. Defaults to ['k', 'M', 'G', 'T', 'P', 'E'].
     * 
     * @param kilo kilo
     * @param mega mega
     * @param giga giga
     * @param tera tera
     * @param peta peta
     * @param exa exa
     * @return self
     */
    public Lang numericSymbols(String kilo, String mega, String giga, String tera, String peta, String exa) {
        this.numericSymbols = new JSArray<Lang, String>(this, kilo, mega, giga, tera, peta, exa);
        return this;
    }

    /**
     * Exporting module only. The tooltip text for the print button. Defaults to "Print the chart".
     * 
     * @param printButtonTitle printButtonTitle
     * @return self
     */
    public Lang printButtonTitle(String printButtonTitle) {
        this.printButtonTitle = printButtonTitle;
        return this;
    }

    /**
     * The text for the label for the "from" input box in the range selector. Defaults to 'From'.
     * 
     * @param rangeSelectorFrom rangeSelectorFrom
     * @return self
     */
    public Lang rangeSelectorFrom(String rangeSelectorFrom) {
        this.rangeSelectorFrom = rangeSelectorFrom;
        return this;
    }

    /**
     * The text for the label for the "to" input box in the range selector. Defaults to 'To'.
     * 
     * @param rangeSelectorTo rangeSelectorTo
     * @return self
     */
    public Lang rangeSelectorTo(String rangeSelectorTo) {
        this.rangeSelectorTo = rangeSelectorTo;
        return this;
    }

    /**
     * The text for the label for the range selector buttons. Defaults to 'Zoom'.
     * 
     * @param rangeSelectorZoom rangeSelectorZoom
     * @return self
     */
    public Lang rangeSelectorZoom(String rangeSelectorZoom) {
        this.rangeSelectorZoom = rangeSelectorZoom;
        return this;
    }

    /**
     * The text for the label appearing when a chart is zoomed. Defaults to 'Reset zoom'.
     * 
     * @param resetZoom resetZoom
     * @return self
     */
    public Lang resetZoom(String resetZoom) {
        this.resetZoom = resetZoom;
        return this;
    }

    /**
     * The tooltip title for the label appearing when a chart is zoomed. Defaults to 'Reset zoom level 1:1'.
     * 
     * @param resetZoomTitle resetZoomTitle
     * @return self
     */
    public Lang resetZoomTitle(String resetZoomTitle) {
        this.resetZoomTitle = resetZoomTitle;
        return this;
    }

    /**
     * The default thousands separator used in the Highcharts.numberFormat method unless otherwise specified in the 
     * function arguments. Defaults to "",.
     * 
     * @param thousandsSep thousandsSep
     * @return self
     */
    public Lang thousandsSep(String thousandsSep) {
        this.thousandsSep = thousandsSep;
        return this;
    }

    /**
     * Weekday names. Defaults to ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'].
     * 
     * @param sunday sunday
     * @param monday monday
     * @param tuesday tuesday
     * @param wednesday wednesday
     * @param thursday thursday
     * @param friday friday
     * @param saturday saturday
     * @return self
     */
    public Lang weekdays(String sunday, String monday, String tuesday, String wednesday, String thursday, String friday,
            String saturday) {
        this.weekdays = new JSArray<Lang, String>(this, sunday, monday, tuesday, wednesday, thursday, friday, saturday);
        return this;
    }
}
