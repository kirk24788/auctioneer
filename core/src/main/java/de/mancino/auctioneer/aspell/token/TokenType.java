package de.mancino.auctioneer.aspell.token;

public enum TokenType {
    // TOKEN TYPES CANNOT HAVE UNDERSCORES!!!
    NUMBER("[0-9]+"),
    //NUMBER("-?[0-9]+"),
    MULTIPLICATOR("[*]"),
    SEPERATOR(","),
    ENDOFCOMMAND(";"),
    COMMAND(ciRegex("CREATE|LIST|DELETE")),
    CATEGORY(ciRegex("STRATEGY|FARMING|SERVERSTATUS|CHARACTERS")),
    KEYWORD(ciRegex("FROM|ADD|ICON|YIELDS")),
    STRING("\"[^\"]*\""),
    WHITESPACE("[ \t\f\r\n]+"),
    INVALID("[^\\s]+");

    public final String pattern;

    private TokenType(String pattern) {
        this.pattern = pattern;
    }

    private static final String ciRegex(final String str) {
        return ciRegex(str, false);
    }
    private static final String ciRegex(final String str, final boolean extended) {
        final String upper = str.toUpperCase();
        final String lower = str.toLowerCase();
        StringBuffer sb = new StringBuffer();
        for(int idx=0;idx<str.length();idx++) {
            final char orig = str.charAt(idx);
            if(orig>='a' && orig<='z' || orig>='A' && orig<='Z'
                    || extended && orig>='à' && orig<='ü' || extended && orig>='À' && orig<='Ü') {
                sb.append("[").append(lower.charAt(idx)).append(upper.charAt(idx)).append("]");

            } else {
                sb.append(orig);
            }
        }
        return sb.toString();
    }
}
