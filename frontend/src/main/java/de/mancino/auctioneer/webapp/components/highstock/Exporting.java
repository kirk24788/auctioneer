/**
 * 
 */
package de.mancino.auctioneer.webapp.components.highstock;

import java.io.Serializable;

/**
 *
 * @author mmancino
 */
@SuppressWarnings("unused") // Attributes are accesses via reflection!
public class Exporting extends JSObject implements Serializable {
    @JSIgnore
    private static final long serialVersionUID = Highstock.VERSION;

    private ExportingButtons buttons;
    private boolean enabled = true;
    private String filename;
    private String type;
    private String url;
    private Integer width;
    
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
     * Options for the export related buttons, print and export.
     * 
     * @return buttons
     */
    public ExportingButtons buttons() {
        if(buttons == null) {
            this.buttons = new ExportingButtons();
        }
        return buttons;
    }

    /**
     * Whether to enable the exporting module. Defaults to true.
     *
     * @param enabled enabled
     * @return self
     */
    public Exporting enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * The filename, without extension, to use for the exported chart. Defaults to "chart".
     *
     * @param filename filename
     * @return self
     */
    public Exporting filename(String filename) {
        this.filename = filename;
        return this;
    }

    /**
     * Default MIME type for exporting if chart.exportChart() is called without specifying a type option. Possible values are 
     * image/png, image/jpeg, application/pdf and image/svg+xml. Defaults to "image/png".
     *
     * @param type type
     * @return self
     */
    public Exporting type(String type) {
        this.type = type;
        return this;
    }

    /**
     * The URL for the server module converting the SVG string to an image format. By default this points to Highslide 
     * Software's free web service. Defaults to http://export.highcharts.com.
     *
     * @param url url
     * @return self
     */
    public Exporting url(String url) {
        this.url = url;
        return this;
    }

    /**
     * The pixel width of charts exported to PNG or JPG. . Defaults to 800.
     *
     * @param width width
     * @return self
     */
    public Exporting width(Integer width) {
        this.width = width;
        return this;
    }
    
}
