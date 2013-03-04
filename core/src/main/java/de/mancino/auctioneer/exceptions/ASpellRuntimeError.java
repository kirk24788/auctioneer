package de.mancino.auctioneer.exceptions;

/**
 * Base class for all ASpell Exception.
 *
 * @author mario
 */
public class ASpellRuntimeError extends ASpellException {
    private static final long serialVersionUID = 1L;

    public ASpellRuntimeError(final String msg) {
        super(msg);
    }
}
