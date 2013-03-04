/**
 * 
 */
package de.mancino.auctioneer.webapp.components.highstock;

import java.io.Serializable;

import de.mancino.auctioneer.dto.components.Color;

/**
 *
 * @author mmancino
 */
@SuppressWarnings("unused") // Attributes are accesses via reflection!
public class Button extends JSObject implements Serializable {
    @JSIgnore
    private static final long serialVersionUID = Highstock.VERSION;

    private String align;
    private Color backgroundColor;
    private Color borderColor;
    private Integer borderRadius;
    private Integer borderWidth;
    private boolean enabled = true;
    private Integer height;
    private Color hoverBorderColor;
    private Color hoverSymbolFill;
    private Color hoverSymbolStroke;
    // XXX: menuItems -> Complex array...not yet supported
    // onClick -> JS code...not yet supported
    private String symbol;
    private Color symbolFill;
    private Integer symbolSize;
    private Color symbolStroke;
    private Integer symbolStrokeWidth;
    private Float symbolX;
    private Float symbolY;
    private String verticalAlign;
    private Integer width;
    private Integer x;
    private Integer y;
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected int identLevel() {
        return 8;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toString(enabled);
    }

    /**
     * Alignment for the buttons. Defaults to "right".
     *
     * @param align align
     * @return self
     */
    public Button align(String align) {
        this.align = align;
        return this;
    }

    /**
     * Background color or gradient for the buttons. Defaults to 
     * linearGradient: [0, 0, 0, 20], stops: [ [0.4, '#F7F7F7'], [0.6, '#E3E3E3'] ] }
     * 
     * @param backgroundColor backgroundColor
     * @return self
     */
    public Button backgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * The border color of the buttons. Defaults to "#B0B0B0".
     *
     * @param borderColor borderColor
     * @return self
     */
    public Button borderColor(Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    /**
     * The border corner radius of the buttons. Defaults to 3.
     *
     * @param borderRadius borderRadius
     * @return self
     */
    public Button borderRadius(Integer borderRadius) {
        this.borderRadius = borderRadius;
        return this;
    }

    /**
     * The border width of the buttons. Defaults to 1.
     *
     * @param borderWidth borderWidth
     * @return self
     */
    public Button borderWidth(Integer borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    /**
     * Whether to enable buttons. Defaults to true.
     *
     * @param enabled enabled
     * @return self
     */
    public Button enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * Pixel height of the buttons. Defaults to 20.
     *
     * @param height height
     * @return self
     */
    public Button height(Integer height) {
        this.height = height;
        return this;
    }

    /**
     * Color of the button border on hover. Defaults to #909090.
     *
     * @param hoverBorderColor hoverBorderColor
     * @return self
     */
    public Button hoverBorderColor(Color hoverBorderColor) {
        this.hoverBorderColor = hoverBorderColor;
        return this;
    }

    /**
     * See navigation.buttonOptions => hoverSymbolFill. Defaults to #768F3E.
     *
     * @param hoverSymbolFill hoverSymbolFill
     * @return self
     */
    public Button hoverSymbolFill(Color hoverSymbolFill) {
        this.hoverSymbolFill = hoverSymbolFill;
        return this;
    }

    /**
     * Stroke (line) color for the symbol within the button on hover. Defaults to #4572A5.
     * 
     * @param hoverSymbolStroke hoverSymbolStroke
     * @return self
     */
    public Button hoverSymbolStroke(Color hoverSymbolStroke) {
        this.hoverSymbolStroke = hoverSymbolStroke;
        return this;
    }

    /**
     * The symbol for the button. Points to a definition function in the Highcharts.Renderer.symbols collection. 
     * The default exportIcon function is part of the exporting module. Defaults to "exportIcon"/"printIcon".
     *
     * @param symbol symbol
     * @return self
     */
    public Button symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    /**
     * See navigation.buttonOptions => symbolFill. Defaults to #A8BF77.
     *
     * @param symbolFill symbolFill
     * @return self
     */
    public Button symbolFill(Color symbolFill) {
        this.symbolFill = symbolFill;
        return this;
    }

    /**
     * The pixel size of the symbol on the button. Defaults to 12.
     *
     * @param symbolSize symbolSize
     * @return self
     */
    public Button symbolSize(Integer symbolSize) {
        this.symbolSize = symbolSize;
        return this;
    }

    /**
     * The color of the symbol's stroke or line. Defaults to "#A0A0A0".
     *
     * @param symbolStroke symbolStroke
     * @return self
     */
    public Button symbolStroke(Color symbolStroke) {
        this.symbolStroke = symbolStroke;
        return this;
    }

    /**
     * The pixel stroke width of the symbol on the button. Defaults to 1.
     *
     * @param symbolStrokeWidth symbolStrokeWidth
     * @return self
     */
    public Button symbolStrokeWidth(Integer symbolStrokeWidth) {
        this.symbolStrokeWidth = symbolStrokeWidth;
        return this;
    }

    /**
     * The x position of the center of the symbol inside the button. Defaults to 11.5.
     *
     * @param symbolX symbolX
     * @return self
     */
    public Button symbolX(Float symbolX) {
        this.symbolX = symbolX;
        return this;
    }

    /**
     * The y position of the center of the symbol inside the button. Defaults to 10.5.
     *
     * @param symbolY symbolY
     * @return self
     */
    public Button symbolY(Float symbolY) {
        this.symbolY = symbolY;
        return this;
    }

    /**
     * The vertical alignment of the buttons. Can be one of "top", "middle" or "bottom". Defaults to "top".
     *
     * @param verticalAlign verticalAlign
     * @return self
     */
    public Button verticalAlign(String verticalAlign) {
        this.verticalAlign = verticalAlign;
        return this;
    }

    /**
     * The pixel width of the button. Defaults to 24.
     *
     * @param width width
     * @return self
     */
    public Button width(Integer width) {
        this.width = width;
        return this;
    }

    /**
     * The horizontal positioin of the button relative to the align option. Defaults to 10.
     *
     * @param x x
     * @return self
     */
    public Button x(Integer x) {
        this.x = x;
        return this;
    }

    /**
     * The vertical offset of the button's position relative to its verticalAlign. Defaults to 10.
     *
     * @param y y
     * @return self
     */
    public Button y(Integer y) {
        this.y = y;
        return this;
    }
    
}
