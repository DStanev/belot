package com.belot.logic;

public enum Call {

	PASS("Пас"), CLUB("Спатия"), DIAMOND("Каро"), HEART("Купа"), SPADE("Пика"), NO_KOZ("Без коз"),
	All_KOZ("Всичко коз"), COUNTER("Контра"), RECOUNTER("Реконтра"), EMPTY("Празен");

	private String call;

	Call(String string) {
		call = string;
	}

	@Override
	public String toString() {
		return call;
	}

	public static Call getEnum(String value) {
		switch (value) {
		case "Пас":
			return Call.PASS;
		case "Спатия":
			return Call.CLUB;
		case "Каро":
			return Call.DIAMOND;
		case "Купа":
			return Call.HEART;
		case "Пика":
			return Call.SPADE;
		case "Без коз":
			return Call.NO_KOZ;
		case "Всичко коз":
			return Call.All_KOZ;
		case "Контра":
			return Call.COUNTER;
		case "Реконтра":
			return Call.RECOUNTER;
		default:
			return Call.EMPTY;
		}
	}
}
