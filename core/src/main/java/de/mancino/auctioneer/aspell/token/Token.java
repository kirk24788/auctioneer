package de.mancino.auctioneer.aspell.token;

import de.mancino.auctioneer.aspell.location.Location;

public class Token {
    public final TokenType type;
    public final String data;
    public final Location location;

    public Token(final TokenType type, final String data, final Location location) {
        this.type = type;
        this.data = data;
        this.location = location;
    }

    @Override
    public String toString() {
        return "(" + type.name() + " " + data + ")@(" + location + ")";
    }
}
