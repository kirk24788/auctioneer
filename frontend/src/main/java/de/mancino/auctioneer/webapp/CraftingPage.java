package de.mancino.auctioneer.webapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.MarkupContainer;
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

import de.mancino.auctioneer.bo.SaleStrategyBO;
import de.mancino.auctioneer.dto.SaleStrategy;
import de.mancino.auctioneer.webapp.behaviors.WowheadTooltip;
import de.mancino.auctioneer.webapp.components.ArmoryImage;
import de.mancino.auctioneer.webapp.components.StyledPagingNavigator;

@AuthorizeInstantiation(Roles.USER)
public class CraftingPage extends BasePage {
    /**
     * Logger instance of this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(CraftingPage.class);

    /**
     * Sale Strategy BO
     */
    @SpringBean
    private SaleStrategyBO saleStrategyBO;

    private String itemFilter = "";

    public CraftingPage() {
        final LoadableDetachableModel<List<SaleStrategy>> craftingListModel = new LoadableDetachableModel<List<SaleStrategy>>() {
            private static final long serialVersionUID = 1L;
            @Override
            protected List<SaleStrategy> load() {
                final List<SaleStrategy> saleStrategies = new ArrayList<SaleStrategy>();
                for(SaleStrategy saleStrategy : saleStrategyBO.getAllOrderedByProfit()) {
                    if(!isFiltered(saleStrategy)) {
                        saleStrategies.add(saleStrategy);
                    }
                }
                return saleStrategies;
            }
            @Override
            public void detach() {
                super.detach();
            }
        };

        final WebMarkupContainer priceWatchContainer = new WebMarkupContainer("craftingContainer");
        final PageableListView<SaleStrategy> listView = new PageableListView<SaleStrategy>("craftingList", craftingListModel, 10) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<SaleStrategy> item) {
                final SaleStrategy saleStrategy = item.getModelObject();
                PageParameters params = new PageParameters();
                params.add( "saleStrategyID",  String.valueOf(saleStrategy.getId().toInt()) );
                final Link<String> priceWatchLine = new BookmarkablePageLink<String>(
                        "craftingLine", CraftingGraphPage.class, params );

                priceWatchLine.add(new WowheadTooltip(saleStrategy.getProduct().getArmoryId()));
                item.add(priceWatchLine);
                priceWatchLine.add(ArmoryImage.createArmoryImage((AuctioneerWebApp) getApplication(),
                        "icon", saleStrategy.getProduct()));
                priceWatchLine.add(new Label("name",saleStrategy.getProduct().getItemName().toString()));
                MarkupContainer deleteAction = new MarkupContainer("deleteAction") {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public boolean isVisible() {
                        final Roles roles = AuctioneerSession.get().getRoles();
                        return roles != null && roles.hasRole(Roles.ADMIN);
                    }
                };
                item.add(new Label("profit",saleStrategy.getProfit().getGold() + "g"));
                item.add(deleteAction);
                deleteAction.add(new Link<String>("deleteLink") {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        LOG.info("Remove Sale Strategy for: {}", saleStrategy);
                        // XXX: TODO
                    }
                });
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
        priceWatchContainer.add(new StyledPagingNavigator("craftingPager", listView));
        priceWatchContainer.setOutputMarkupId(true);
        add(priceWatchContainer);
    }

    protected boolean isFiltered(SaleStrategy saleStrategy) {
        if(StringUtils.isEmpty(itemFilter)) {
            return false;
        }

        return !saleStrategy.getProduct().getItemName().toString().toLowerCase().contains(itemFilter.toLowerCase());
    }
}
