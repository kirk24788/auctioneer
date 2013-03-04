package de.mancino.auctioneer.aspell.symbols;

import de.mancino.auctioneer.exceptions.ASpellParserException;
import de.mancino.auctioneer.exceptions.ASpellRuntimeError;

public class ListServerStatusCommand extends Symbol {
    public ListServerStatusCommand(Symbol symbol) throws ASpellParserException {
        super(symbol);
    }

    @Override
    protected void parse() throws ASpellParserException {
        // Category already chomped by ListCommand!
    }

    @Override
    public String execute() throws ASpellRuntimeError {
        StringBuffer sb = new StringBuffer();
        sb.append("SERVER-STATUS: ");
        if(getServerStatusBO().isOnline()) {
            sb.append("ONLINE");
        } else {
            sb.append("OFFLINE");
        }
        sb.append(" (").append(getMinutes(getServerStatusBO().getLastUpdate())).append(" MIN AGO)\n");
        return sb.toString();
    }

    private long getMinutes(final long timestamp) {
        final long diff =System.currentTimeMillis() - timestamp;
        return diff / 1000L / 60L;
    }
}
