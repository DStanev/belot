package com.belot.logic;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CardFactory {

	private Random r = new Random();
	private Set<Integer> usedCards = new HashSet<Integer>();

	public Set<Card> generateEightRandomCards() {
		Set<Card> result = new HashSet<Card>();
		for (int i = 0; i < 8; i++) {
			int nextCardNum = r.nextInt(32);
			while (usedCards.contains(nextCardNum)) {
				nextCardNum = r.nextInt(32);
			}
			usedCards.add(nextCardNum);
			result.add(new Card(createCard(nextCardNum)));
		}
		return result;
	}

	public void reset() {
		usedCards.clear();
	}

	private String createCard(int cardNumber) {
		String card = "";
		switch (cardNumber % 8) {
		case 0:
			card = "7";
			break;
		case 1:
			card = "8";
			break;
		case 2:
			card = "9";
			break;
		case 3:
			card = "10";
			break;
		case 4:
			card = "J";
			break;
		case 5:
			card = "Q";
			break;
		case 6:
			card = "K";
			break;
		case 7:
			card = "A";
			break;
		}

		switch (cardNumber / 8) {
		case 0:
			card = card + "C";
			break;
		case 1:
			card = card + "D";
			break;
		case 2:
			card = card + "H";
			break;
		case 3:
			card = card + "S";
			break;
		}
		return card;
	}

	public static void sortCards(Player player, Call call) {
		Arrays.sort(player.cards, new Comparator<Card>() {

			@Override
			public int compare(Card o1, Card o2) {
				int res = o1.color.getValue() - o2.color.getValue();
				if (res == 0) {
					if (call == Call.All_KOZ) {
						res = o1.kozPower - o2.kozPower;
					} else {
						if (o1.color.toString().equals(call.toString())) {
							res = o1.kozPower - o2.kozPower;
						} else {
							res = o1.bezPower - o2.bezPower;
						}
					}
				}
				return res;
			}
		});
	}

	public void sortFirstCards(Card[] cards) {
		Arrays.sort(cards, 0, 5, new Comparator<Card>() {

			@Override
			public int compare(Card o1, Card o2) {
				int res = o1.color.getValue() - o2.color.getValue();
				if (res == 0) {
					res = o1.num - o2.num;
				}
				return res;
			}
		});

	}

}
