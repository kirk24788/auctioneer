package de.mancino.auctioneer.aspell.location;

public class Location {
    public final int line;
    public final int column;

    public Location(final int line, final int column) {
        this.line = line;
        this.column = column;
    }

    @Override
    public String toString() {
        return line + ":" + column;
    }
}
