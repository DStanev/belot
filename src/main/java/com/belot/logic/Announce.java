package com.belot.logic;

public class Announce {

	public Power highesCard;

	public int type;

	public int size;

	public int value;

	public String strValue;

	public Announce(Power power, String type) {
		this.highesCard = power;
		if (type.equals("Терца")) {
			this.type = 1;
			value = 20;
			size = 1;
		}
		if (type.equals("50")) {
			this.type = 1;
			value = 50;
			size = 2;
		}
		if (type.equals("100")) {
			this.type = 1;
			value = 100;
			size = 3;
		}
		if (type.equals("Каре")) {
			this.type = 2;
			if (power == Power.Seven || power == Power.Eight) {
				value = 0;
			}
			if (power == Power.Jack) {
				value = 200;
			}
			if (power == Power.Nine) {
				value = 150;
			}
			if (power == Power.Ten || power == Power.King || power == Power.Queen || power == Power.Ace) {
				value = 100;
			}
		}

		StringBuilder stringBuilder = new StringBuilder();
		if (this.type == 1) {
			if (type.equals("Терца")) {
				stringBuilder.append("Терца до ");
			} else {
				if (type.equals("50")) {
					stringBuilder.append("50 до ");
				} else {
					if (type.equals("100")) {
						stringBuilder.append("100 до ");
					}
				}
			}
			stringBuilder.append(highesCard.toString());
		} else {
			stringBuilder.append("Каре от ");
			stringBuilder.append(highesCard.toString());
		}
		strValue = stringBuilder.toString();
	}

	public Announce(String str) {
		if (!str.equals("Белот")) {
			String hk = "";
			if (str.startsWith("Терца до")) {
				this.size = 1;
				this.type = 1;
				hk = str.substring(9, str.length());
				value = 20;
			} else {
				if (str.startsWith("50 до")) {
					this.size = 2;
					this.type = 1;
					hk = str.substring(6, str.length());
					value = 50;
				} else {
					if (str.startsWith("100 до")) {
						this.size = 3;
						this.type = 1;
						hk = str.substring(7, str.length());
						value = 100;
					} else {
						if (str.startsWith("Каре от")) {
							this.type = 2;
							hk = str.substring(8, str.length());
							if (Power.valueOf(hk) == Power.Seven || Power.valueOf(hk) == Power.Eight) {
								value = 0;
							} else {
								if (Power.valueOf(hk) == Power.Nine) {
									value = 150;
								} else {
									if (Power.valueOf(hk) == Power.Jack) {
										value = 200;
									} else {
										value = 100;
									}
								}
							}
						}
					}
				}
			}
			highesCard = Power.valueOf(hk);
			strValue = str;
		} else {
			strValue = "Белот";
		}
	}

	public String toString() {
		return strValue;
	}

	public String getHiddenValue() {
		if (type == 2) {
			return "Каре";
		}
		if (size == 1) {
			return "Терца";
		}
		if (size == 2) {
			return "50";
		}
		if (strValue == "Белот") {
			return "Белот";
		}
		return "100";
	}

	public boolean beats(Announce highB) {
		if (highB == null) {
			return true;
		}
		if (type == 1) {
			if (size > highB.size) {
				return true;
			}
			if (size < highB.size) {
				return false;
			}
			return highesCard.getValue() > highB.highesCard.getValue();
		} else {
			if (value > highB.value) {
				return true;
			}
			if (value < highB.value) {
				return false;
			}
			return highesCard.getValue() > highB.highesCard.getValue();
		}
	}

}
