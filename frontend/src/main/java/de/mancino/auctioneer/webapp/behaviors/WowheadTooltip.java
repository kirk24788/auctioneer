package de.mancino.auctioneer.webapp.behaviors;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;

import de.mancino.auctioneer.dto.components.ArmoryId;

public class WowheadTooltip extends AbstractBehavior {
    private static final long serialVersionUID = 1L;

    private ArmoryId armoryId;


    public WowheadTooltip(final ArmoryId armoryId) {
        this.armoryId = armoryId;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavascriptReference("http://static.wowhead.com/widgets/power.js");
    }
/*
    public WowheadTooltip(final ArmoryId armoryId) {
        try {
            if(priceWatch.getAmoryId() == 0) {
                InjectorHolder.getInjector().inject(this);
                final SearchResults searchResults = armory.searchArmory(priceWatch.getItemName(), SelectedTab.ITEMS).searchResults;
                if(searchResults.items != null) {
                    for(Item item : searchResults.items) {
                        if(item.name.equals(priceWatch.getItemName())) {
                            itemId = item.id;
                        }
                    }
                }
                priceWatch.setAmoryId(itemId);
                priceWatchBO.updatePriceWatch(priceWatch);
            } else {
                itemId = priceWatch.getAmoryId();
            }
        } catch (ArmoryConnectionException e) {
            // XXX: Log error?
        }
    }
*/
    @Override
    public void beforeRender(Component component) {
        component.getResponse().write("<a href=\"#\" rel=\"item=" + armoryId.toInt() + "\" style=\"text-decoration:none;\">");
        super.beforeRender(component);
    }

    @Override
    public void onRendered(Component component) {
        super.onRendered(component);
        component.getResponse().write("</a>");
    }
}
