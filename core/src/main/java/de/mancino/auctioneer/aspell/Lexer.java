package de.mancino.auctioneer.aspell;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.mancino.auctioneer.aspell.location.Location;
import de.mancino.auctioneer.aspell.location.LocationFactory;
import de.mancino.auctioneer.aspell.token.Token;
import de.mancino.auctioneer.aspell.token.TokenType;
import de.mancino.auctioneer.exceptions.ASpellLexerException;


public class Lexer {
    public static ArrayList<Token> lex(final String input) throws ASpellLexerException {
        // The tokens to return
        ArrayList<Token> tokens = new ArrayList<Token>();

        // Location factory for finding line offsets
        LocationFactory lf = new LocationFactory(input);

        // Lexer logic begins here
        StringBuffer tokenPatternsBuffer = new StringBuffer();
        for (TokenType tokenType : TokenType.values()) {
            tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
        }
        Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));

        // Begin matching tokens
        Matcher matcher = tokenPatterns.matcher(input);
        while (matcher.find()) {
            final Location location = lf.locationOf(matcher.toMatchResult().start());
            for (TokenType tokenType : TokenType.values()) {
                if (matcher.group(tokenType.name()) != null) {
                    if(tokenType.equals(TokenType.INVALID)) {
                       throw new ASpellLexerException("Found invalid Token '" + matcher.group(tokenType.name()) + "' at: " + location);
                    } else if (tokenType.equals(TokenType.WHITESPACE)) {
                        continue;
                    } else {
                        tokens.add(new Token(tokenType, matcher.group(tokenType.name()), location));
                    }
                }
            }
        }

        return tokens;
    }
}
