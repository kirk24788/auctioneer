package de.mancino.auctioneer.webapp;

import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.target.coding.MixedParamUrlCodingStrategy;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class AuctioneerWebApp extends AuthenticatedWebApplication {
    @Override
    protected void init() {
        super.init();
        // Web-Applikation wird in init()-Methode konfiguriert, z.B.
        //  getApplicationSettings().setDefaultMaximumUploadSize(Bytes.megabytes(2));
        addComponentInstantiationListener(new SpringComponentInjector(this));
        // getResourceSettings().addResourceFolder("");
        getMarkupSettings().setStripWicketTags(true);
        getMarkupSettings().setAutomaticLinking(true);
        getMarkupSettings().setDefaultBeforeDisabledLink("<span class=\"disabled-link\">");
        getMarkupSettings().setDefaultAfterDisabledLink("</span>");
        getResourceSettings().setAddLastModifiedTimeToResourceReferenceUrl(true);
        mountBookmarkablePage("/priceWatch", PriceWatchPage.class);
    //    mountBookmarkablePage("/priceWatchGraph", PriceWatchGraphPage.class);
        mount(new MixedParamUrlCodingStrategy("/priceGraph", PriceWatchGraphPage.class,
                new String[]{"itemID"}));
        mountBookmarkablePage("/items", ItemPage.class);
        mountBookmarkablePage("/bargains", BargainPage.class);
        mountBookmarkablePage("/cashlog", CashLogPage.class);
        mountBookmarkablePage("/manage", ManagePage.class);
        mountBookmarkablePage("/login", LoginPage.class);
        mountBookmarkablePage("/errorLog", ErrorLogPage.class);
        mountBookmarkablePage("/sessionExpired", SessionExpiredPage.class);
        mountBookmarkablePage("/crafting", CraftingPage.class);
        mountBookmarkablePage("/adminConsole", AdminConsolePage.class);
        /*
        mount(new MixedParamUrlCodingStrategy("/bargains", BargainDetailsPage.class,
                new String[]{"bargainID"}));
                */
        mount(new MixedParamUrlCodingStrategy("/errorEvent", ErrorStacktracePage.class,
                new String[]{"exceptionId"}));
        mount(new MixedParamUrlCodingStrategy("/craftingGraph", CraftingGraphPage.class,
                new String[]{"saleStrategyID"}));
        mountBookmarkablePage("/farming", FarmingPage.class);
        mount(new MixedParamUrlCodingStrategy("/farmingGraph", FarmingGraphPage.class,
                new String[]{"farmStrategyID"}));
        getApplicationSettings().setAccessDeniedPage(AccessDeniedPage.class);
        getApplicationSettings().setPageExpiredErrorPage(SessionExpiredPage.class);
    }

    @Override
    public Class<? extends WebPage> getHomePage() {
        return HomePage.class;
    }

    @Override
    protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
        return AuctioneerSession.class;
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return LoginPage.class;
    }

    public ApplicationContext getApplicationContext() {
      return WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    }
}
