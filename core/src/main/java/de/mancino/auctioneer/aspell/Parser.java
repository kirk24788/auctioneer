package de.mancino.auctioneer.aspell;

import java.util.List;

import de.mancino.auctioneer.aspell.symbols.CommandList;
import de.mancino.auctioneer.aspell.token.Token;
import de.mancino.auctioneer.bo.ArmoryCharacterBO;
import de.mancino.auctioneer.bo.ArmoryItemBO;
import de.mancino.auctioneer.bo.FarmStrategyBO;
import de.mancino.auctioneer.bo.RealmStatusBO;
import de.mancino.auctioneer.bo.SaleStrategyBO;
import de.mancino.auctioneer.exceptions.ASpellParserException;

/**
 * Parser
<tt>
<br/>Syntax:<br/>
commandlist ::= [command]';'([commandlist])<br/>
command ::= [list-command]|[create-command]<br/>
list-command ::= 'LIST' [category]<br/>
create-command ::= 'CREATE' [create-strategy-command|create-farming-command]<br/>
create-strategy-command ::= 'STRATEGY' [strategy-data]<br/>
create-farming-command ::= 'FARMING' [string] ICON [item] YIELDS [item-list] ('ADD' [number])
category ::= 'STRATEGY'<br/>
strategy-data ::= [item] 'FROM' [item-list] ('ADD' [number])<br/>
item-list ::= [item]|[item],[item-list]<br/>
item ::= ([multiplicator]) [item_identifier]<br/>
multiplicator ::= [number] '*'<br/>
item_identifier ::= [number]|[string]<br/>
</tt>
 * @author mario
 */
public class Parser {

    public static CommandList parse(final List<Token> tokenList, final ArmoryItemBO armoryItemBO,
            final SaleStrategyBO saleStrategyBO, final RealmStatusBO serverStatusBO, final FarmStrategyBO farmStrategyBO,
            final ArmoryCharacterBO armoryCharacterBO) throws ASpellParserException {
        return new CommandList(tokenList, armoryItemBO, saleStrategyBO, serverStatusBO, farmStrategyBO, armoryCharacterBO);
    }
}
