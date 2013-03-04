/**
 * 
 */
package de.mancino.auctioneer.webapp.components.highstock;

import java.io.Serializable;

/**
 *
 * @author mmancino
 */
public class ExportingButtons extends JSObject implements Serializable {
    @JSIgnore
    private static final long serialVersionUID = Highstock.VERSION;

    private Button exportButton;
    private Button printButton;

    /**
     * {@inheritDoc}
     */
    @Override
    protected int identLevel() {
        return 6;
    }

    /**
     * Options for the export button.
     *
     * @return exportButton
     */
    public Button exportButton() {
        if(this.exportButton == null) {
            this.exportButton = new Button();
        }
        return this.exportButton;
    }

    /**
     * Options for the print button.
     *
     * @param printButton printButton
     * @return printButton
     */
    public Button printButton() {
        if(this.printButton == null) {
            this.printButton = new Button();
        }
        return this.printButton;
    }


}
