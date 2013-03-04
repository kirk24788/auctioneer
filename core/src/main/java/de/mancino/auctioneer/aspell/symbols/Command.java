package de.mancino.auctioneer.aspell.symbols;

import de.mancino.auctioneer.aspell.token.TokenType;
import de.mancino.auctioneer.exceptions.ASpellParserException;
import de.mancino.auctioneer.exceptions.ASpellRuntimeError;

public class Command extends Symbol {
    private Symbol command;
    Command(Symbol parent) throws ASpellParserException {
        super(parent);
    }

    @Override
    protected void parse() throws ASpellParserException {
        final String commandString = expect(TokenType.COMMAND);
        switch(commandString.toUpperCase()) {
        case "LIST":
            command = expect(ListCommand.class);
            break;
        case "CREATE":
            command = expect(CreateCommand.class);
            break;
        case "DELETE":
            command = expect(DeleteCommand.class);
            break;
        default:
            throw new IllegalStateException("Unsupported Command: " + commandString);
        }
    }

    @Override
    public String execute() throws ASpellRuntimeError {
        return command.execute();
    }
}
