package de.mancino.auctioneer.webapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.mancino.auctioneer.bo.ArmoryCharacterBO;
import de.mancino.auctioneer.dto.ArmoryCharacter;
import de.mancino.auctioneer.dto.CashSample;
import de.mancino.auctioneer.dto.components.Color;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.webapp.components.highstock.Data;
import de.mancino.auctioneer.webapp.components.highstock.Highstock;
import de.mancino.auctioneer.webapp.components.highstock.Lang;
import de.mancino.auctioneer.webapp.components.highstock.RangeSelectorButton;
import de.mancino.auctioneer.webapp.components.highstock.Series;
import de.mancino.auctioneer.webapp.components.highstock.XAxis;
import de.mancino.auctioneer.webapp.components.highstock.YAxis;

@AuthorizeInstantiation(Roles.ADMIN)
public class CashLogPage extends BasePage implements IHeaderContributor {

    @SpringBean
    private ArmoryCharacterBO armoryCharacterBO;

    private static final Color CHART_BACKGROUND_COLOR = new Color("FFFFFF");
    private static final Color GOLD_TOTAL_SERIES_COLOR = new Color("AA4643");
    private static final Color GOLD_BOT_TOTAL_SERIES_COLOR = new Color("4572A7");
    private static final Color GOLD_NORMAL_TOTAL_SERIES_COLOR = new Color("89A54E");
    private static final Color LEGEND_BACKGROUND_COLOR = new Color("efefef");
    private static final RangeSelectorButton RANGESELECTOR_24H = new RangeSelectorButton().type("hour").count(24).text("24h");
    private static final RangeSelectorButton RANGESELECTOR_1W = new RangeSelectorButton().type("day").count(7).text("1w");
    private static final RangeSelectorButton RANGESELECTOR_ALL = new RangeSelectorButton().type("all").count(1).text("Alle");

    private static final String TABSWITCH_JS = "$(document).ready(function() {\n" +
            "    $(\".segmented-tab-content\").css('display', 'none');\n" +
            "    $(\".segmented-tab-content:first\").fadeIn();\n" +
            "    $(\"label\").click(function() {\n" +
            "        $(\".segmented-tab-content\").css('display', 'none');\n" +
            "        var activeTab = $(this).attr(\"rel\");\n" +
            "        $(\"#\"+activeTab).fadeIn();\n" +
            "    });\n" +
            "});";

    @Override
    public void renderHead(IHeaderResponse response) {
        Highstock.lang(Lang.GERMAN);
        Highstock.global().useUTC(false);
        addCashLogSeriesData(createCashLogStockchart()).render(response);
        addProfitSeriesData(createProfitStockchart()).render(response);
        response.renderJavascript(TABSWITCH_JS, "tabswitch_js");
    }



