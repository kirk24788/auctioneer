package de.mancino.auctioneer.webapp;

import static de.mancino.auctioneer.dto.components.Currency.currency;
import static de.mancino.auctioneer.dto.components.ItemName.itemName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.mancino.auctioneer.bo.ArmoryItemBO;
import de.mancino.auctioneer.bo.PurchaseOrderBO;
import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.PurchaseOrder;
import de.mancino.auctioneer.exceptions.ArmoryItemDoesNotExistException;
import de.mancino.auctioneer.strategies.BuyoutStrategy;
import de.mancino.auctioneer.webapp.behaviors.ErrorFeedbackBorderBehavior;
import de.mancino.auctioneer.webapp.behaviors.WowheadTooltip;
import de.mancino.auctioneer.webapp.components.ArmoryImage;
import de.mancino.auctioneer.webapp.components.StyledPagingNavigator;

@AuthorizeInstantiation(Roles.USER)
public class PurchaseOrderPage extends BasePage {

    /**
     * Price Watch BO
     */
    @SpringBean
    private PurchaseOrderBO purchaseOrderBO;

    @SpringBean
    private ArmoryItemBO armoryItemBO;

    private String purchaseItemName = "";
    private int purchaseItemCount = 1;
    private long purchaseItemPrice = 1;


    public PurchaseOrderPage() {
        final List<ArmoryItem> armoryItems = armoryItemBO.listArmoryItems();
        final LoadableDetachableModel<List<PurchaseOrder>> priceWatchListModel = new LoadableDetachableModel<List<PurchaseOrder>>() {
            private static final long serialVersionUID = 1L;
            @Override
            protected List<PurchaseOrder> load() {
                return purchaseOrderBO.listPurchaseOrders();
            }
            @Override
            public void detach() {
                super.detach();
            }
        };
        PageableListView<PurchaseOrder> listView = new PageableListView<PurchaseOrder>("purchaseOrderList", priceWatchListModel, 10) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<PurchaseOrder> item) {
                final PurchaseOrder purchaseOrder = item.getModelObject();
                final Link<String> purchaseOrderLine = new Link<String>("purchaseOrderLine") {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public void onClick() {
                        //TODO : RESPONSE PAGE ! setResponsePage(new PriceWatchGraphPage(purchaseOrder));
                    }
                };
                purchaseOrderLine.add(new WowheadTooltip(purchaseOrder.getArmoryItem().getArmoryId()));
                item.add(purchaseOrderLine);
                purchaseOrderLine.add(ArmoryImage.createArmoryImage((AuctioneerWebApp) getApplication(),
                        "icon", purchaseOrder.getArmoryItem()));
                final String nameLine = purchaseOrder.getArmoryItem().getItemName().toString() + " (" +
                            purchaseOrder.getItemCount() + " @ " + purchaseOrder.getMaxPrice() + ")";
                purchaseOrderLine.add(new Label("name",nameLine));
                MarkupContainer deleteAction = new MarkupContainer("deleteAction") {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public boolean isVisible() {
                        final Roles roles = AuctioneerSession.get().getRoles();
                        return roles != null && roles.hasRole(Roles.ADMIN);
                    }
                };
                item.add(deleteAction);
                deleteAction.add(new Link<String>("deleteLink") {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        //TODO: CLICK ACTION ACTIVATE/DEACTIVATE priceWatchBO.deletePriceWatch(purchaseORder);
                    }
                });
            }
        };
        add(listView);
        add(new StyledPagingNavigator("purchaseOrderPager", listView));


        final CompoundPropertyModel<ManagePage> compoundPropertyModel = new CompoundPropertyModel<ManagePage>(this);
        final Form<ManagePage> priceWatchAddForm = new Form<ManagePage>("purchaseOrderAddForm", compoundPropertyModel) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void onSubmit() {
                try {
                    //TODO: Create!
                    purchaseOrderBO.createPurchaseOrder(armoryItemBO.findByItemName(itemName(purchaseItemName)),
                            currency(purchaseItemPrice),
                            purchaseItemCount,
                            BuyoutStrategy.DOUBLE_THRESHOLD);
                    purchaseItemName = "";
                    purchaseItemCount = 1;
                    purchaseItemPrice = 1;
                } catch (ArmoryItemDoesNotExistException e) {
                    error(getLocalizer().getString("purchaseOrderAddFormError.doesntExists", this, compoundPropertyModel));
                }
            }
        };
        final AutoCompleteTextField<String> itemNameTextField =
                new AutoCompleteTextField<String>("purchaseItemName", new PropertyModel<String>(this, "purchaseItemName")) {
            private static final long serialVersionUID = 1L;

            @Override
            protected Iterator<String> getChoices(String input) {
                if (StringUtils.isEmpty(input)) {
                    List<String> emptyList = Collections.emptyList();
                    return emptyList.iterator();
                }
                final List<String> choices = new ArrayList<String>(10);

                for (final ArmoryItem armoryItem : armoryItems) {
                    final String itemName = armoryItem.getItemName().toString();

                    if (itemName.toUpperCase().startsWith(input.toUpperCase())) {
                        choices.add(itemName);
                        if (choices.size() == 10) {
                            break;
                        }
                    }
                }

                return choices.iterator();
            }
        };
        itemNameTextField.setRequired(true);
        itemNameTextField.add(new ErrorFeedbackBorderBehavior());
        priceWatchAddForm.add(new TextField<Integer>("purchaseItemCount").setRequired(true));
        priceWatchAddForm.add(new TextField<Long>("purchaseItemPrice").setRequired(true));
        priceWatchAddForm.add(itemNameTextField);
        priceWatchAddForm.add(new SubmitLink("addLink"));
        add(priceWatchAddForm);
    }
}
