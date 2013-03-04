package de.mancino.auctioneer.exceptions;


public class UnexpectedEndException extends ASpellParserException {
    private static final long serialVersionUID = 1L;

    public UnexpectedEndException() {
        super("Unexpected end of Command");
    }
}
