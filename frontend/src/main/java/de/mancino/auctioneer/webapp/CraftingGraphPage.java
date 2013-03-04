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

import de.mancino.auctioneer.bo.SaleStrategyBO;
import de.mancino.auctioneer.dto.SaleStrategy;
import de.mancino.auctioneer.dto.SaleStrategyMargin;
import de.mancino.auctioneer.dto.SaleStrategyMaterial;
import de.mancino.auctioneer.dto.components.Color;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.dto.components.SaleStrategyId;
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
public class CraftingGraphPage extends BasePage implements IHeaderContributor {

    private static final Color CHART_BACKGROUND_COLOR = new Color("FFFFFF");
    private static final Color MIN_MATERIAL_COST_SERIES_COLOR = new Color("AA4643");
    private static final Color MEDIAN_MATERIAL_COST_SERIES_COLOR = new Color("833734");
    private static final Color MIN_SALE_PRICE_SERIES_COLOR = new Color("4572A7");
    private static final Color MATERIAL_SAMPLE_SIZE_SERIES_COLOR = new Color("89A54E");
    private static final Color LEGEND_BACKGROUND_COLOR = new Color("efefef");
    private static final RangeSelectorButton RANGESELECTOR_24H = new RangeSelectorButton().type("hour").count(24).text("24h");
    private static final RangeSelectorButton RANGESELECTOR_1W = new RangeSelectorButton().type("day").count(7).text("1w");
    private static final RangeSelectorButton RANGESELECTOR_ALL = new RangeSelectorButton().type("all").count(1).text("Alle");

    private final SaleStrategy saleStrategy;
    /**
     * Price Watch BO
     */
    @SpringBean
    private SaleStrategyBO saleStrategyBO;

    public CraftingGraphPage(final SaleStrategy saleStrategy) {
        this.saleStrategy = saleStrategy;
        add(new Label("itemName", saleStrategy.getProduct().getItemName().toString()));
    }

    public CraftingGraphPage(PageParameters parameters) throws Exception {
        final int saleStrategyID = parameters.getInt("saleStrategyID");
        saleStrategy = saleStrategyBO.getById(SaleStrategyId.saleStrategyId(saleStrategyID));
        add(new Label("itemName",saleStrategy.getProduct().getItemName().toString()));
        setPageTitle("Crafting - " + saleStrategy.getProduct().getItemName().toString());

        final PropertyModel<List<SaleStrategyMaterial>> materialListModel =
                new PropertyModel<List<SaleStrategyMaterial>>(this, "saleStrategy.materials");

        ListView<SaleStrategyMaterial>  listView = new ListView<SaleStrategyMaterial>("materialList", materialListModel) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void populateItem(ListItem<SaleStrategyMaterial> item) {

                final SaleStrategyMaterial material = item.getModelObject();
                PageParameters params = new PageParameters();
                params.add( "itemID",  String.valueOf(material.getItem().getArmoryId().toInt()) );
                final Link<String> materialLine = new BookmarkablePageLink<String>(
                        "materialLine", PriceWatchGraphPage.class, params );

                materialLine.add(new WowheadTooltip(material.getItem().getArmoryId()));
                materialLine.add(ArmoryImage.createArmoryImage((AuctioneerWebApp) getApplication(),
                        "icon", material.getItem()));
                final String name = material.getItemCount() + " x " + material.getItem().getItemName();
                materialLine.add(new Label("name",name));
                item.add(materialLine);
            }
        };
        listView.setReuseItems(true);
        listView.setOutputMarkupId(true);
        add(listView);


        add(new Label("salePrice", goldToString(new Currency(saleStrategy.getMinSalePrice().toLong() * saleStrategy.getProductCount()))));

        add(new Label("minMaterialPrice", goldToString(saleStrategy.getMinMaterialCost())));
        add(new Label("medianMaterialPrice", goldToString(saleStrategy.getMedianMaterialCost())));

        PageParameters params = new PageParameters();
        params.add( "itemID",  String.valueOf(saleStrategy.getProduct().getArmoryId().toInt()) );
        final Link<String> productLine = new BookmarkablePageLink<String>(
                "productLine", PriceWatchGraphPage.class, params );
        productLine.add(new WowheadTooltip(saleStrategy.getProduct().getArmoryId()));
        productLine.add(ArmoryImage.createArmoryImage((AuctioneerWebApp) getApplication(),
                "productIcon", saleStrategy.getProduct()));
        final String name = saleStrategy.getProductCount() + " x " + saleStrategy.getProduct().getItemName();
        productLine.add(new Label("productName",name));
        add(productLine);


        add(new Label("medianProfit", goldToString(saleStrategy.getProfit())));
        add(new Label("minProfit", goldToString(saleStrategy.getSafeProfit())));
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
        hs.chart("craftingChart").backgroundColor(CHART_BACKGROUND_COLOR);
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
        final List<SaleStrategyMargin> margins = saleStrategy.getMargins();
        // Fill Data
        final List<Data> medianMaterialCost = new ArrayList<Data>();
        final List<Data> minMaterialCost = new ArrayList<Data>();
        final List<Data> minSalePrice = new ArrayList<Data>();
        final List<Data> materialSampleSize = new ArrayList<Data>();
        final List<Data> profit = new ArrayList<Data>();

        for(SaleStrategyMargin margin : margins) {
            medianMaterialCost.add(new Data(margin.getMarginTimestamp(), margin.getMedianMaterialCost().toLong()/10000.0));
            minMaterialCost.add(new Data(margin.getMarginTimestamp(), margin.getMinMaterialCost().toLong()/10000.0));
            minSalePrice.add(new Data(margin.getMarginTimestamp(), (saleStrategy.getProductCount() * margin.getMinSalePrice().toLong())/10000.0));
            materialSampleSize.add(new Data(margin.getMarginTimestamp(), margin.getMaterialCostSampleSize()));
            profit.add(new Data(margin.getMarginTimestamp(), margin.getProfit(saleStrategy.getAdditionalExpenses()).toLong()/10000.0));
        }
        hs.series().add(new Series(minSalePrice).color(MIN_SALE_PRICE_SERIES_COLOR)
        .name("Verkaufs Preis")
        .index(0)
        .type("area")
        .shadow(true));
        hs.series().add(new Series(minMaterialCost).color(MIN_MATERIAL_COST_SERIES_COLOR)
        .name("Material Kosten")
        .index(1)
        .type("area")
        .shadow(true));
        hs.series().add(new Series(medianMaterialCost).color(MEDIAN_MATERIAL_COST_SERIES_COLOR)
        .name("Material Kosten (Median)")
        .index(2)
        .type("line")
        .shadow(true));
        hs.series().add(new Series(medianMaterialCost).color(MATERIAL_SAMPLE_SIZE_SERIES_COLOR)
        .name("Samples")
        .index(4)
        .type("area")
        .showInLegend(false)
        .shadow(true)
        .yAxis(1));

        return hs;
    }

}
