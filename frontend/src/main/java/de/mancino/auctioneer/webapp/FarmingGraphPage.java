package de.mancino.auctioneer.webapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.mancino.auctioneer.bo.FarmStrategyBO;
import de.mancino.auctioneer.dto.FarmStrategy;
import de.mancino.auctioneer.dto.FarmStrategyLoot;
import de.mancino.auctioneer.dto.FarmStrategyProfit;
import de.mancino.auctioneer.dto.components.Color;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.dto.components.FarmStrategyId;
import de.mancino.auctioneer.webapp.behaviors.WowheadTooltip;
import de.mancino.auctioneer.webapp.components.ArmoryImage;
import de.mancino.auctioneer.webapp.components.highstock.Data;
import de.mancino.auctioneer.webapp.components.highstock.Highstock;
import de.mancino.auctioneer.webapp.components.highstock.Lang;
import de.mancino.auctioneer.webapp.components.highstock.RangeSelectorButton;
import de.mancino.auctioneer.webapp.components.highstock.Series;
import de.mancino.auctioneer.webapp.components.highstock.XAxis;
import de.mancino.auctioneer.webapp.components.highstock.YAxis;

@AuthorizeInstantiation(Roles.USER)
public class FarmingGraphPage extends BasePage implements IHeaderContributor {

    private static final Color CHART_BACKGROUND_COLOR = new Color("FFFFFF");
    private static final Color MIN_MATERIAL_COST_SERIES_COLOR = new Color("AA4643");
    private static final Color MEDIAN_MATERIAL_COST_SERIES_COLOR = new Color("833734");
    private static final Color MIN_SALE_PRICE_SERIES_COLOR = new Color("4572A7");
    private static final Color MATERIAL_SAMPLE_SIZE_SERIES_COLOR = new Color("89A54E");
    private static final Color LEGEND_BACKGROUND_COLOR = new Color("efefef");
    private static final RangeSelectorButton RANGESELECTOR_24H = new RangeSelectorButton().type("hour").count(24).text("24h");
    private static final RangeSelectorButton RANGESELECTOR_1W = new RangeSelectorButton().type("day").count(7).text("1w");
    private static final RangeSelectorButton RANGESELECTOR_ALL = new RangeSelectorButton().type("all").count(1).text("Alle");

    private final FarmStrategy farmStrategy;
    /**
     * Price Watch BO
     */
    @SpringBean
    private FarmStrategyBO farmStrategyBO;

    public FarmingGraphPage(final FarmStrategy farmStrategy) {
        this.farmStrategy = farmStrategy;
        add(new Label("strategyName",farmStrategy.getStrategyName()));
        add(ArmoryImage.createArmoryImage((AuctioneerWebApp) getApplication(), "icon", farmStrategy.getIconItem()));
    }

    public FarmingGraphPage(PageParameters parameters) throws Exception {
        final int farmStrategyID = parameters.getInt("farmStrategyID");
        farmStrategy = farmStrategyBO.getById(FarmStrategyId.farmStrategyId(farmStrategyID));
        add(new Label("strategyName",farmStrategy.getStrategyName()));
        add(ArmoryImage.createArmoryImage((AuctioneerWebApp) getApplication(), "icon", farmStrategy.getIconItem()));
        setPageTitle("Farming - " + farmStrategy.getStrategyName());

        final PropertyModel<List<FarmStrategyLoot>> materialListModel =
                new PropertyModel<List<FarmStrategyLoot>>(this, "farmStrategy.loot");

        ListView<FarmStrategyLoot>  listView = new ListView<FarmStrategyLoot>("lootList", materialListModel) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void populateItem(ListItem<FarmStrategyLoot> item) {

                final FarmStrategyLoot loot = item.getModelObject();
                PageParameters params = new PageParameters();
                params.add( "itemID",  String.valueOf(loot.getItem().getArmoryId().toInt()) );
                final Link<String> lootLine = new BookmarkablePageLink<String>(
                        "lootLine", PriceWatchGraphPage.class, params );

                lootLine.add(new WowheadTooltip(loot.getItem().getArmoryId()));
                lootLine.add(ArmoryImage.createArmoryImage((AuctioneerWebApp) getApplication(),
                        "icon", loot.getItem()));
                final String name = loot.getItemCount() + " x " + loot.getItem().getItemName();
                lootLine.add(new Label("name",name));
                item.add(lootLine);
            }
        };
        listView.setReuseItems(true);
        listView.setOutputMarkupId(true);
        add(listView);

        add(new Label("minProfit", goldToString(farmStrategy.getTotalSafeProfit())));
        add(new Label("medianProfit", goldToString(farmStrategy.getTotalProfit())));
    }

    private String goldToString(final Currency currency) {
        final long gold = currency.getGold();
        final long firstFraction = currency.getSilver() >= 0 ? currency.getSilver()/10 : currency.getSilver()/-10;
        return gold + "," + firstFraction +"g";
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        Highstock.lang(Lang.GERMAN);
        Highstock.global().useUTC(false);
        addMarginSeriesData(createCraftingStockchart()).render(response);
    }


    private Highstock createCraftingStockchart() {
        Highstock hs = new Highstock();
        hs.chart("farmingChart").backgroundColor(CHART_BACKGROUND_COLOR);
        /*
        hs.navigator().enabled(true).top(320);
        hs.scrollbar().enabled(true);
        */
        hs.navigator().enabled(false);
        hs.scrollbar().enabled(false);

        hs.rangeSelector().buttons(RANGESELECTOR_24H, RANGESELECTOR_1W, RANGESELECTOR_ALL)
        .selected(1)
        .inputEnabled(false);
        hs.credits().enabled(false);
        hs.exporting().enabled(true);
        hs.legend().enabled(false);
        hs.xAxis().add(new XAxis().min(60 * 60 * 1000).top(-70));
        hs.yAxis().add(new YAxis().title("Kosten").min(0).height(100).lineWidth(2).top(50))
        .add(new YAxis().title("Samples").top(175).height(50).offset(0).lineWidth(2));

        return hs;
    }


    private Highstock addMarginSeriesData(final Highstock hs) {
        final List<FarmStrategyProfit> profits = farmStrategy.getProfits();
        // Fill Data
        final List<Data> medianProfit = new ArrayList<Data>();
        final List<Data> minProfit = new ArrayList<Data>();
        final List<Data> profitSampleSize = new ArrayList<Data>();

        for(FarmStrategyProfit profit : profits) {
            medianProfit.add(new Data(profit.getProfitTimestamp(), profit.getMedianSalePrice().toLong()/10000.0));
            minProfit.add(new Data(profit.getProfitTimestamp(), profit.getMinSalePrice().toLong()/10000.0));
            profitSampleSize.add(new Data(profit.getProfitTimestamp(), profit.getSalePriceSampleSize()));
        }
        hs.series().add(new Series(medianProfit).color(MIN_SALE_PRICE_SERIES_COLOR)
        .name("Median Profit")
        .index(0)
        .type("area")
        .shadow(true));
        hs.series().add(new Series(minProfit).color(MIN_MATERIAL_COST_SERIES_COLOR)
        .name("Minimaler Profit")
        .index(1)
        .type("area")
        .shadow(true));
        hs.series().add(new Series(profitSampleSize).color(MATERIAL_SAMPLE_SIZE_SERIES_COLOR)
        .name("Samples")
        .index(4)
        .type("area")
        .showInLegend(false)
        .shadow(true)
        .yAxis(1));

        return hs;
    }

}
