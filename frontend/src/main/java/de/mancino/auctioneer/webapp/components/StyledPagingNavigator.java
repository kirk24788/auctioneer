package de.mancino.auctioneer.webapp.components;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;

public class StyledPagingNavigator extends PagingNavigator {
    private static final long serialVersionUID = 3076805970237426489L;

    public StyledPagingNavigator(final String id, IPageable pagable) {
        super(id, pagable);
    }
}