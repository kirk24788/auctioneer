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
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.mancino.auctioneer.bo.BargainBO;
import de.mancino.auctioneer.bo.PriceWatchBO;
import de.mancino.auctioneer.dto.Bargain;
import de.mancino.auctioneer.dto.PriceSample;
import de.mancino.auctioneer.dto.components.Color;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.exceptions.PriceWatchDoesnNotExistException;
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
public class BargainDetailsPage extends BasePage implements IHeaderContributor {

    private static final Color CHART_BACKGROUND_COLOR = new Color("FFFFFF");
    private static final Color MIN_MATERIAL_COST_SERIES_COLOR = new Color("AA4643");
    private static final Color MEDIAN_MATERIAL_COST_SERIES_COLOR = new Color("4572A7");
    private static final Color MATERIAL_SAMPLE_SIZE_SERIES_COLOR = new Color("89A54E");
    private static final RangeSelectorButton RANGESELECTOR_24H = new RangeSelectorButton().type("hour").count(24).text("24h");
    private static final RangeSelectorButton RANGESELECTOR_1W = new RangeSelectorButton().type("day").count(7).text("1w");
    private static final RangeSelectorButton RANGESELECTOR_ALL = new RangeSelectorButton().type("all").count(1).text("Alle");

    private final Bargain bargain;
    /**
     * Price Watch BO
     */
    @SpringBean
    private PriceWatchBO priceWatchBO;

    @SpringBean
    private BargainBO bargainBO;

    public BargainDetailsPage(final Bargain bargain) {
        this.bargain = bargain;
        add(new Label("itemName", bargain.getItem().getItemName().toString()));
    }

    public BargainDetailsPage(PageParameters parameters) throws Exception {
        final int bargainID = parameters.getInt("bargainID");
        bargain = bargainBO.getById(bargainID);
        add(new Label("itemName", bargain.getItem().getItemName().toString()));
        setPageTitle("Bargain - " + bargain.getItem().getItemName().toString());


        add(new Label("minEqualized", goldToString( bargain.getEqualizedMinimum() )));
        add(new Label("medianEqualized", goldToString( bargain.getEqualizedMedian() )));

        PageParameters params = new PageParameters();
        params.add( "itemID",  String.valueOf(bargain.getItem().getArmoryId().toInt()) );
        final Link<String> productLine = new BookmarkablePageLink<String>(
                "productLine", PriceWatchGraphPage.class, params );
        productLine.add(new WowheadTooltip(bargain.getItem().getArmoryId()));
        productLine.add(ArmoryImage.createArmoryImage((AuctioneerWebApp) getApplication(),
                "productIcon", bargain.getItem()));
        final String name = bargain.getItemCount() + " x " + bargain.getItem().getItemName();
        productLine.add(new Label("productName",name));
        add(productLine);


        add(new Label("medianProfit", goldToString(bargain.getTotalProfit())));
        add(new Label("minProfit", goldToString(bargain.getTotalSafeProfit())));
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
        try {
            addMarginSeriesData(createCraftingStockchart()).render(response);
        } catch (PriceWatchDoesnNotExistException e) {
        }
    }


    private Highstock createCraftingStockchart() {
        Highstock hs = new Highstock();
        hs.chart("bargainChart").backgroundColor(CHART_BACKGROUND_COLOR);
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


    private Highstock addMarginSeriesData(final Highstock hs) throws PriceWatchDoesnNotExistException {
        final List<PriceSample> samples = priceWatchBO.findByArmoryId(bargain.getItem().getArmoryId()).getPriceSamples();
        // Fill Data
        final List<Data> medianCost = new ArrayList<Data>();
        final List<Data> minCost = new ArrayList<Data>();
        final List<Data> materialSampleSize = new ArrayList<Data>();

        for(PriceSample sample : samples) {
            medianCost.add(new Data(sample.getTimeInMilliseconds(), sample.getMedianPrice().toLong()/10000.0));
            minCost.add(new Data(sample.getTimeInMilliseconds(), sample.getMinimumPrice().toLong()/10000.0));
            materialSampleSize.add(new Data(sample.getTimeInMilliseconds(), sample.getSampleSize()));
        }
        hs.series().add(new Series(minCost).color(MIN_MATERIAL_COST_SERIES_COLOR)
        .name("Minimum")
        .index(1)
        .type("area")
        .shadow(true));
        hs.series().add(new Series(medianCost).color(MEDIAN_MATERIAL_COST_SERIES_COLOR)
        .name("Median")
        .index(0)
        .type("area")
        .shadow(true));
        hs.series().add(new Series(materialSampleSize).color(MATERIAL_SAMPLE_SIZE_SERIES_COLOR)
        .name("Samples")
        .index(4)
        .type("area")
        .showInLegend(false)
        .shadow(true)
        .yAxis(1));

        return hs;
    }

}
