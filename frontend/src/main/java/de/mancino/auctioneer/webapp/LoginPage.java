package de.mancino.auctioneer.webapp;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

public class LoginPage extends BasePage {
    private String username;
    private String password;

    public LoginPage() {
        final Form<LoginPage> loginForm = new Form<LoginPage>("loginForm", new CompoundPropertyModel<LoginPage>(this)) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void onSubmit() {
                if (AuctioneerSession.get().signIn(username, password)) {
                    setResponsePage(HomePage.class);
                }
            }
        };
        loginForm.add(new TextField<String>("username"));
        loginForm.add(new PasswordTextField("password"));
        add(loginForm);
    }
}
