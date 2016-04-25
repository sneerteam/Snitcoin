package sneer.bitcoin.core;

public class ExchangeRate {

	public final String code;
	public final double rate;
	public boolean isDefault;

	public ExchangeRate(String code, double rate, boolean isDefault) {
		this.code = code;
		this.rate = rate;
		this.isDefault = isDefault;
	}

	@Override
	public String toString() {
		return "ExchangeRate{" +
				"code='" + code + '\'' +
				", rate=" + rate +
				", isDefault=" + isDefault +
				'}';
	}
}
