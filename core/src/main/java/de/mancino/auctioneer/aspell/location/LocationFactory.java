package de.mancino.auctioneer.aspell.location;

import java.util.ArrayList;
import java.util.List;

public class LocationFactory {
    private final List<Integer> lineStartOffsets = new ArrayList<Integer>();

    public LocationFactory(final String input) {
        // Find line breaks
        lineStartOffsets.add(0);
        for(int offset = 0; offset < input.length(); offset++) {
            char c = input.charAt(offset);
            if(c != '\n')
                continue;

            lineStartOffsets.add(offset+1);
        }
        lineStartOffsets.add(input.length());
    }


    public Location locationOf(int offset) {
        for(int lineNumber = 0; lineNumber < lineStartOffsets.size(); lineNumber++) {
            int curr = lineStartOffsets.get(lineNumber);
            if(curr == offset) {
                return new Location(lineNumber, 0);
            } else if(curr > offset) {
                int col0 = lineStartOffsets.get(lineNumber-1);
                return new Location(lineNumber-1, offset - col0);
            }
        }

        throw new IndexOutOfBoundsException(offset +
                " is not in range of lineStartOffsets [0-"+lineStartOffsets.get(lineStartOffsets.size()-1)+"]" );
    }
}
