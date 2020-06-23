package com.belot.logic;

public enum Color {
	ПИКА("Пика"), КУПА("Купа"), СПАТИЯ("Спатия"), КАРО("Каро");

	private String call;

	Color(String string) {
		call = string;
	}

	@Override
	public String toString() {
		return call;
	}

	public int getValue() {
		switch (call) {
		case "Пика":
			return 4;
		case "Купа":
			return 3;
		case "Каро":
			return 2;

		}
		return 1;
	}
}
