package com.belot.logic;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnnounceManager {

	public static Set<String> getAnnounces(Card[] cards, Call call, Card newCard, Card firstCard, int turns) {
		Set<String> result = new HashSet<String>();
		if (call == Call.NO_KOZ) {
			return result;
		}
		if (turns == 0) {
			result.addAll(getNeBelots(cards, call, newCard, firstCard));
		}
		if (isBelot(cards, call, newCard, firstCard)) {
			result.add("Белот");
		}
		return result;
	}

	public static void setAnnounces(Set<String> announces, Player player) {
		for (String str : announces) {
			if (str != "Белот") {
				player.announces.add(new Announce(str));
			} else {
				player.belots++;
			}
		}
	}

	public static Set<String> getNeBelots(Card[] cards, Call call, Card newCard, Card firstCard) {
		Set<String> result = new HashSet<String>();

		Card[] sorted = Arrays.copyOf(cards, cards.length);
		Arrays.sort(sorted, new Comparator<Card>() {

			@Override
			public int compare(Card o1, Card o2) {
				int res = o1.color.getValue() - o2.color.getValue();
				if (res == 0) {
					res = o1.num - o2.num;
				}
				return res;
			}
		});
		Map<Power, Integer> map = new HashMap<Power, Integer>();
		map.put(sorted[0].power, 1);
		int size = 1;
		for (int i = 1; i < 8; i++) {
			if (map.get(sorted[i].power) == null) {
				map.put(sorted[i].power, 1);
			} else {
				int prev = map.get(sorted[i].power);
				map.put(sorted[i].power, prev + 1);
			}
			if (sorted[i].color == sorted[i - 1].color && sorted[i - 1].num + 1 == sorted[i].num) {
				size++;
			} else {
				if (size == 3) {
					result.add(new Announce(sorted[i - 1].power, "Терца").toString());
				}
				if (size == 4) {
					result.add(new Announce(sorted[i - 1].power, "50").toString());
				}
				if (size > 4) {
					result.add(new Announce(sorted[i - 1].power, "100").toString());
				}
				size = 1;
			}
		}
		if (size == 3) {
			result.add(new Announce(sorted[7].power, "Терца").toString());
		}
		if (size == 4) {
			result.add(new Announce(sorted[7].power, "50").toString());
		}
		if (size > 4) {
			result.add(new Announce(sorted[7].power, "100").toString());
		}
		for (Power power : map.keySet()) {
			if (map.get(power) == 4) {
				result.add(new Announce(power, "Каре").toString());
			}
		}
		return result;
	}

	public static boolean isBelot(Card[] cards, Call call, Card newCard, Card firstCard) {
		if (newCard.power == Power.King) {
			if ((call == Call.All_KOZ && (firstCard == null || firstCard.color == newCard.color))
					|| call.toString().equals(newCard.color.toString())) {
				for (Card card : cards) {
					if (!card.used && card.power == Power.Queen && card.color == newCard.color) {
						return true;
					}
				}
			}
		}
		if (newCard.power == Power.Queen) {
			if ((call == Call.All_KOZ && (firstCard == null || firstCard.color == newCard.color))
					|| call.toString().equals(newCard.color.toString())) {
				for (Card card : cards) {
					if (!card.used && card.power == Power.King && card.color == newCard.color) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static int whiTheFuck(Set<Announce> announcsA, Set<Announce> announcsB, int type) {
		Announce highA = null;
		for (Announce announce : announcsA) {
			if (announce.type == type && announce.beats(highA)) {
				if (announce.beats(highA)) {
					highA = announce;
				}
			}
		}
		Announce highB = null;
		for (Announce announce : announcsB) {
			if (announce.type == type && announce.beats(highA)) {
				if (announce.beats(highB)) {
					highB = announce;
				}
			}
		}
		if (highA == null) {
			return 2;
		}
		if (highB == null) {
			return 1;
		}
		if (highA.beats(highB)) {
			return 1;
		}
		return 2;
	}

}
