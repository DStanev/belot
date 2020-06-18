package com.belot.logic;

public class DummyResult implements Cloneable {

	public int h;
	public int b;

	public int getH() {
		return h;
	}

	public int getB() {
		return b;
	}

	public void setResult(int resultA) {
		this.h = resultA;
	}

	public DummyResult(int resA, int resB) {
		h = resA;
		b = resB;
	}

	public DummyResult() {
	}
}
