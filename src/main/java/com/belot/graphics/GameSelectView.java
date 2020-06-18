package com.belot.graphics;

import com.belot.logic.GameManager;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("game")
public class GameSelectView extends VerticalLayout {

	public GameSelectView() {

		if (VaadinSession.getCurrent().getAttribute("name") == null) {
			UI.getCurrent().navigate("");
			UI.getCurrent().getPage().reload();
			return;
		}

		String playerName = (String) VaadinSession.getCurrent().getAttribute("name");
		Select<String> labelSelect = new Select<>();
		labelSelect.setItems(GameManager.getGameNames());
		labelSelect.setLabel("Active games");
		add(labelSelect);

		TextField labelField = new TextField();
		labelField.setLabel("New game");

		add(labelSelect, labelField);

		Button button = new Button("Start");
		button.setAutofocus(true);

		Dialog dialog = new Dialog();
		Button OK = new Button("OK");
		OK.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				dialog.close();

			}
		});
		dialog.add(OK);
		dialog.setCloseOnEsc(true);
		dialog.setCloseOnEsc(true);

		dialog.setWidth("400px");
		dialog.setHeight("150px");

		button.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				// TODO Auto-generated method stub
				if (labelSelect.getValue() != null) {
					if (!GameManager.joinGame(labelSelect.getValue(), playerName)) {
						dialog.open();
						return;
					}
					VaadinSession.getCurrent().setAttribute("gameName", labelSelect.getValue());
					UI.getCurrent().navigate("belot");
					UI.getCurrent().getPage().reload();
					return;
				}
				if (labelField.getValue() != null && !"".equals(labelField.getValue())) {
					if (!GameManager.beginGame(labelField.getValue(), playerName)) {
						dialog.open();
						return;
					}
					VaadinSession.getCurrent().setAttribute("gameName", labelField.getValue());
					UI.getCurrent().navigate("belot");
					UI.getCurrent().getPage().reload();
					return;
				} else {

					dialog.open();
				}
			}
		});
		
		Image image = new Image("/frontend/images/cards/tikva.jpg", "10C");
		image.getElement().getStyle().set("position", "absolute");
		image.getElement().getStyle().set("top", "5em");
		image.getElement().getStyle().set("right", "5em");
		image.setVisible(true);
		image.setHeight("20em");

		add(labelSelect, labelField, button, image);
	}

}