    // ***************************
    // ***************************
    // ** CASH STATS
    // ***************************
    // ***************************
    public CashLogPage() {
        LoadableDetachableModel<List<ArmoryCharacter>> armoryCharListModel = new LoadableDetachableModel<List<ArmoryCharacter>>() {
            private static final long serialVersionUID = 1L;

            @Override
            protected List<ArmoryCharacter> load() {
                return armoryCharacterBO.listArmoryCharacters();
            }
        };
        add(new ListView<ArmoryCharacter>("cashList", armoryCharListModel) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void populateItem(ListItem<ArmoryCharacter> item) {
                item.add(new Label("name", item.getModelObject().getCharacterName().toString()));
                final Currency money = item.getModelObject().getCurrentCash();
                final String goldAmount = money.getGold() + " Gold " + money.getSilver() + " Silber";
                item.add(new Label("cash", goldAmount));
            }
        });
        long lastTimestamp = 0;
        for(final ArmoryCharacter character : armoryCharacterBO.listArmoryCharacters()) {
            final List<CashSample> cashSamples = character.getCashSamples();
            if(cashSamples.size() > 0) {
                final long charTimestamp = cashSamples.get(cashSamples.size()-1).getTimeInMilliseconds();
                lastTimestamp = charTimestamp > lastTimestamp ? charTimestamp : lastTimestamp;
            }
        }
        final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy 'um' HH:mm");
        final String date = SDF.format(new Date(lastTimestamp));
        add(new Label("date", date));
        long moneyTotal = 0L;
        long moneyBots = 0L;
        long moneyNormal = 0L;
        for(ArmoryCharacter character : armoryCharListModel.getObject()) {
            moneyTotal += character.getCurrentCash().toLong();
            if(character.isBot()) {
                moneyBots += character.getCurrentCash().toLong();
            } else {
                moneyNormal += character.getCurrentCash().toLong();
            }
        }
        final String goldAmount = (moneyTotal/10000L) + " Gold " + ((moneyTotal%10000L)/100L) + " Silber";
        add(new Label("cashTotal", goldAmount));
        final String botGoldAmount = (moneyBots/10000L) + " Gold " + ((moneyBots%10000L)/100L) + " Silber";
        add(new Label("cashBotTotal", botGoldAmount));
        final String normalGoldAmount = (moneyNormal/10000L) + " Gold " + ((moneyNormal%10000L)/100L) + " Silber";
        add(new Label("cashNormalTotal", normalGoldAmount));
    }



    // ***************************
    // ***************************
    // ** CASH LOG GRAPH
    // ***************************
    // ***************************
    private Highstock createCashLogStockchart() {
        Highstock hs = new Highstock();
        hs.chart("cashlog_graph").backgroundColor(CHART_BACKGROUND_COLOR);
        hs.rangeSelector().buttons(RANGESELECTOR_24H, RANGESELECTOR_1W, RANGESELECTOR_ALL)
        .selected(1)
        .inputEnabled(false);
        hs.navigator().enabled(true).top(370);
        hs.scrollbar().enabled(true);
        hs.credits().enabled(false);
        hs.exporting().enabled(true);
        hs.legend().enabled(true).floating(true).backgroundColor(LEGEND_BACKGROUND_COLOR).verticalAlign("top").y(440);
        hs.xAxis().add(new XAxis().min(60 * 60 * 1000).top(-70));
        hs.yAxis().add(new YAxis().title("Gold pro Char").min(0).height(150).lineWidth(2).top(50))
        .add(new YAxis().title("Gold Total").top(225).height(100).offset(0).lineWidth(2));
        return hs;
    }


    private Highstock addCashLogSeriesData(final Highstock hs) {
        final List<ArmoryCharacter> characters = armoryCharacterBO.listArmoryCharacters();
        final List<List<Data>> goldHistories = new ArrayList<List<Data>>(characters.size());
        final List<Long> lastFoundCashAmounts = new ArrayList<Long>(characters.size());
        final List<Long> timestamps = getUniqueTimestamps(characters);
        // init arrays
        Collections.sort(characters);
        for(int i=0 ; i< characters.size() ; i++) {
            goldHistories.add(new ArrayList<Data>(timestamps.size()));
            lastFoundCashAmounts.add(0L);
        }
        hs.navigator().baseSeries(characters.size());
        // fill history, even out missing values by using last known value
        for(final long timestamp : timestamps) {
            updateLastFoundCashAmounts(characters, lastFoundCashAmounts, timestamp);
            addCashToHistory(goldHistories, lastFoundCashAmounts, timestamp);
        }
        // Add char series
        for(int charPos=0 ; charPos<characters.size() ; charPos++) {
            final ArmoryCharacter character = characters.get(charPos);
            final Series series = new Series(goldHistories.get(charPos))
            .color(character.getColor())
            .name(character.getCharacterName().toString())
            .index(charPos)
            .type("line")
            .shadow(true);

            hs.series().add(series);
        }
        // Total series
        final Series total = new Series(getTotalGold(goldHistories))
        .name("Gesamt")
        .type("area")
        .index(characters.size())
        .shadow(true)
        .showInLegend(false)
        .color(GOLD_TOTAL_SERIES_COLOR)
        .yAxis(1);
        final Series totalBot = new Series(getTotalGold(filterGoldHistories(characters, goldHistories, true)))
        .name("Bots")
        .type("area")
        .index(characters.size() + 1)
        .shadow(true)
        .showInLegend(false)
        .color(GOLD_BOT_TOTAL_SERIES_COLOR)
        .yAxis(1);
        hs.series().add(total);
        hs.series().add(totalBot);
        // return edites Highstock
        return hs;
    }

    private List<List<Data>> filterGoldHistories(final List<ArmoryCharacter> characters , final List<List<Data>> goldHistories,
            final boolean isBot) {
        final List<List<Data>> filteredGoldHistories = new ArrayList<List<Data>>();
        for(int historyPos = 0 ; historyPos < goldHistories.size() ; historyPos++ ) {
            if(characters.get(historyPos).isBot() == isBot) {
                filteredGoldHistories.add(goldHistories.get(historyPos));
            }
        }
        return filteredGoldHistories;
    }

    private List<Data> getTotalGold(final List<List<Data>> goldHistories) {
        final List<Data> goldTotal = new ArrayList<Data>();
        for(int dataPos = 0 ; dataPos < goldHistories.get(0).size() ; dataPos++) {
            final long timestamp = goldHistories.get(0).get(dataPos).x;
            double cashSum = 0.0;
            for(List<Data> goldHistory : goldHistories) {
                cashSum += goldHistory.get(dataPos).y;
            }
            goldTotal.add(new Data(timestamp, cashSum));
        }
        return goldTotal;
    }

    private void addCashToHistory(final List<List<Data>> goldHistories, final List<Long> lastFoundCashAmounts,
            final long timestamp) {
        for(int historyPos = 0 ; historyPos < goldHistories.size() ; historyPos++ ) {
            final List<Data> charHistory = goldHistories.get(historyPos);
            final long cash = lastFoundCashAmounts.get(historyPos);
            charHistory.add(new Data(timestamp, cash));
        }
    }

    private void updateLastFoundCashAmounts(final List<ArmoryCharacter> characters, final List<Long> lastFoundCashAmounts,
            final long timestamp) {
        for(int charPos = 0 ; charPos < characters.size() ; charPos++ ) {
            final ArmoryCharacter character = characters.get(charPos);
            for(final CashSample cashSample : character.getCashSamples()) {
                if(cashSample.getTimeInMilliseconds() == timestamp) {
                    lastFoundCashAmounts.set(charPos, cashSample.getCash().toLong() / 10000L);
                    break;
                }
            }
        }
    }

    private List<Long> getUniqueTimestamps(final List<ArmoryCharacter> characters) {
        final Set<Long> timestamps = new HashSet<Long>();
        for(final ArmoryCharacter character : characters) {
            for(final CashSample cashSample : character.getCashSamples()) {
                timestamps.add(cashSample.getTimeInMilliseconds());
            }
        }
        final List<Long>sortedTimestamps = new ArrayList<Long>(timestamps);
        Collections.sort(sortedTimestamps);
        return sortedTimestamps;
    }




    // ***************************
    // ***************************
    // ** PROFIT GRAPH
    // ***************************
    // ***************************


    private Highstock createProfitStockchart() {
        Highstock hs = new Highstock();
        hs.chart("profit_graph").backgroundColor(CHART_BACKGROUND_COLOR);
        hs.rangeSelector().buttons(RANGESELECTOR_24H, RANGESELECTOR_1W, RANGESELECTOR_ALL)
        .selected(1)
        .inputEnabled(false);
        hs.navigator().enabled(true);
        hs.scrollbar().enabled(true);
        hs.credits().enabled(false);
        hs.legend().enabled(false);
        hs.xAxis().add(new XAxis().min(60 * 60 * 1000));
        hs.yAxis().add(new YAxis().title("Profit/Tag").height(300).lineWidth(2));
        return hs;
    }


    private Highstock addProfitSeriesData(final Highstock hs) {
        final List<ArmoryCharacter> characters = armoryCharacterBO.listArmoryCharacters();
        final List<List<Data>> goldHistories = new ArrayList<List<Data>>(characters.size());
        final List<Long> lastFoundCashAmounts = new ArrayList<Long>(characters.size());
        final List<Long> timestamps = getUniqueTimestamps(characters);
        // init arrays
        Collections.sort(characters);
        for(int i=0 ; i< characters.size() ; i++) {
            goldHistories.add(new ArrayList<Data>(timestamps.size()));
            lastFoundCashAmounts.add(0L);
        }
        // fill history, even out missing values by using last known value
        for(final long timestamp : timestamps) {
            updateLastFoundCashAmounts(characters, lastFoundCashAmounts, timestamp);
            addCashToHistory(goldHistories, lastFoundCashAmounts, timestamp);
        }
        // reduce all Histories to one timestamp per day
        for(int historyPos=0 ; historyPos < goldHistories.size() ; historyPos++) {
            goldHistories.set(historyPos, reduceToOnePerDay(goldHistories.get(historyPos)));
        }
        // Total series
        final Series profit = new Series(calculateProfits(getTotalGold(goldHistories)))
        .name("Gesamt")
        .type("line")
        .index(0)
        .shadow(true)
        .color(GOLD_TOTAL_SERIES_COLOR);
        final Series profitBot = new Series(calculateProfits(getTotalGold(filterGoldHistories(characters, goldHistories, true))))
        .name("Bots")
        .type("line")
        .index(0)
        .shadow(true)
        .color(GOLD_BOT_TOTAL_SERIES_COLOR);

        final Series profitNormal = new Series(calculateProfits(getTotalGold(filterGoldHistories(characters, goldHistories, false))))
        .name("Normale Chars")
        .type("line")
        .index(2)
        .shadow(true)
        .color(GOLD_NORMAL_TOTAL_SERIES_COLOR);
        hs.series().add(profit);
        hs.series().add(profitBot);
        hs.series().add(profitNormal);
        // return edites Highstock
        return hs;
    }

    final List<Data> reduceToOnePerDay(final List<Data> dataList) {
        final List<Data> reduced = new ArrayList<Data>();
        final long DAY_IN_MS = 24L * 60L * 60L * 1000L;
        final long OFFSET = 2L * 60L * 60L * 1000L;
        long lastTimestamp = 0L;
        for(Data data : dataList) {
            if(data.x > lastTimestamp+DAY_IN_MS) {
                lastTimestamp = data.x - (data.x % DAY_IN_MS);
                reduced.add(new Data(lastTimestamp-OFFSET, data.y));
            }
        }
        // Also add last entry...else we would have to wait up to 24h for an update!
        reduced.add(dataList.get(dataList.size()-1));
        return reduced;
    }

    final List<Data> calculateProfits(final List<Data> dataList) {
        final List<Data> profits = new ArrayList<Data>();
        double lastBalance = dataList.get(0).y;
        for(Data data : dataList) {
            profits.add(new Data(data.x, data.y-lastBalance));
            lastBalance = data.y;
        }
        return profits;
    }
}
