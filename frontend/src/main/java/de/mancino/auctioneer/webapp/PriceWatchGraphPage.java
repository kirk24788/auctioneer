package de.mancino.auctioneer.webapp;

import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.mancino.auctioneer.bo.PriceWatchBO;
import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.PriceSample;
import de.mancino.auctioneer.dto.PriceWatch;
import de.mancino.auctioneer.dto.components.ArmoryId;

@AuthorizeInstantiation(Roles.USER)
public class PriceWatchGraphPage extends BasePage implements IHeaderContributor {
    private final PriceWatch priceWatch;
    /**
     * Price Watch BO
     */
    @SpringBean
    private PriceWatchBO priceWatchBO;

    private static final CompressedResourceReference CHART_JS = new CompressedResourceReference(PriceWatchGraphPage.class, "js/priceWatchChart.js");
    private static final CompressedResourceReference JQUERY_JS = new CompressedResourceReference(PriceWatchGraphPage.class, "js/jquery-1.4.2.js");
    private static final CompressedResourceReference HIGHCHART_BASE_JS = new CompressedResourceReference(PriceWatchGraphPage.class, "js/testing-stock-exporting.js");

    public PriceWatchGraphPage(final PriceWatch priceWatch) {
        this.priceWatch = priceWatch;
        add(new Label("itemName", priceWatch.getArmoryItem().getItemName().toString()));
    }

    public PriceWatchGraphPage(PageParameters parameters) throws Exception {
        final int itemID = parameters.getInt("itemID");
        final ArmoryItem armoryItem = priceWatchBO.findByArmoryId(ArmoryId.armoryId(itemID)).getArmoryItem();
        this.priceWatch = priceWatchBO.findByArmoryId(armoryItem.getArmoryId());
        add(new Label("itemName", priceWatch.getArmoryItem().getItemName().toString()));
        setPageTitle("PriceGraph - " + armoryItem.getItemName().toString());
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavascriptReference(JQUERY_JS);
        response.renderJavascript(createJavascriptArray(), "jsArray");
        response.renderJavascriptReference(HIGHCHART_BASE_JS);
        response.renderJavascriptReference(CHART_JS);
    }

    private String createJavascriptArray() {
        final StringBuffer minimumPrices = new StringBuffer();
        final StringBuffer medianPrices = new StringBuffer();
        final StringBuffer averagePrices = new StringBuffer();
        final StringBuffer sampleSize = new StringBuffer();
        minimumPrices.append("var minimumPrices = [");
        medianPrices.append("var medianPrices = [");
        averagePrices.append("var averagePrices = [");
        sampleSize.append("var sampleSizes = [");
        List<PriceSample> priceSamples = priceWatch.getPriceSamples();
        for(PriceSample price : priceSamples) {
            minimumPrices.append("[").append(price.getTimeInMilliseconds()).append(",").append(price.getMinimumPrice().toLong()/10000.0).append("],");
            medianPrices.append("[").append(price.getTimeInMilliseconds()).append(",").append(price.getMedianPrice().toLong()/10000.0).append("],");
            averagePrices.append("[").append(price.getTimeInMilliseconds()).append(",").append(price.getAveragePrice().toLong()/10000.0).append("],");
            sampleSize.append("[").append(price.getTimeInMilliseconds()).append(",").append(price.getSampleSize()).append("],");
        }
        PriceSample price = priceSamples.get(priceSamples.size()-1);
        minimumPrices.append("[").append(price.getTimeInMilliseconds()).append(",").append(price.getMinimumPrice().toLong()/10000.0).append("]");
        medianPrices.append("[").append(price.getTimeInMilliseconds()).append(",").append(price.getMedianPrice().toLong()/10000.0).append("]");
        averagePrices.append("[").append(price.getTimeInMilliseconds()).append(",").append(price.getAveragePrice().toLong()/10000.0).append("]");
        sampleSize.append("[").append(price.getTimeInMilliseconds()).append(",").append(price.getSampleSize()).append("]");
        minimumPrices.append("];");
        medianPrices.append("];");
        averagePrices.append("];");
        sampleSize.append("];");
        return minimumPrices.append(medianPrices).append(averagePrices).append(sampleSize).toString();
    }

}
