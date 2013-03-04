package de.mancino.auctioneer.webapp;

import org.apache.wicket.PageParameters;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.mancino.auctioneer.bo.ErrorLogBO;
import de.mancino.auctioneer.dto.ErrorEvent;
import de.mancino.auctioneer.exceptions.ErrorEventDoesnNotExistException;

@AuthorizeInstantiation(Roles.ADMIN)
public class ErrorStacktracePage extends BasePage {
    @SpringBean
    private ErrorLogBO errorLogBO;

    public ErrorStacktracePage(PageParameters parameters) throws ErrorEventDoesnNotExistException {
        final int errorEventId = parameters.getInt("exceptionId");
        ErrorEvent errorEvent;
        errorEvent = errorLogBO.getById(errorEventId);
        setPageTitle(errorEvent.getThrowableClass().getSimpleName());
        add(new Label("exceptionName",errorEvent.getThrowableClass().getCanonicalName()));
        add(new Label("stackTrace",errorEvent.getStackTrace()));
    }
}
