package com.belot.main;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.belot.logic.Call;
import com.belot.logic.Card;
import com.belot.logic.DummyResult;
import com.belot.logic.Game;
import com.belot.logic.GameManager;
import com.belot.main.Broadcaster;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;

@Route(value = "belot")
@Push
public class MainView extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	HorizontalLayout todosList = new HorizontalLayout();

	Image img1 = new Image("", "");
	Image img2 = new Image("", "");
	Image img3 = new Image("", "");
	Image img4 = new Image("", "");
	Image img5 = new Image("", "");
	Image img6 = new Image("", "");
	Image img7 = new Image("", "");
	Image img8 = new Image("", "");

	Grid<DummyResult> result = new Grid<>(DummyResult.class);

	Span myName = new Span("player1");
	Span myCall = new Span("");
	Span partnerName = new Span("player3");
	Span partnerCall = new Span("");
	Span leftName = new Span("player4");
	Span leftCall = new Span("");
	Span rightName = new Span("player2");
	Span rightCall = new Span("");

	Span obqva = new Span();

	Span player1name = new Span("player1");
	Span player2name = new Span("player2");
	Span player3name = new Span("player3");
	Span player4name = new Span("player4");

	Span caller = new Span("");
	Span call = new Span("");
	Span counter = new Span("");

	Span gameN = new Span("");
	Span globalResultA = new Span("");
	Span globalResultB = new Span("");

	Image left = new Image("/frontend/images/cards/Gray_back.jpg", "10C");
	Image right = new Image("/frontend/images/cards/Gray_back.jpg", "10C");
	Image partner = new Image("/frontend/images/cards/Gray_back.jpg", "10C");
	Image my = new Image("/frontend/images/cards/Gray_back.jpg", "10C");

	ComboBox<String> comboBox = new ComboBox<String>();

	private String gameName;
	private String player;

	private Registration broadcasterRegistration;

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		UI ui = attachEvent.getUI();
		broadcasterRegistration = Broadcaster.register(newMessage -> {
			ui.access(() -> kor(newMessage));
		});
	}

	@Override
	protected void onDetach(DetachEvent detachEvent) {
		broadcasterRegistration.remove();
		broadcasterRegistration = null;
	}

	private void kor(String message) {
		drawMyCards("KOR");
	}

	private void drawMyCards(String message) {
		try {
			Game game = GameManager.getGame(gameName);
			Card[] cards = game.getPlayer(player, "my").cards;

			obqva.setText("obqva");
			if (GameManager.getGame(gameName).hasActiveAnnounce) {
				obqva.setText(game.activeAnnounce);
				add(obqva);
				obqva.setVisible(true);
			} else {
				remove(obqva);
				obqva.setVisible(false);
			}

			result.setItems(GameManager.getGame(gameName).getLocalgameResult(player));
			globalResultA.setText(String.valueOf(GameManager.getGame(gameName).getGloaballgameResultA(player)));
			globalResultB.setText(String.valueOf(GameManager.getGame(gameName).getGloaballgameResultB(player)));

			myName.getElement().getStyle().set("color", "green");
			partnerName.getElement().getStyle().set("color", "green");
			left.getElement().getStyle().set("color", "green");
			right.getElement().getStyle().set("color", "green");
			// if (cards != null) {
			img1.setSrc(cards[0].src);
			img2.setSrc(cards[1].src);
			img3.setSrc(cards[2].src);
			img4.setSrc(cards[3].src);
			img5.setSrc(cards[4].src);
			img6.setSrc(cards[5].src);
			img7.setSrc(cards[6].src);
			img8.setSrc(cards[7].src);
			if (game.isAnnouncePhase) {
				img1.setVisible(true);
				img2.setVisible(true);
				img3.setVisible(true);
				img4.setVisible(true);
				img5.setVisible(true);
			}
			if (game.isAnnouncePhase) {
				img6.setVisible(false);
				img7.setVisible(false);
				img8.setVisible(false);
			} else {
				if (!img6.getSrc().contains("EMPTY")) {
					img6.setVisible(true);
				}
				if (!img7.getSrc().contains("EMPTY")) {
					img7.setVisible(true);
				}
				if (!img8.getSrc().contains("EMPTY")) {
					img8.setVisible(true);
				}
			}

			rightName.getElement().getStyle().set("color", "green");
			leftName.getElement().getStyle().set("color", "green");
			partnerName.getElement().getStyle().set("color", "green");
			myName.getElement().getStyle().set("color", "green");

			if (game.getPlayer(player, "my") != null) {
				if (game.getPlayer(player, "my").activeCard != null) {
					my.setSrc(game.getPlayer(player, "my").activeCard.src);
				} else {
					my.setSrc("/frontend/images/cards/Gray_back.jpg");
				}
				player1name.setText(game.getPlayer(player, "my").getName());
				myName.setText(game.getPlayer(player, "my").getName());
				if (game.isActiveToCall(player, "my") && game.isAnnouncePhase) {
					myName.getElement().getStyle().set("color", "blue");
				}
				if (game.isActiveToplay(player, "my") && game.isGamePhase) {
					myName.getElement().getStyle().set("color", "red");
				}
				if (game.isAnnouncePhase) {
					if (game.getPlayer(player, "my").call != Call.EMPTY) {
						myCall.setText(game.getPlayer(player, "my").call.toString());
						myCall.setVisible(true);
					} else {
						myCall.setVisible(false);
					}
				} else {
					myCall.setVisible(false);
				}
			}
			if (game.getPlayer(player, "partner") != null) {
				if (game.getPlayer(player, "partner").activeCard != null) {
					partner.setSrc(game.getPlayer(player, "partner").activeCard.src);
				} else {
					partner.setSrc("/frontend/images/cards/Gray_back.jpg");
				}
				player3name.setText(game.getPlayer(player, "partner").getName());
				partnerName.setText(game.getPlayer(player, "partner").getName());
				if (game.isActiveToCall(player, "partner") && game.isAnnouncePhase) {
					partnerName.getElement().getStyle().set("color", "blue");
				}
				if (game.isActiveToplay(player, "partner") && game.isGamePhase) {
					partnerName.getElement().getStyle().set("color", "red");
				}
				if (game.isAnnouncePhase) {
					if (game.getPlayer(player, "partner").call != Call.EMPTY) {
						partnerCall.setText(game.getPlayer(player, "partner").call.toString());
						partnerCall.setVisible(true);
					} else {
						partnerCall.setVisible(false);
					}
				} else {
					partnerCall.setVisible(false);
				}
			}
			if (game.getPlayer(player, "left") != null) {
				if (game.getPlayer(player, "left").activeCard != null) {
					left.setSrc(game.getPlayer(player, "left").activeCard.src);
				} else {
					left.setSrc("/frontend/images/cards/Gray_back.jpg");
				}
				player4name.setText(game.getPlayer(player, "left").getName());
				leftName.setText(game.getPlayer(player, "left").getName());
				if (game.isActiveToCall(player, "left") && game.isAnnouncePhase) {
					leftName.getElement().getStyle().set("color", "blue");
				}
				if (game.isActiveToplay(player, "left") && game.isGamePhase) {
					leftName.getElement().getStyle().set("color", "red");
				}
				if (game.isAnnouncePhase) {
					if (game.getPlayer(player, "left").call != Call.EMPTY) {
						leftCall.setText(game.getPlayer(player, "left").call.toString());
						leftCall.setVisible(true);
					} else {
						leftCall.setVisible(false);
					}
				} else {
					leftCall.setVisible(false);
				}
			}
			if (game.getPlayer(player, "right") != null) {
				if (game.getPlayer(player, "right").activeCard != null) {
					right.setSrc(game.getPlayer(player, "right").activeCard.src);
				} else {
					right.setSrc("/frontend/images/cards/Gray_back.jpg");
				}
				rightName.setText(game.getPlayer(player, "right").getName());
				player2name.setText(game.getPlayer(player, "right").getName());
				if (game.isActiveToCall(player, "right") && game.isAnnouncePhase) {
					rightName.getElement().getStyle().set("color", "blue");
				}
				if (game.isActiveToplay(player, "right") && game.isGamePhase) {
					rightName.getElement().getStyle().set("color", "red");
				}
				if (game.isAnnouncePhase) {
					if (game.getPlayer(player, "right").call != Call.EMPTY) {
						rightCall.setText(game.getPlayer(player, "right").call.toString());
						rightCall.setVisible(true);
					} else {
						rightCall.setVisible(false);
					}
				} else {
					rightCall.setVisible(false);
				}
			}

			if (game.call != Call.EMPTY) {
				call.setText(game.call.toString());
			}
			if (game.caller != null) {
				caller.setText(game.caller.getName());
			}
			counter.setText(game.isCounter());

			comboBox.setLabel("ОБЯВА");
			List<String> calls = new LinkedList<String>();
			calls.addAll(game.getPossibleCalls(player));
			comboBox.setItems(calls);

			if (game.isAnnouncePhase && game.isMyTurnToCall(player)) {
				remove(comboBox);
				add(comboBox);
				comboBox.setVisible(true);
			} else {
				comboBox.setVisible(false);
			}
		} catch (

		Throwable t) {
			System.out.println(t);
		}
	}

	public MainView() {
		if (VaadinSession.getCurrent().getAttribute("name") == null) {
			UI.getCurrent().navigate("");
			return;
		}
		String game1 = VaadinSession.getCurrent().getAttribute("gameName").toString();
		gameName = game1;

		String player1 = VaadinSession.getCurrent().getAttribute("name").toString();
		player = player1;
		draw("");
		Set<Image> myCards = new HashSet<Image>();
		myCards.add(img1);
		myCards.add(img2);
		myCards.add(img3);
		myCards.add(img4);
		myCards.add(img5);
		myCards.add(img6);
		myCards.add(img7);
		myCards.add(img8);

		obqva.getElement().getStyle().set("position", "absolute");
		obqva.getElement().getStyle().set("left", "20em");
		obqva.getElement().getStyle().set("top", "28em");

		for (Image img : myCards) {
			img.addClickListener(new ComponentEventListener<ClickEvent<Image>>() {

				@Override
				public void onComponentEvent(ClickEvent event) {
					if (!GameManager.getGame(gameName).isGamePhase) {
						return;
					}
					if (!GameManager.getGame(gameName).isMyTurn(player)) {
						return;
					}
					if (GameManager.getGame(gameName).isValidMove(player, img.getSrc())) {
						if (GameManager.getGame(gameName).getPlayer(player).clicked) {
							return;
						}
						GameManager.getGame(gameName).getPlayer(player).clicked = true;
						GameManager.getGame(gameName).makeMove(player,
								event.getSource().getElement().getAttribute("src"), true);
						if (GameManager.getGame(gameName).isActiveAnnounce) {
							CheckboxGroup<String> checkboxGroup = new CheckboxGroup<>();
							checkboxGroup.setItems(GameManager.getGame(gameName).getAnnounce(player, img.getSrc()));
							setAlignItems(Alignment.CENTER);
							setJustifyContentMode(JustifyContentMode.CENTER);
							Button b = new Button();
							b.setText("Обяви");
							checkboxGroup.add(b);
							add(checkboxGroup);
							b.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

								@Override
								public void onComponentEvent(ClickEvent<Button> event1) {
									GameManager.getGame(gameName).setAnnounce(checkboxGroup.getValue(), player);
									GameManager.getGame(gameName).makeMove(player,
											event.getSource().getElement().getAttribute("src"), false);
									img.setVisible(false);
									final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
									executor.schedule(new Runnable() {
										@Override
										public void run() {
											GameManager.getGame(gameName).endTurn();
											GameManager.getGame(gameName).getPlayer(player).clicked = false;
										}
									}, 0, TimeUnit.SECONDS);
									remove(checkboxGroup);
								}
							});
						} else {
							GameManager.getGame(gameName).hasActiveAnnounce = false;
							GameManager.getGame(gameName).makeMove(player,
									event.getSource().getElement().getAttribute("src"), false);
							img.setVisible(false);
							final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
							executor.schedule(new Runnable() {
								@Override
								public void run() {
									GameManager.getGame(gameName).endTurn();
									GameManager.getGame(gameName).getPlayer(player).clicked = false;
								}
							}, 0, TimeUnit.SECONDS);
						}
					}
				}
			});
		}

		comboBox.addValueChangeListener(valueChangeEvent -> {
			if (valueChangeEvent.getValue() == null) {

			} else {
				comboBox.setVisible(false);
				GameManager.getGame(gameName).makeCall(player, valueChangeEvent.getValue());
			}
			remove(comboBox);
		});

		gameN.setText(gameName);
		gameN.getElement().getStyle().set("position", "absolute");
		gameN.getElement().getStyle().set("left", "2em");
		gameN.getElement().getStyle().set("color", "green");
		gameN.setVisible(true);

		player1name.getElement().getStyle().set("position", "absolute");
		player1name.getElement().getStyle().set("top", "12em");
		player1name.getElement().getStyle().set("left", "2em");
		player1name.getElement().getStyle().set("color", "green");
		player1name.setVisible(true);

		player3name.getElement().getStyle().set("position", "absolute");
		player3name.getElement().getStyle().set("top", "13em");
		player3name.getElement().getStyle().set("left", "2em");
		player3name.getElement().getStyle().set("color", "green");
		player3name.setVisible(true);

		player2name.getElement().getStyle().set("position", "absolute");
		player2name.getElement().getStyle().set("top", "12em");
		player2name.getElement().getStyle().set("left", "8em");
		player2name.getElement().getStyle().set("color", "green");
		player2name.setVisible(true);

		player4name.getElement().getStyle().set("position", "absolute");
		player4name.getElement().getStyle().set("top", "13em");
		player4name.getElement().getStyle().set("left", "8em");
		player4name.getElement().getStyle().set("color", "green");
		player4name.setVisible(true);

		caller.getElement().getStyle().set("position", "absolute");
		caller.getElement().getStyle().set("top", "4em");
		caller.getElement().getStyle().set("left", "2em");
		caller.getElement().getStyle().set("color", "green");
		caller.setVisible(true);

		call.getElement().getStyle().set("position", "absolute");
		call.getElement().getStyle().set("top", "4em");
		call.getElement().getStyle().set("left", "8em");
		call.getElement().getStyle().set("color", "green");
		call.setVisible(true);

		counter.getElement().getStyle().set("position", "absolute");
		counter.getElement().getStyle().set("top", "5em");
		counter.getElement().getStyle().set("left", "8em");
		counter.getElement().getStyle().set("color", "green");
		counter.setVisible(true);

		globalResultA.getElement().getStyle().set("position", "absolute");
		globalResultA.getElement().getStyle().set("top", "15em");
		globalResultA.getElement().getStyle().set("left", "2em");
		globalResultA.getElement().getStyle().set("color", "green");
		globalResultA.setVisible(true);

		globalResultB.getElement().getStyle().set("position", "absolute");
		globalResultB.getElement().getStyle().set("top", "15em");
		globalResultB.getElement().getStyle().set("left", "8em");
		globalResultB.getElement().getStyle().set("color", "green");
		globalResultB.setVisible(true);

		result.setVisible(true);
		result.setColumns("h", "b");
		result.getElement().getStyle().set("position", "absolute");
		result.getElement().getStyle().set("top", "18em");
		result.getElement().getStyle().set("left", "2em");
		result.getElement().getStyle().set("color", "green");
		result.setVisible(true);
		result.setHeight("28em");
		result.setWidth("10em");

		add(left);
		add(right);
		add(partner);
		add(my);
		add(img1);
		add(img2);
		add(img3);
		add(img4);
		add(img5);
		add(img6);
		add(img7);
		add(img8);
		add(myName);
		add(myCall);
		add(partnerCall);
		add(partnerName);
		add(leftCall);
		add(leftName);
		add(rightName);
		add(rightCall);
		add(gameN);
		add(globalResultA);
		add(globalResultB);
		add(result);
		add(player1name);
		add(player2name);
		add(player3name);
		add(player4name);
		add(call);
		add(caller);
		add(counter);
		add(todosList);
	}

	private void draw(String message) {
		Game game = GameManager.getGame(gameName);
		// A component with coordinates for its top-left corner

		left.getElement().getStyle().set("position", "absolute");
		left.getElement().getStyle().set("top", "10em");
		left.getElement().getStyle().set("right", "40em");
		left.setVisible(true);
		left.setHeight("10em");

		right.getElement().getStyle().set("position", "absolute");
		right.getElement().getStyle().set("top", "10em");
		right.getElement().getStyle().set("right", "20em");
		right.setVisible(true);
		right.setHeight("10em");

		partner.getElement().getStyle().set("position", "absolute");
		partner.getElement().getStyle().set("top", "5em");
		partner.getElement().getStyle().set("right", "30em");
		partner.setVisible(true);
		partner.setHeight("10em");

		my.getElement().getStyle().set("position", "absolute");
		my.getElement().getStyle().set("top", "18em");
		my.getElement().getStyle().set("right", "30em");
		my.setVisible(true);
		my.setHeight("10em");

		myName.getElement().getStyle().set("position", "absolute");
		myName.getElement().getStyle().set("top", "28em");
		myName.getElement().getStyle().set("right", "30em");
		myName.getElement().getStyle().set("color", "green");
		myName.setVisible(true);

		myCall.getElement().getStyle().set("position", "absolute");
		myCall.getElement().getStyle().set("top", "28em");
		myCall.getElement().getStyle().set("right", "36em");
		myCall.getElement().getStyle().set("color", "red");
		myCall.setVisible(false);

		partnerName.getElement().getStyle().set("position", "absolute");
		partnerName.getElement().getStyle().set("top", "3em");
		partnerName.getElement().getStyle().set("right", "30em");
		partnerName.getElement().getStyle().set("color", "green");
		partnerName.setVisible(true);

		partnerCall.getElement().getStyle().set("position", "absolute");
		partnerCall.getElement().getStyle().set("top", "15em");
		partnerCall.getElement().getStyle().set("right", "30em");
		partnerCall.getElement().getStyle().set("color", "red");
		partnerCall.setVisible(false);

		leftName.getElement().getStyle().set("position", "absolute");
		leftName.getElement().getStyle().set("top", "8em");
		leftName.getElement().getStyle().set("right", "40em");
		leftName.getElement().getStyle().set("color", "green");
		leftName.setVisible(true);

		leftCall.getElement().getStyle().set("position", "absolute");
		leftCall.getElement().getStyle().set("top", "20em");
		leftCall.getElement().getStyle().set("right", "40em");
		leftCall.getElement().getStyle().set("color", "red");
		leftCall.setVisible(false);

		rightName.getElement().getStyle().set("position", "absolute");
		rightName.getElement().getStyle().set("top", "8em");
		rightName.getElement().getStyle().set("right", "20em");
		rightName.getElement().getStyle().set("color", "green");
		rightName.setVisible(true);

		rightCall.getElement().getStyle().set("position", "absolute");
		rightCall.getElement().getStyle().set("top", "20em");
		rightCall.getElement().getStyle().set("right", "20em");
		rightCall.getElement().getStyle().set("color", "red");
		rightCall.setVisible(false);

		comboBox.getElement().getStyle().set("position", "absolute");
		comboBox.getElement().getStyle().set("top", "10em");
		comboBox.getElement().getStyle().set("left", "20em");

		if (!img1.getSrc().contains("EMPTY")) {
			img1.getElement().getStyle().set("position", "absolute");
			img1.getElement().getStyle().set("top", "30em");
			img1.getElement().getStyle().set("right", "36em");
			img1.setVisible(true);
			img1.setHeight("10em");
		} else {
			img1.setVisible(false);
		}

		if (!img2.getSrc().contains("EMPTY")) {
			img2.getElement().getStyle().set("position", "absolute");
			img2.getElement().getStyle().set("top", "30em");
			img2.getElement().getStyle().set("right", "34em");
			img2.setVisible(true);
			img2.setHeight("10em");
		} else {
			img2.setVisible(false);
		}

		if (!img3.getSrc().contains("EMPTY")) {
			img3.getElement().getStyle().set("position", "absolute");
			img3.getElement().getStyle().set("top", "30em");
			img3.getElement().getStyle().set("right", "32em");
			img3.setVisible(true);
			img3.setHeight("10em");
		} else {
			img3.setVisible(false);
		}

		if (!img4.getSrc().contains("EMPTY")) {
			img4.getElement().getStyle().set("position", "absolute");
			img4.getElement().getStyle().set("top", "30em");
			img4.getElement().getStyle().set("right", "30em");
			img4.setVisible(true);
			img4.setHeight("10em");
		} else {
			img4.setVisible(false);
		}

		if (!img5.getSrc().contains("EMPTY")) {
			img5.getElement().getStyle().set("position", "absolute");
			img5.getElement().getStyle().set("top", "30em");
			img5.getElement().getStyle().set("right", "28em");
			img5.setVisible(true);
			img5.setHeight("10em");
		} else {
			img5.setVisible(false);
		}

		if (!img6.getSrc().contains("EMPTY")) {
			img6.getElement().getStyle().set("position", "absolute");
			img6.getElement().getStyle().set("top", "30em");
			img6.getElement().getStyle().set("right", "26em");
			img6.setVisible(true);
			img6.setHeight("10em");
		} else {
			img6.setVisible(false);
		}

		if (!img7.getSrc().contains("EMPTY")) {
			img7.getElement().getStyle().set("position", "absolute");
			img7.getElement().getStyle().set("top", "30em");
			img7.getElement().getStyle().set("right", "24em");
			img7.setVisible(true);
			img7.setHeight("10em");
		} else {
			img7.setVisible(false);
		}

		if (!img8.getSrc().contains("EMPTY")) {
			img8.getElement().getStyle().set("position", "absolute");
			img8.getElement().getStyle().set("top", "30em");
			img8.getElement().getStyle().set("right", "22em");
			img8.setVisible(true);
			img8.setHeight("10em");
		} else {
			img8.setVisible(false);
		}

		drawMyCards(message);
	}

}