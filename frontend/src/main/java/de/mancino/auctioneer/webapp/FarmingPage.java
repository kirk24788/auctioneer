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

import de.mancino.auctioneer.bo.FarmStrategyBO;
import de.mancino.auctioneer.dto.FarmStrategy;
import de.mancino.auctioneer.webapp.components.ArmoryImage;
import de.mancino.auctioneer.webapp.components.StyledPagingNavigator;

@AuthorizeInstantiation(Roles.USER)
public class FarmingPage extends BasePage {
    /**
     * Logger instance of this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(FarmingPage.class);

    /**
     * Sale Strategy BO
     */
    @SpringBean
    private FarmStrategyBO farmStrategyBO;

    private String itemFilter = "";

    public FarmingPage() {
        final LoadableDetachableModel<List<FarmStrategy>> farmingListModel = new LoadableDetachableModel<List<FarmStrategy>>() {
            private static final long serialVersionUID = 1L;
            @Override
            protected List<FarmStrategy> load() {
                final List<FarmStrategy> farmStrategies = new ArrayList<FarmStrategy>();
                for(FarmStrategy farmStrategy : farmStrategyBO.getAllOrderedBySafeProfit()) {
                    if(!isFiltered(farmStrategy)) {
                        farmStrategies.add(farmStrategy);
                    }
                }
                return farmStrategies;
            }
            @Override
            public void detach() {
                super.detach();
            }
        };

        final WebMarkupContainer priceWatchContainer = new WebMarkupContainer("farmingContainer");
        final PageableListView<FarmStrategy> listView = new PageableListView<FarmStrategy>("farmingList", farmingListModel, 10) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<FarmStrategy> item) {
                final FarmStrategy farmStrategy = item.getModelObject();
                PageParameters params = new PageParameters();
                params.add( "farmStrategyID",  String.valueOf(farmStrategy.getId().toInt()) );
                final Link<String> priceWatchLine = new BookmarkablePageLink<String>(
                        "farmingLine", FarmingGraphPage.class, params );

                item.add(priceWatchLine);
                priceWatchLine.add(ArmoryImage.createArmoryImage((AuctioneerWebApp) getApplication(),
                        "icon", farmStrategy.getIconItem()));
                priceWatchLine.add(new Label("name",farmStrategy.getStrategyName()));
                MarkupContainer deleteAction = new MarkupContainer("deleteAction") {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public boolean isVisible() {
                        final Roles roles = AuctioneerSession.get().getRoles();
                        return roles != null && roles.hasRole(Roles.ADMIN);
                    }
                };
                item.add(new Label("profit",farmStrategy.getTotalSafeProfit().getGold() + "g"));
                item.add(deleteAction);
                deleteAction.add(new Link<String>("deleteLink") {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        LOG.info("Remove Sale Strategy for: {}", farmStrategy);
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
        priceWatchContainer.add(new StyledPagingNavigator("farmingPager", listView));
        priceWatchContainer.setOutputMarkupId(true);
        add(priceWatchContainer);
    }

    protected boolean isFiltered(FarmStrategy farmStrategy) {
        if(StringUtils.isEmpty(itemFilter)) {
            return false;
        }

        return !farmStrategy.getStrategyName().toLowerCase().contains(itemFilter.toLowerCase());
    }
}
