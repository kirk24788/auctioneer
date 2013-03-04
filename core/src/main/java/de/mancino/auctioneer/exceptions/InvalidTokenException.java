package de.mancino.auctioneer.exceptions;

import de.mancino.auctioneer.aspell.token.Token;
import de.mancino.auctioneer.aspell.token.TokenType;

public class InvalidTokenException extends ASpellParserException {
    private static final long serialVersionUID = 1L;

    public InvalidTokenException(final Token token, final TokenType expectedType) {
        super("Invalid Token " + token.type.name() + " at " + token.location + " expected " + expectedType.name());
    }

    public InvalidTokenException(final Token token, final String expectedData) {
        super("Invalid Token " + token.type.name() + "(" + token.data + ") at " + token.location
                + " expected " + token.type.name() + "(" + expectedData + ")");
    }
}
