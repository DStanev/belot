package com.belot.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player {

	private String name;

	public Card[] cards;

	public Set<Card> wincards = new HashSet<Card>();

	public String getName() {
		return name;
	}

	public Player(String name) {
		this.name = name;
	}

	public Card activeCard;

	public Call call = Call.EMPTY;

	public int number;

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj == this)
			return true;
		if (!(obj instanceof Player))
			return false;
		Player other = (Player) obj;
		if (this == null || obj == null) {
			return false;
		}
		return this.getName().equals(other.getName());
	}

	public void useCard(Card card) {
		for (int i = 0; i < 8; i++) {
			if (cards[i].equals(card)) {
				cards[i].src = "EMPTY";
				cards[i].used = true;
				break;
			}
		}
		activeCard = card;
	}

	public boolean hasColor(Color color) {
		for (int i = 0; i < 8; i++) {
			if (!cards[i].used) {
				if (cards[i].color == color) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean hasHigherCard(Call call, Card card) {
		for (int i = 0; i < 8; i++) {
			if (!cards[i].used) {
				if (cards[i].beats(card, call)) {
					return true;
				}
			}
		}
		return false;
	}

	public void winCards(Card card) {
		wincards.add(card);
	}

	public int belots;

	public List<Announce> announces = new ArrayList<Announce>();

	public void cleadAnnounces() {
		belots = 0;
		announces.clear();
	}

	public boolean lastTen = false;
	public boolean clicked = false;
}
