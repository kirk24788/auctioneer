package de.mancino.auctioneer.webapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Application;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AbstractAutoCompleteTextRenderer;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.IAutoCompleteRenderer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.mancino.armory.Armory;
import de.mancino.armory.exceptions.RequestException;
import de.mancino.armory.json.wowhead.opensearchresult.ItemSearchResult;
import de.mancino.auctioneer.bo.ArmoryItemBO;
import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.exceptions.ArmoryItemAlreadyExistingException;
import de.mancino.auctioneer.webapp.behaviors.WowheadTooltip;
import de.mancino.auctioneer.webapp.components.ArmoryImage;
import de.mancino.auctioneer.webapp.components.StyledPagingNavigator;

@AuthorizeInstantiation(Roles.ADMIN)
public class ItemPage extends BasePage {
    private Integer addItemId;

    /**
     * Price Watch BO
     */
    @SpringBean
    private ArmoryItemBO armoryItemBO;
    private transient Armory armory;

    private String itemFilter = "";

    public ItemPage() {
        final LoadableDetachableModel<List<ArmoryItem>> priceWatchListModel = new LoadableDetachableModel<List<ArmoryItem>>() {
            private static final long serialVersionUID = 1L;
            @Override
            protected List<ArmoryItem> load() {
                final List<ArmoryItem> armoryItems = new ArrayList<ArmoryItem>();
                for(ArmoryItem armoryItem : armoryItemBO.listArmoryItems()) {
                    if(!isFiltered(armoryItem)) {
                        armoryItems.add(armoryItem);
                    }
                }
                return armoryItems;
            }
            @Override
            public void detach() {
                super.detach();
            }
        };
        final WebMarkupContainer armoryItemContainer = new WebMarkupContainer("armoryItemContainer");
        armoryItemContainer.setOutputMarkupId(true);
        final PageableListView<ArmoryItem> listView = new PageableListView<ArmoryItem>("armoryItemList", priceWatchListModel, 10) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<ArmoryItem> item) {
                final ArmoryItem armoryItem = item.getModelObject();
                final Link<String> priceWatchLine = new Link<String>("armoryItemLine") {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public void onClick() {
                    }
                };
                priceWatchLine.add(new WowheadTooltip(armoryItem.getArmoryId()));
                item.add(priceWatchLine);
                priceWatchLine.add(ArmoryImage.createArmoryImage((AuctioneerWebApp) getApplication(), "icon",armoryItem));
                priceWatchLine.add(new Label("name",armoryItem.getItemName().toString()));
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
                        // XXX: DEL=!=
                    }
                });
            }
        };
        listView.setReuseItems(true);
        listView.setOutputMarkupId(true);
        armoryItemContainer.add(listView);
        armoryItemContainer.add(new StyledPagingNavigator("priceWatchPager", listView));



        armory = (Armory) ((AuctioneerWebApp)Application.get()).getApplicationContext().getBean("armory");
        final CompoundPropertyModel<ItemPage> compoundPropertyModel = new CompoundPropertyModel<ItemPage>(this);

        final Form<ItemPage> armoryItemAddByIdForm = new Form<ItemPage>("armoryItemAddByIdForm", compoundPropertyModel) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void onSubmit() {
                try {
                    if(addItemId != null) {
                        armoryItemBO.createArmoryItem(armory.api.getItem(addItemId));
                        addItemId = null;
                    }
                } catch (ArmoryItemAlreadyExistingException e) {
                    error(getLocalizer().getString("armoryItemAddByIdForm.alreadyExists", this, compoundPropertyModel));
                } catch (RequestException e) {
                    //XXX: FAllback WowHead
                    error(getLocalizer().getString("armoryUnavailable", this));
                }
            }
        };
        //armoryItemAddByIdForm.add(new TextField<String>("addItemId").setRequired(true));

        IAutoCompleteRenderer<ItemSearchResult> myRenderer = new
                AbstractAutoCompleteTextRenderer<ItemSearchResult>() {
            private static final long serialVersionUID = 1L;

            @Override
            protected String getTextValue(ItemSearchResult object) {
                return object.title;
            }

            @Override
            protected CharSequence getOnSelectJavascriptExpression(ItemSearchResult item) {
                return String.valueOf(item.id);
            }
        };
        final AutoCompleteTextField<ItemSearchResult> itemIdTextField =
                new AutoCompleteTextField<ItemSearchResult>("addItemId", new Model<ItemSearchResult>(), myRenderer) {
            private static final long serialVersionUID = 1L;

            @Override
            protected Iterator<ItemSearchResult> getChoices(String input) {
                final List<ItemSearchResult> emptyList = Collections.emptyList();
                if (StringUtils.isEmpty(input)) {
                    return emptyList.iterator();
                }
                try {
                    Integer.parseInt(input);
                    return emptyList.iterator();
                } catch (NumberFormatException nfe) {
                    final List<ItemSearchResult> choices = new ArrayList<ItemSearchResult>(10);
                    try {
                        for (final ItemSearchResult itemSearchResult : armory.wowhead.searchItems(input)) {
                            choices.add(itemSearchResult);
                            if (choices.size() == 10) {
                                break;
                            }
                        }
                        return choices.iterator();
                    } catch (RequestException re) {
                        return emptyList.iterator();
                    }
                }
            }
        };
        itemIdTextField.setRequired(true);
        itemIdTextField.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                addItemId = Integer.valueOf(getComponent().getDefaultModelObject().toString());
            }
        });
        final TextField<String> itemFilter = new TextField<String>("itemFilter", new PropertyModel<String>(this, "itemFilter") );
        OnChangeAjaxBehavior filterBehavior = new OnChangeAjaxBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.addComponent(armoryItemContainer);
                listView.removeAll();
                listView.setCurrentPage(0);
            }
        };
        itemFilter.add(filterBehavior);
        add(itemFilter);
        
        armoryItemAddByIdForm.add(itemIdTextField);

        armoryItemAddByIdForm.add(new SubmitLink("addLink"));
        armoryItemContainer.add(armoryItemAddByIdForm);
        armoryItemContainer.add(new FeedbackPanel("feedback"));
        add(armoryItemContainer);
    }

    protected boolean isFiltered(ArmoryItem item) {
        if(StringUtils.isEmpty(itemFilter)) {
            return false;
        }

        return !item.getItemName().toString().toLowerCase().contains(itemFilter.toLowerCase());
    }
}
