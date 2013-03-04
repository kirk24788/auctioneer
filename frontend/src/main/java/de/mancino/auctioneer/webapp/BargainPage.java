package de.mancino.auctioneer.webapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mancino.auctioneer.bo.BargainBO;
import de.mancino.auctioneer.dto.Bargain;
import de.mancino.auctioneer.webapp.behaviors.WowheadTooltip;
import de.mancino.auctioneer.webapp.components.ArmoryImage;
import de.mancino.auctioneer.webapp.components.StyledPagingNavigator;

@AuthorizeInstantiation(Roles.USER)
public class BargainPage extends BasePage {
    /**
     * Logger instance of this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(BargainPage.class);

    /**
     * Sale Strategy BO
     */
    @SpringBean
    private BargainBO dealBO;

    private String itemFilter = "";

    public BargainPage() {
        final LoadableDetachableModel<List<Bargain>> dealListModel = new LoadableDetachableModel<List<Bargain>>() {
            private static final long serialVersionUID = 1L;
            @Override
            protected List<Bargain> load() {
                final List<Bargain> saleStrategies = new ArrayList<Bargain>();
                for(Bargain deal : dealBO.getAllOrderedByProfit()) {
                    if(!isFiltered(deal)) {
                        saleStrategies.add(deal);
                    }
                }
                return saleStrategies;
            }
            @Override
            public void detach() {
                super.detach();
            }
        };

        final WebMarkupContainer priceWatchContainer = new WebMarkupContainer("bargainContainer");
        final PageableListView<Bargain> listView = new PageableListView<Bargain>("bargainList", dealListModel, 10) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<Bargain> item) {
                final Bargain bargain = item.getModelObject();
                PageParameters params = new PageParameters();
                params.add( "bargainID",  String.valueOf(bargain.getId()) );
                final Link<String> bargainLine = new BookmarkablePageLink<String>("bargainLine", BargainDetailsPage.class, params );

                bargainLine.add(new WowheadTooltip(bargain.getItem().getArmoryId()));
                item.add(bargainLine);
                bargainLine.add(ArmoryImage.createArmoryImage((AuctioneerWebApp) getApplication(),
                        "icon", bargain.getItem()));
                bargainLine.add(new Label("name",bargain.getItem().getItemName().toString()));
                item.add(new Label("profit",bargain.getTotalProfit().getGold() + "g"));
            }
        };
        listView.setReuseItems(true);
        listView.setOutputMarkupId(true);

        final TextField<String> itemFilter = new TextField<String>("itemFilter", new PropertyModel<String>(this, "itemFilter") );
        OnChangeAjaxBehavior filterBehavior = new OnChangeAjaxBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.addComponent(priceWatchContainer);
                listView.removeAll();
                listView.setCurrentPage(0);
            }
        };
        itemFilter.add(filterBehavior);
        add(itemFilter);

        priceWatchContainer.add(listView);
        priceWatchContainer.add(new StyledPagingNavigator("bargainPager", listView));
        priceWatchContainer.setOutputMarkupId(true);
        add(priceWatchContainer);
    }

    protected boolean isFiltered(Bargain deal) {
        if(StringUtils.isEmpty(itemFilter)) {
            return false;
        }

        return !deal.getItem().getItemName().toString().toLowerCase().contains(itemFilter.toLowerCase());
    }
}
