package io.github.felipebueno.core;

import java.math.BigDecimal;

public class ExchangeRate {

	public final String code;
	public final double rate;
	public final BigDecimal balance;
	public boolean isDefault;

	public ExchangeRate(String code, double rate, BigDecimal balance, boolean isDefault) {
		this.code = code;
		this.rate = rate;
		this.balance = balance;
		this.isDefault = isDefault;
	}

	@Override
	public String toString() {
		return "ExchangeRate{" +
				"code='" + code + '\'' +
				", rate=" + rate +
				", balance=" + balance +
				", isDefault=" + isDefault +
				'}';
	}
}
