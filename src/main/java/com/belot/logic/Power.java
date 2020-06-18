package com.belot.logic;

public enum Power {
	Seven("Seven"), Eight("Eight"), Nine("Nine"), Ten("Ten"), Jack("Jack"), Queen("Queen"), King("King"), Ace("Ace");

	public int getValue() {
		switch (call) {
		case "Seven":
			return 1;
		case "Eight":
			return 2;
		case "Nine":
			return 3;
		case "Ten":
			return 4;
		case "Jack":
			return 5;
		case "Queen":
			return 6;
		case "King":
			return 7;
		}
		return 8;
	}

	public String call;

	Power(String string) {
		call = string;
	}

	@Override
	public String toString() {
		return call;
	}

	public Power fromString(String str) {
		switch (str) {
		case "Seven":
			return Power.Seven;
		case "Eight":
			return Power.Eight;
		case "Ten":
			return Power.Ten;
		case "Queen":
			return Power.Queen;
		case "King":
			return Power.King;
		case "Ace":
			return Power.Ace;
		case "Nine":
			return Power.Nine;
		}
		return Power.Jack;
	}
}
