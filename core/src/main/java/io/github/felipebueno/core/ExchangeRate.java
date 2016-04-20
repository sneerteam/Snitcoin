package io.github.felipebueno.core;

public class ExchangeRate {

	public final String code;
	public final double val;
	public final boolean isDefault;

	public ExchangeRate(String code, double val, boolean isDefault) {
		this.code = code;
		this.val = val;
		this.isDefault = isDefault;
	}

}
