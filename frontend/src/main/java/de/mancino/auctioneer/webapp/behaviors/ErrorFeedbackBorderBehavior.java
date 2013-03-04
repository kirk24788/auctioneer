package de.mancino.auctioneer.webapp.behaviors;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.ComponentTag;

public class ErrorFeedbackBorderBehavior extends AbstractBehavior {
    private static final long serialVersionUID = 1L;

    @Override
    public void onComponentTag(Component component, ComponentTag tag) {
        super.onComponentTag(component, tag);
        if (component.hasErrorMessage()) {
            tag.append("class", "componentFeedbackError", " ");
            tag.put("title", "ERROR!");
        }
    }
}
