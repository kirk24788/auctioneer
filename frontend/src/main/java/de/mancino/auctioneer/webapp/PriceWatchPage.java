package de.mancino.auctioneer.webapp;

import static de.mancino.auctioneer.dto.components.ItemName.itemName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mancino.auctioneer.bo.ArmoryItemBO;
import de.mancino.auctioneer.bo.PriceWatchBO;
import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.PriceWatch;
import de.mancino.auctioneer.exceptions.ArmoryItemDoesNotExistException;
import de.mancino.auctioneer.exceptions.PriceWatchDoesnNotExistException;
import de.mancino.auctioneer.webapp.behaviors.ErrorFeedbackBorderBehavior;
import de.mancino.auctioneer.webapp.behaviors.WowheadTooltip;
import de.mancino.auctioneer.webapp.components.ArmoryImage;
import de.mancino.auctioneer.webapp.components.StyledPagingNavigator;

@AuthorizeInstantiation(Roles.USER)
public class PriceWatchPage extends BasePage {
    /**
     * Max. Suggestions in List
     */
    private static final int MAX_SUGGEST_CHOICES = 10;
    /**
     * Logger instance of this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(PriceWatchPage.class);
    /**
     * Price Watch BO
     */
    @SpringBean
    private PriceWatchBO priceWatchBO;

    /**
     * Price Watch BO
     */
    @SpringBean
    private ArmoryItemBO armoryItemBO;

    private String addItemName = "";

    private String itemFilter = "";

    public PriceWatchPage() {
        final List<ArmoryItem> armoryItems = armoryItemBO.listArmoryItems(); // XXX: Entfernen, Iterieren nur noch über nicht-Highlights!
        final LoadableDetachableModel<List<PriceWatch>> priceWatchListModel = new LoadableDetachableModel<List<PriceWatch>>() {
            private static final long serialVersionUID = 1L;
            @Override
            protected List<PriceWatch> load() {
                final List<PriceWatch> priceWatches = new ArrayList<PriceWatch>();
                for(PriceWatch priceWatch : priceWatchBO.listAllHighlighted()) {
                    if(!isFiltered(priceWatch)) {
                        priceWatches.add(priceWatch);
                    }
                }
                return priceWatches;
            }
            @Override
            public void detach() {
                super.detach();
            }
        };

        final WebMarkupContainer priceWatchContainer = new WebMarkupContainer("priceWatchContainer");
        final PageableListView<PriceWatch> listView = new PageableListView<PriceWatch>("priceWatchList", priceWatchListModel, 10) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<PriceWatch> item) {
                final PriceWatch priceWatch = item.getModelObject();
                PageParameters params = new PageParameters(); 
                params.add( "itemID",  String.valueOf(priceWatch.getArmoryItem().getArmoryId().toInt()) ); 
                final Link<String> priceWatchLine = new BookmarkablePageLink<String>( 
                        "priceWatchLine", PriceWatchGraphPage.class, params ); 

                priceWatchLine.add(new WowheadTooltip(priceWatch.getArmoryItem().getArmoryId()));
                item.add(priceWatchLine);
                priceWatchLine.add(ArmoryImage.createArmoryImage((AuctioneerWebApp) getApplication(),
                        "icon", priceWatch.getArmoryItem()));
                priceWatchLine.add(new Label("name",priceWatch.getArmoryItem().getItemName().toString()));
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
                        LOG.info("Remove PriceWatch Highlight for {} (Item-ID: {})", priceWatch.getArmoryItem().getItemName(),
                                priceWatch.getArmoryItem().getArmoryId());
                        priceWatchBO.setHighlighted(priceWatch, false);
                    }
                });
            }
        };
        listView.setReuseItems(true);
        listView.setOutputMarkupId(true);

        final CompoundPropertyModel<ManagePage> compoundPropertyModel = new CompoundPropertyModel<ManagePage>(this);
        final Form<ManagePage> priceWatchAddForm = new Form<ManagePage>("priceWatchAddForm", compoundPropertyModel) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void onSubmit() {
                try {
                    final ArmoryItem armoryItem = armoryItemBO.findByItemName(itemName(addItemName));
                    final PriceWatch priceWatch = priceWatchBO.findByArmoryId(armoryItem.getArmoryId());
                    priceWatchBO.setHighlighted(priceWatch, true);
                    LOG.info("Added PriceWatch Highlight for {} (Item-ID: {})", armoryItem.getItemName(), armoryItem.getArmoryId());
                    addItemName = "";
                } catch (ArmoryItemDoesNotExistException e) {
                    error(getLocalizer().getString("priceWatchAddFormError.alreadyExists", this, compoundPropertyModel));
                } catch (PriceWatchDoesnNotExistException e) {
                    // XXX: Richtige Meldung!!!
                    error(getLocalizer().getString("priceWatchAddFormError.alreadyExists", this, compoundPropertyModel));
                }
            }
            @Override
            public boolean isVisible() {
                final Roles roles = AuctioneerSession.get().getRoles();
                return roles != null && roles.hasRole(Roles.ADMIN);
            }
        };
        final AutoCompleteTextField<String> itemNameTextField =
                new AutoCompleteTextField<String>("addItemName", new PropertyModel<String>(this, "addItemName")) {
            private static final long serialVersionUID = 1L;

            @Override
            protected Iterator<String> getChoices(String input) {
                if (StringUtils.isEmpty(input)) {
                    List<String> emptyList = Collections.emptyList();
                    return emptyList.iterator();
                }
                final List<String> choices = new ArrayList<String>(10);

                for (final ArmoryItem armoryItem : armoryItems) {
                    final String itemName = armoryItem.getItemName().toString().toLowerCase();
                    String inputName = input.toLowerCase();
                    boolean startFixed = false;
                    boolean endFixed = false;
                    if(inputName.startsWith("^")) {
                        startFixed = true;
                        inputName = inputName.substring(1);
                    }
                    if(inputName.endsWith("$")) {
                        endFixed = true;
                        inputName = inputName.substring(0,inputName.length()-1);
                    }
                    boolean matches = false;
                    if(startFixed && endFixed) {
                        matches = itemName.equals(inputName);
                    } else if (startFixed) {
                        matches = itemName.startsWith(inputName);
                    } else if (endFixed) {
                        matches = itemName.endsWith(inputName);
                    } else {
                        matches = itemName.contains(inputName);
                    }
                    if ( matches ) {
                        choices.add(itemName);
                        if (choices.size() == MAX_SUGGEST_CHOICES) {
                            break;
                        }
                    }
                }

                return choices.iterator();
            }
        };
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

        itemNameTextField.setRequired(true);
        itemNameTextField.add(new ErrorFeedbackBorderBehavior());
        priceWatchAddForm.add(itemNameTextField);
        priceWatchAddForm.add(new SubmitLink("addLink"));
        priceWatchContainer.add(listView);
        priceWatchContainer.add(new StyledPagingNavigator("priceWatchPager", listView));
        priceWatchContainer.add(priceWatchAddForm);
        priceWatchContainer.setOutputMarkupId(true);
        add(priceWatchContainer);
    }

    protected boolean isFiltered(PriceWatch item) {
        if(StringUtils.isEmpty(itemFilter)) {
            return false;
        }

        return !item.getArmoryItem().getItemName().toString().toLowerCase().contains(itemFilter.toLowerCase());
    }
}
