package de.mancino.auctioneer.webapp;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mancino.auctioneer.aspell.ASpell;
import de.mancino.auctioneer.bo.ArmoryCharacterBO;
import de.mancino.auctioneer.bo.ArmoryItemBO;
import de.mancino.auctioneer.bo.FarmStrategyBO;
import de.mancino.auctioneer.bo.RealmStatusBO;
import de.mancino.auctioneer.bo.SaleStrategyBO;

public class AdminConsolePage extends BasePage {
    private String consoleOutput = "Examples:\n"
            + "LIST STRATEGY;\n"
            + "CREATE STRATEGY \"Frostweave Bag\" FROM 12 * \"Infinite Dust\", 60 * \"Frostweave Cloth\" ADD 15000;\n"
            + "CREATE STRATEGY 5 * 12343 FROM 1234, 5 * 2341;\n"
            + "CREATE FARMING \"Embersilk Cloth @ Uldum\" ICON \"Embersilk Cloth\" YIELDS 60 * \"Embersilk Cloth\" ADD 150000;\n"
            + "CREATE FARMING \"Embersilk Cloth @ Uldum\" ICON 6534 YIELDS 60 * 1245, 10 * 23452, 5 * 10113 ADD 150000;\n";
    private String consoleInput;

    @SpringBean
    private ArmoryItemBO armoryItemBO;
    @SpringBean
    private SaleStrategyBO saleStrategyBO;
    @SpringBean
    private RealmStatusBO serverStatusBO;
    @SpringBean
    private FarmStrategyBO farmStrategyBO;
    @SpringBean
    private ArmoryCharacterBO armoryCharacterBO;

    /**
     * Logger instance of this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AdminConsolePage.class);

    public AdminConsolePage() {
        final CompoundPropertyModel<AdminConsolePage> compoundPropertyModel = new CompoundPropertyModel<AdminConsolePage>(this);


        final Form<AdminConsolePage> consoleForm = new Form<AdminConsolePage>("consoleForm", compoundPropertyModel) {
            private static final long serialVersionUID = 1L;
        };

        final TextArea<String> consoleOutputTextArea = new TextArea<String>("consoleOutput",
                new PropertyModel<String>(this, "consoleOutput"));
        consoleOutputTextArea.setOutputMarkupId(true);
        final TextArea<String> consoleInputTextArea = new TextArea<String>("consoleInput",
                new PropertyModel<String>(this, "consoleInput"));
        consoleInputTextArea.setOutputMarkupId(true);
        final AjaxButton submitButton = new AjaxButton("submitButton") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                target.addComponent(consoleOutputTextArea);
                target.addComponent(consoleInputTextArea);
                try {
                    this.setEnabled(false);
                    LOG.info("Executing: {}", "" + consoleInput);
                    consoleOutput = ASpell.executeCommand("" + consoleInput,
                            armoryItemBO, saleStrategyBO, serverStatusBO, farmStrategyBO, armoryCharacterBO);
                } catch (Exception e) {
                    LOG.error(e.getMessage());
                    consoleOutput = e.getMessage() + "\n" + ExceptionUtils.getStackTrace(e);
                } finally {
                    this.setEnabled(true);
                }
                consoleInput = "";
            }
        };
        submitButton.setOutputMarkupId(true);
        consoleForm.add(consoleOutputTextArea);
        consoleForm.add(consoleInputTextArea);
        consoleForm.add(submitButton);
        add(consoleForm);
    }
}
