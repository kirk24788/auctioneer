package de.mancino.auctioneer.webapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import de.mancino.auctioneer.bo.ErrorLogBO;
import de.mancino.auctioneer.dto.ErrorEvent;
import de.mancino.auctioneer.webapp.components.StyledPagingNavigator;

@AuthorizeInstantiation(Roles.ADMIN)
public class ErrorLogPage extends BasePage {

    @SpringBean
    private ErrorLogBO errorLogBO;

    private String exceptionFilter = "";

    public ErrorLogPage() {
        clearErrors();
        final LoadableDetachableModel<List<ErrorEvent>> exceptionListModel = new LoadableDetachableModel<List<ErrorEvent>>() {
            private static final long serialVersionUID = 1L;
            @Override
            protected List<ErrorEvent> load() {
                final List<ErrorEvent> exceptionEvents = new ArrayList<ErrorEvent>();
                for(ErrorEvent exceptionEvent : errorLogBO.getAll()) {
                    if(!isFiltered(exceptionEvent)) {
                        exceptionEvents.add(exceptionEvent);
                    }
                }
                return exceptionEvents;
            }
            @Override
            public void detach() {
                super.detach();
            }
        };
        final WebMarkupContainer errorLogContainer = new WebMarkupContainer("errorLogContainer");
        errorLogContainer.setOutputMarkupId(true);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        final PageableListView<ErrorEvent> listView = new PageableListView<ErrorEvent>("exceptionList", exceptionListModel, 10) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<ErrorEvent> item) {
                final ErrorEvent errorEvent = item.getModelObject();

                PageParameters params = new PageParameters();
                params.add( "exceptionId",  String.valueOf(errorEvent.getId()) );
                final Link<String> errorLogLine = new BookmarkablePageLink<String>("errorLogLine", ErrorStacktracePage.class, params );


                item.add(errorLogLine);
                errorLogLine.add(new Label("time",sdf.format(new Date(errorEvent.getTimestamp()))));
                errorLogLine.add(new Label("message",errorEvent.getThrowableClass().getCanonicalName()));
            }
        };
        listView.setReuseItems(true);
        listView.setOutputMarkupId(true);
        errorLogContainer.add(listView);
        errorLogContainer.add(new StyledPagingNavigator("exceptionPager", listView));


        final TextField<String> itemFilter = new TextField<String>("exceptionFilter", new PropertyModel<String>(this, "exceptionFilter") );
        OnChangeAjaxBehavior filterBehavior = new OnChangeAjaxBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.addComponent(errorLogContainer);
                listView.removeAll();
                listView.setCurrentPage(0);
            }
        };
        itemFilter.add(filterBehavior);
        add(itemFilter);

        add(errorLogContainer);
    }

    protected boolean isFiltered(final ErrorEvent errorEvent) {
        if(StringUtils.isEmpty(exceptionFilter)) {
            return false;
        }

        return !errorEvent.getStackTrace().toLowerCase().contains(exceptionFilter.toLowerCase());
    }
}
