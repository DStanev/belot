package com.belot.logic;

public class Card {
	public String src;

	public int kozPower;
	public int bezPower;
	public int kozValue;
	public int bezValue;
	public int num;

	public Card(String id) {
		if (id.contains("frontend")) {
			src = id;
		} else {
			src = "/frontend/images/cards/" + id + ".jpg";
		}
		if (src.charAt(src.length() - 5) == 'H') {
			color = Color.КУПА;
		}
		if (src.charAt(src.length() - 5) == 'S') {
			color = Color.ПИКА;
		}
		if (src.charAt(src.length() - 5) == 'D') {
			color = Color.КАРО;
		}
		if (src.charAt(src.length() - 5) == 'C') {
			color = Color.СПАТИЯ;
		}

		if (src.charAt(src.length() - 6) == '7') {
			power = Power.Seven;
			kozPower = 1;
			bezPower = 1;
			kozValue = 0;
			bezValue = 0;
			num = 1;
		}
		if (src.charAt(src.length() - 6) == '8') {
			power = Power.Eight;
			kozPower = 2;
			bezPower = 2;
			kozValue = 0;
			bezValue = 0;
			num = 2;
		}
		if (src.charAt(src.length() - 6) == '9') {
			power = Power.Nine;
			kozPower = 7;
			bezPower = 3;
			kozValue = 14;
			bezValue = 0;
			num = 3;
		}
		if (src.charAt(src.length() - 6) == '0') {
			power = Power.Ten;
			kozPower = 5;
			bezPower = 7;
			kozValue = 10;
			bezValue = 10;
			num = 4;
		}
		if (src.charAt(src.length() - 6) == 'J') {
			power = Power.Jack;
			kozPower = 8;
			bezPower = 4;
			kozValue = 20;
			bezValue = 2;
			num = 5;
		}
		if (src.charAt(src.length() - 6) == 'Q') {
			power = Power.Queen;
			kozPower = 3;
			bezPower = 5;
			kozValue = 3;
			bezValue = 3;
			num = 6;
		}
		if (src.charAt(src.length() - 6) == 'K') {
			power = Power.King;
			kozPower = 4;
			bezPower = 6;
			kozValue = 4;
			bezValue = 4;
			num = 7;
		}
		if (src.charAt(src.length() - 6) == 'A') {
			power = Power.Ace;
			kozPower = 6;
			bezPower = 8;
			kozValue = 11;
			bezValue = 11;
			num = 8;
		}
	}

	public Power power;
	public Color color;

	public boolean used = false;

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj == this)
			return true;
		if (!(obj instanceof Card))
			return false;
		Card other = (Card) obj;
		if (this == null || obj == null) {
			return false;
		}
		return this.power == other.power && this.color == other.color;
	}

	public boolean beats(Card card, Call call) {
		if (card.color == this.color) {
			if (call == Call.All_KOZ || call.toString().equals(card.color.toString())) {
				return this.kozPower > card.kozPower;
			} else {
				return this.bezPower > card.bezPower;
			}
		}
		if (this.color.toString().equals(call.toString())) {
			return true;
		}
		return false;
	}
}
