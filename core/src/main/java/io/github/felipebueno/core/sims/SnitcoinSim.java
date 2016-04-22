package io.github.felipebueno.core.sims;

import android.graphics.Bitmap;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.uri.BitcoinURI;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import io.github.felipebueno.core.ExchangeRate;
import io.github.felipebueno.core.Snitcoin;
import io.github.felipebueno.core.util.QrCode;

import static io.github.felipebueno.core.util.Constants.PARAMS;
import static io.github.felipebueno.core.util.Constants.TO;

public class SnitcoinSim implements Snitcoin {

	private List<ExchangeRate> exchangeRates = Arrays.asList(
			new ExchangeRate("USD", 436.98,  round(436.98  * balanceInBTC(), 2), true),
			new ExchangeRate("ABC", 223.32,  round(223.32  * balanceInBTC(), 2), false),
			new ExchangeRate("BRL", 1550.36, round(1550.36 * balanceInBTC(), 2), false),
			new ExchangeRate("FBI", 554.12,  round(554.12  * balanceInBTC(), 2), false),
			new ExchangeRate("NSA", 78.65,   round(78.65   * balanceInBTC(), 2), false),
			new ExchangeRate("ISO", 123.45,  round(123.45  * balanceInBTC(), 2), false),
			new ExchangeRate("FTW", 998.65,  round(998.65  * balanceInBTC(), 2), false),
			new ExchangeRate("SBT", 345.43,  round(345.43  * balanceInBTC(), 2), false),
			new ExchangeRate("CNT", 183.12,  round(183.12  * balanceInBTC(), 2), false),
			new ExchangeRate("WWE", 10.97,   round(10.97   * balanceInBTC(), 2), false)
	);

	public SnitcoinSim() {
	}

	private static double round(double btc, int scale) {
		return new BigDecimal(btc).setScale(scale, BigDecimal.ROUND_CEILING).doubleValue();
	}

	@Override
	public Address currentReceiveAddress() {
		try {
			return new Address(PARAMS, TO);
		} catch (AddressFormatException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public double balanceInBTC() {
		return 0.042654;
	}

	@Override
	public double balanceConverted() {
		return round(balanceInBTC() * currentDefaultRate().rate, 2);
	}

	@Override
	public Bitmap qrCode(String address, int size) {
		return QrCode.bitmap(address, size);
	}

	@Override
	public String requestUri() {
		return BitcoinURI.convertToBitcoinURI(currentReceiveAddress(), null, null, null);
	}

	@Override
	public List<ExchangeRate> exchangeRates() {
		return exchangeRates;
	}

	@Override
	public ExchangeRate currentDefaultRate() {
		ExchangeRate ret = null;
		for (ExchangeRate rate : exchangeRates)
			if (rate.isDefault)
				ret = rate;
		return ret;
	}

	@Override
	public void setDefault(ExchangeRate rate) {
		currentDefaultRate().isDefault = false;
		rate.isDefault = true;
	}

	@Override
	public ExchangeRate rateBy(String code) {
		ExchangeRate ret = currentDefaultRate();
		for (ExchangeRate rate : exchangeRates)
			if (code.equals(rate.code))
				ret = rate;
		return ret;
	}

	@Override
	public BigDecimal ammountInBTC(double ammount) {
		return BigDecimal.valueOf(ammount).divide(BigDecimal.valueOf(currentDefaultRate().rate), 6, BigDecimal.ROUND_CEILING);
	}

}
