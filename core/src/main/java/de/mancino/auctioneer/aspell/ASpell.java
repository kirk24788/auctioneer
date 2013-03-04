package de.mancino.auctioneer.aspell;

import de.mancino.auctioneer.aspell.symbols.CommandList;
import de.mancino.auctioneer.bo.ArmoryCharacterBO;
import de.mancino.auctioneer.bo.ArmoryItemBO;
import de.mancino.auctioneer.bo.FarmStrategyBO;
import de.mancino.auctioneer.bo.RealmStatusBO;
import de.mancino.auctioneer.bo.SaleStrategyBO;
import de.mancino.auctioneer.exceptions.ASpellException;

public class ASpell {
    public static String executeCommand(final String input, final ArmoryItemBO armoryItemBO,
            final SaleStrategyBO saleStrategyBO, final RealmStatusBO serverStatusBO,
            final FarmStrategyBO farmStrategyBO, final ArmoryCharacterBO armoryCharacterBO) throws ASpellException {
        CommandList commandList = Parser.parse(Lexer.lex(input),
                armoryItemBO, saleStrategyBO, serverStatusBO, farmStrategyBO, armoryCharacterBO);
        return commandList.execute();
    }
}
