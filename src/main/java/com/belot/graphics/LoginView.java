package com.belot.graphics;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("rout")
public class LoginView extends VerticalLayout {

	public LoginView() {
		LoginForm component = new LoginForm();
		component.addLoginListener(e -> {
			VaadinSession.getCurrent().setAttribute("name", e.getUsername());
			UI.getCurrent().navigate("game");
		});
		add(component);
	}

}
