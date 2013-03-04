package de.mancino.auctioneer.aspell.symbols;

import java.util.ArrayList;
import java.util.List;

import de.mancino.auctioneer.aspell.token.Token;
import de.mancino.auctioneer.aspell.token.TokenType;
import de.mancino.auctioneer.bo.ArmoryCharacterBO;
import de.mancino.auctioneer.bo.ArmoryItemBO;
import de.mancino.auctioneer.bo.FarmStrategyBO;
import de.mancino.auctioneer.bo.RealmStatusBO;
import de.mancino.auctioneer.bo.SaleStrategyBO;
import de.mancino.auctioneer.exceptions.ASpellParserException;
import de.mancino.auctioneer.exceptions.ASpellRuntimeError;

public class CommandList extends Symbol {
    private List<Symbol> executableCommands;
    public CommandList(List<Token> tokenList, final ArmoryItemBO armoryItemBO,
            final SaleStrategyBO saleStrategyBO, final RealmStatusBO serverStatusBO,
            final FarmStrategyBO farmStrategyBO, final ArmoryCharacterBO armoryCharacterBO) throws ASpellParserException {
        super(tokenList, armoryItemBO, saleStrategyBO, serverStatusBO, farmStrategyBO, armoryCharacterBO);
    }
    CommandList(Symbol parent) throws ASpellParserException {
        super(parent);
    }

    @Override
    protected void parse() throws ASpellParserException {
        executableCommands = new ArrayList<Symbol>();
        executableCommands.add(expect(Command.class));
        expect(TokenType.ENDOFCOMMAND);
        if(!endOfInput()) {
            executableCommands.addAll(expect(CommandList.class).executableCommands);
        }
    }

    @Override
    public String execute() throws ASpellRuntimeError {
        final StringBuffer sb = new StringBuffer();
        for(final Symbol command : executableCommands) {
            sb.append(command.execute());
        }
        return sb.toString();
    }
}