package de.mancino.auctioneer.dto.components;

import org.apache.commons.lang.StringUtils;

public class Color {
    public static final Color DEATH_KNIGHT = new Color("C41F3B");
    public static final Color DRUID = new Color("FF7D0A");
    public static final Color HUNTER = new Color("ABD473");
    public static final Color MAGE = new Color("69CCF0");
    public static final Color MONK = new Color("00FF96");
    public static final Color PALADIN = new Color("F58CBA");
    public static final Color PRIEST = new Color("FFFFFF");
    public static final Color ROGUE = new Color("FFF569");
    public static final Color SHAMAN = new Color("0070DE");
    public static final Color WARLOCK = new Color("9482C9");
    public static final Color WARRIOR = new Color("C79C6E");
    
    
    
    private int red;
    private int green;
    private int blue;
    
    public Color() {
        this(0,0,0);
    }
    
    public Color(int red, int green, int blue) {
        if(!inRange(red) || !inRange(green) || !inRange(blue) ) {
            throw new IllegalStateException("RGB Color must be >= 0 and <= 255! (R:" + red + ";G:" + green + ";B:" + blue + ")");
        }
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    
    public Color(final String hexString) {
        if(!inRange(hexString)) {
            throw new IllegalStateException("HEX-String must have length of 6 and only contain digits 0-9 or characters A-F/a-f! (" + hexString + ")");
        }
        red = Integer.parseInt(hexString.substring(0, 2), 16);
        green = Integer.parseInt(hexString.substring(2, 4), 16);
        blue = Integer.parseInt(hexString.substring(4, 6), 16);
    }

    private boolean inRange(final int color) {
        return color >= 0 && color <= 255;
    }

    private boolean inRange(final String hexString) {
        return hexString.length()==6 && hexString.matches("[0-9A-Fa-f]{6}");
    }
    
    public String toString() {
        return  StringUtils.leftPad(Integer.toHexString(red), 2, '0') +
                StringUtils.leftPad(Integer.toHexString(green), 2, '0') +
                StringUtils.leftPad(Integer.toHexString(blue), 2, '0');
    }

    public static Color color(String hexString) {
        return new Color(hexString);
    }
    public static Color color(int red, int green, int blue) {
        return new Color(red, green, blue);
    }
}
