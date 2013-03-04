package de.mancino.auctioneer.aspell.symbols;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
import de.mancino.auctioneer.exceptions.InvalidTokenException;
import de.mancino.auctioneer.exceptions.UnexpectedEndException;

public abstract class Symbol {
    protected static final String NO_PREFIX = "";
    protected static final String NO_DATA_MATCH = "";

    private final ArmoryItemBO armoryItemBO;
    private final SaleStrategyBO saleStrategyBO;
    private final RealmStatusBO serverStatusBO;
    private final FarmStrategyBO farmStrategyBO;
    private List<Token> tokenList;
    private final ArmoryCharacterBO armoryCharacterBO;

    Symbol(final List<Token> tokenList,
            final ArmoryItemBO armoryItemBO,
            final SaleStrategyBO saleStrategyBO,
            final RealmStatusBO serverStatusBO,
            final FarmStrategyBO farmStrategyBO,
            final ArmoryCharacterBO armoryCharacterBO) throws ASpellParserException {
        this.tokenList = tokenList;
        this.armoryItemBO = armoryItemBO;
        this.saleStrategyBO = saleStrategyBO;
        this.serverStatusBO = serverStatusBO;
        this.farmStrategyBO = farmStrategyBO;
        this.armoryCharacterBO = armoryCharacterBO;
        parse();

    }

    Symbol(final Symbol parent) throws ASpellParserException {
        this(parent.getTokenList(), parent.getArmoryItemBO(), parent.getSaleStrategyBO(),
                parent.getServerStatusBO(), parent.getFarmStrategyBO(), parent.getArmoryCharacterBO());
    }

    protected ArmoryCharacterBO getArmoryCharacterBO() {
        return armoryCharacterBO;
    }

    protected abstract void parse() throws ASpellParserException;

    protected Token chomp() throws UnexpectedEndException {
        if(endOfInput()) {
            throw new UnexpectedEndException();
        }
        Token chomped = tokenList.get(0);
        tokenList.remove(0);
        return chomped;
    }

    protected Token lookAhead() throws UnexpectedEndException {
        return lookAhead(0);
    }
    protected Token lookAhead(final int offset) throws UnexpectedEndException {
        if(offset < tokenList.size()) {
            return tokenList.get(offset);
        } else {
            throw new UnexpectedEndException();
        }
    }

    protected boolean lookAheadType(final TokenType expected) throws UnexpectedEndException {
        return lookAheadType(expected, 0);
    }
    protected boolean lookAheadType(final TokenType expected, final int offset) throws UnexpectedEndException {
        return lookAhead(offset).type == expected;
    }

    protected boolean lookAheadData(final String expected) throws UnexpectedEndException {
        return lookAheadData(expected, 0);
    }
    protected boolean lookAheadData(final String expected, final int offset) throws UnexpectedEndException {
        return expected.equalsIgnoreCase(lookAhead(offset).data);
    }


    protected boolean endOfInput() {
        return tokenList.size()==0;
    }
    /*
    protected <T extends Symbol> T expect(final String keywordPrefix, final TokenType tokenType, final Class<T> symbolType,
     */

    protected String expect(final TokenType tokenType) throws ASpellParserException {
        return expect(NO_PREFIX, tokenType, NO_DATA_MATCH);
    }
    protected String expect(final TokenType tokenType, final String tokenData) throws ASpellParserException {
        return expect(NO_PREFIX, tokenType, tokenData);
    }
    protected String expect(final String keywordPrefix, final TokenType tokenType, final String tokenData) throws ASpellParserException {
        // check if keyword needs to be prefixed
        if(keywordPrefix != NO_PREFIX) {
            final Token keywordToken = chomp();
            if (keywordToken.type != TokenType.KEYWORD) {
                throw new InvalidTokenException(keywordToken, TokenType.KEYWORD);
            }
            if (!keywordPrefix.equalsIgnoreCase(keywordToken.data)) {
                throw new InvalidTokenException(keywordToken, keywordPrefix);
            }
        }
        // actual token
        final Token nextToken = chomp();
        if(nextToken.type != tokenType) {
            throw new InvalidTokenException(nextToken, tokenType);
        }
        if(tokenData != NO_DATA_MATCH) {
            if (!tokenData.equalsIgnoreCase(nextToken.data)) {
                throw new InvalidTokenException(nextToken, tokenData);
            }
        }
        return nextToken.data;
    }

    protected String optional(final TokenType tokenType) {
        return optional(NO_PREFIX, tokenType, "");
    }
    protected String optional(final TokenType tokenType, final String defaultValue) {
        return optional(NO_PREFIX, tokenType, defaultValue);
    }
    protected String optional(final String keywordPrefix, final TokenType tokenType, final String defaultValue) {
        System.err.println("optional()");
        final List<Token> pushBackList = new ArrayList<>(tokenList);
        try {
            return expect(keywordPrefix, tokenType, NO_DATA_MATCH);
        } catch (ASpellParserException e) {
            tokenList = pushBackList;
            return defaultValue;
        }
    }
    protected boolean optionalMatch(final TokenType tokenType, final String expected) throws UnexpectedEndException {
        if(lookAheadType(tokenType)) {
            if(lookAheadData(expected)) {
                chomp();
                return true;
            }
        }
        return false;
    }

    protected List<Token> getTokenList() {
        return tokenList;
    }
    protected ArmoryItemBO getArmoryItemBO() {
        return armoryItemBO;
    }
    protected SaleStrategyBO getSaleStrategyBO() {
        return saleStrategyBO;
    }
    protected RealmStatusBO getServerStatusBO() {
        return serverStatusBO;
    }
    public FarmStrategyBO getFarmStrategyBO() {
        return farmStrategyBO;
    }

    public String execute() throws ASpellRuntimeError {
        throw new IllegalStateException("This Symbol shouldn't be executed!");
    }


    protected <T extends Symbol> T expect(final Class<T> symbolType) throws ASpellParserException {
        try {
            @SuppressWarnings("rawtypes")
            final Constructor constructor = symbolType.getDeclaredConstructor(Symbol.class);
            @SuppressWarnings("unchecked")
            T symbol = (T) constructor.newInstance(this);
            return symbol;
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException e) {
            throw new IllegalStateException("Reflection should work for all Symbols!", e);
        } catch (InvocationTargetException e) {
            if(e.getCause() instanceof ASpellParserException) {
                throw (ASpellParserException)e.getCause();
            } else {
                throw new IllegalStateException("Reflection should work for all Symbols!", e);
            }
        }
    }

    protected <T extends Symbol> T optional(final Class<T> symbolType) {
        return optional(symbolType, null);
    }
    protected <T extends Symbol> T optional(final Class<T> symbolType, T defaultValue) {
        final List<Token> pushBackList = new ArrayList<>(tokenList);
        try {
            return expect(symbolType);
        } catch (ASpellParserException e) {
            tokenList = pushBackList;
            return defaultValue;
        }
    }

    protected ASpellNumberFinal number(final long number) {
        try {
            return new ASpellNumberFinal(this, number);
        } catch (ASpellParserException e) {
            throw new IllegalStateException("ASpellNumberFinal error!");
        }
    }

    protected ASpellStringFinal string(final String string) {
        try {
            return new ASpellStringFinal(this, string);
        } catch (ASpellParserException e) {
            throw new IllegalStateException("ASpellStringFinal error!");
        }
    }
}
