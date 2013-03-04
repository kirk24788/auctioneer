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
public class RangeSelector extends JSObject implements Serializable {
    @JSIgnore
    private static final long serialVersionUID = Highstock.VERSION;

    private Integer buttonSpacing = 0;
    // XXX: buttonTheme -> CSS etc. ... not yet implemented
    private JSArray<RangeSelector, RangeSelectorButton> buttons;
    private boolean enabled = true;
    // XXX: inputBoxStyle ->CSS ... not yet implemented - won't be implemented as Deprecated as of 1.2.5!
    private String inputDateFormat;
    private String inputEditDateFormat;
    private boolean inputEnabled;
    // XXX: input Position -> Object...not yet implemented
    // XXX: inputStyle ->CSS ... not yet implemented 
    // XXX: labelStyle ->CSS ... not yet implemented
    private Integer selected;

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
     * The space in pixels between the buttons in the range selector. Defaults to 0.
     *
     * @param buttonSpacing buttonSpacing
     * @return self
     */
    public RangeSelector buttonSpacing(Integer buttonSpacing) {
        this.buttonSpacing = buttonSpacing;
        return this;
    }

    /**
     * An array of configuration objects for the buttons.
     *
     * @param buttons buttons
     * @return self
     */
    public RangeSelector buttons(RangeSelectorButton ... buttons) {
        this.buttons = new JSArray<RangeSelector, RangeSelectorButton>(this);
        for(RangeSelectorButton button : buttons) {
            this.buttons.add(button);
        }
        return this;
    }

    /**
     * An array of configuration objects for the buttons.
     *
     * @param buttons buttons
     * @return self
     */
    public JSArray<RangeSelector, RangeSelectorButton> buttons() {
        if(this.buttons == null) {
            this.buttons = new JSArray<RangeSelector, RangeSelectorButton>(this);
        }
        return this.buttons;
    }

    /**
     * Enable or disable the range selector. Defaults to true.
     *
     * @param enabled enabled
     * @return self
     */
    public RangeSelector enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * The date format in the input boxes when not selected for editing. Defaults to %b %e, %Y. Defaults to %b %e %Y,.
     *
     * @param inputDateFormat inpputDateFormat
     * @return self
     */
    public RangeSelector inputDateFormat(String inputDateFormat) {
        this.inputDateFormat = inputDateFormat;
        return this;
    }

    /**
     * The date format in the input boxes when they are selected for editing. This must be a format that is recognized 
     * by JavaScript Date.parse. Defaults to %Y-%m-%d.
     *
     * @param inputEditDateFormat inputEditDateFormat
     * @return self
     */
    public RangeSelector inputEditDateFormat(String inputEditDateFormat) {
        this.inputEditDateFormat = inputEditDateFormat;
        return this;
    }

    /**
     * Enable or disable the date input boxes.
     *
     * @param inputEnabled inputEnabled
     * @return self
     */
    public RangeSelector inputEnabled(boolean inputEnabled) {
        this.inputEnabled = inputEnabled;
        return this;
    }

    /**
     * The index of the button to appear pre-selected. Defaults to undefined.
     *
     * @param selected selected
     * @return self
     */
    public RangeSelector selected(Integer selected) {
        this.selected = selected;
        return this;
    }

}
