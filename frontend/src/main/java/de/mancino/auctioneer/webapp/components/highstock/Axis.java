package de.mancino.auctioneer.webapp.components.highstock;

import static de.mancino.auctioneer.dto.components.Color.color;
import de.mancino.auctioneer.dto.components.Color;

public class Axis {

    
    public static void main(String[] args) {
        Highstock.lang(Lang.GERMAN);
        
        final Highstock hs = new Highstock();
        hs.chart("chart").backgroundColor(color("ffffff"));
        hs.colors().add(color("efefef")).add(color("f1f1f1")).done()
        .credits().href("http://auctioneer.mancino-net.de").enabled(false);
        hs.legend().enabled(false);
        hs.navigator().enabled(false);
        hs.scrollbar().enabled(false);
        hs.rangeSelector().buttons()
        .add(new RangeSelectorButton().type("hour").count(24).text("24h"))
        .add(new RangeSelectorButton().type("day").count(7).text("1w"))
        .add(new RangeSelectorButton().type("all").count(1).text("Alle")).done()
        .selected(1)
        .inputEnabled(false);
        hs.title();
        hs.xAxis().add(new XAxis().minRange(60 * 60 * 1000));
        hs.yAxis().add(new YAxis().title("Gold pro Char").min(0).height(200).lineWidth(2))
        .add(new YAxis().title("Gold Total").top(250).height(100).offset(0).lineWidth(2));
        hs.series().add(new Series().color(new Color("444444")).data(1, 1).data(2, 4).data(3,9));
        System.err.println(hs.toString());
    }

}
