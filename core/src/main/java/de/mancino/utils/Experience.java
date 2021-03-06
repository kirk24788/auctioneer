package de.mancino.utils;

public class Experience {
    public enum ConColor {
        GREY,
        GREEN,
        YELLOW,
        ORANGE,
        RED,
        SKULL;
    }
    // Mob XP Functions (including Con Colors)
    // Colors will be numbers:
    //  {grey = 0, green = 1, yellow = 2, orange = 3, red = 4, skull = 5}
    // NOTE: skull = red when working with anything OTHER than mobs!

    public static ConColor getConColor(int playerlvl, int moblvl) {
        if(playerlvl + 5 <= moblvl) {
            if(playerlvl + 10 <= moblvl) {
                return ConColor.SKULL;
            }
            else {
                return ConColor.RED;
            }
        }
        else {
            switch(moblvl - playerlvl)
            {
            case 4:
            case 3:
                return ConColor.ORANGE;
            case 2:
            case 1:
            case 0:
            case -1:
            case -2:
                return ConColor.YELLOW;
            default:
                // More adv formula for grey/green lvls:
                if(playerlvl <= 5) {
                    return ConColor.GREEN; //All others are green.
                }
                else {
                    if(playerlvl <= 39) {
                        if(moblvl <= (playerlvl - 5 - Math.floor(playerlvl/10))) {
                            // Its below or equal to the 'grey level':
                            return ConColor.GREY;
                        }
                        else {
                            return ConColor.GREEN;
                        }
                    }
                    else {
                        //player over lvl 39:
                        if(moblvl <= (playerlvl - 1 - Math.floor(playerlvl/5))) {
                            return ConColor.GREY;
                        }
                        else {
                            return ConColor.GREEN;
                        }
                    }
                }
            }
        }
    }
    public static int getZD(int lvl) {
        if(lvl <= 7) {
            return 5;
        }
        if(lvl <= 9) {
            return 6;
        }
        if(lvl <= 11) {
            return 7;
        }
        if(lvl <= 15) {
            return 8;
        }
        if(lvl <= 19) {
            return 9;
        }
        if(lvl <= 29) {
            return 11;
        }
        if(lvl <= 39) {
            return 12;
        }
        if(lvl <= 44) {
            return 13;
        }
        if(lvl <= 49) {
            return 14;
        }
        if(lvl <= 54) {
            return 15;
        }
        if(lvl <= 59) {
            return 16;
        }
        else {
            return 17; // Approx.
        }
    }

    public static double getMobXP(int playerlvl, int moblvl) {
        if(moblvl >= playerlvl) {
            double temp = ((playerlvl * 5) + 45) * (1 + (0.05 * (moblvl - playerlvl)));
            double tempcap = ((playerlvl * 5) + 45) * 1.2;
            if(temp > tempcap) {
                return Math.floor(tempcap);
            }
            else {
                return Math.floor(temp);
            }
        }
        else {
            if(getConColor(playerlvl, moblvl) == ConColor.GREY) {
                return 0;
            }
            else {
                return Math.floor((playerlvl * 5) + 45) * (1 - (playerlvl -  moblvl)/getZD(playerlvl));
            }
        }
    }

    public static double getEliteMobXP(int playerlvl, int moblvl) {
        return getMobXP(playerlvl, moblvl) * 2;
    }

    // Rested Bonus:
    // Restedness is double XP, but if we only have part restedness we must split the XP:

    public static double getMobXPFull(int playerlvl, int moblvl, boolean elite, int rest) {
        // rest = how much XP is left before restedness wears off:
        double temp = 0;
        if(elite) {
            temp = getEliteMobXP(playerlvl, moblvl);
        }
        else {
            temp = getMobXP(playerlvl, moblvl);
        }
        // Now to apply restedness.  temp = actual XP.
        // If restedness is 0...
        if(rest == 0) {
            return temp;
        }
        else {
            if(rest >= temp) {
                return temp * 2;
            }
            else {
                //Restedness is partially covering the XP gained.
                // XP = rest + (AXP - (rest / 2))
                return rest + (temp - (rest / 2));
            }
        }
    }
    // Party Mob XP:
    public static double getPartyMobXPFull(int playerlvl, int highestlvl, int sumlvls, int moblvl, boolean elite, int  rest) {
        double temp = getMobXPFull(highestlvl, moblvl, elite, 0);
        // temp = XP from soloing via highest lvl...
        temp = temp * playerlvl / sumlvls;
        if(rest == 0) {
            return temp;
        }
        else {
            if(rest >= temp) {
                return temp * 2;
            }
            else {
                //Restedness is partially covering the XP gained.
                // XP = rest + (AXP - (rest / 2))
                return rest + (temp - (rest / 2));
            }
        }
    }
}
