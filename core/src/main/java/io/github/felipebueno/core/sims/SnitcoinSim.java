package io.github.felipebueno.core.sims;

import android.graphics.Bitmap;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.Coin;
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
			new ExchangeRate("USD", 436.98, rounded(436.98 * balanceInBTC(), 2), false),
			new ExchangeRate("ABC", 223.32, rounded(223.32 * balanceInBTC(), 2), false),
			new ExchangeRate("BRL", 1550.36, rounded(1550.36 * balanceInBTC(), 2), true),
			new ExchangeRate("YTU", 554.12, rounded(554.12 * balanceInBTC(), 2), false),
			new ExchangeRate("OIA", 78.65, rounded(78.65 * balanceInBTC(), 2), false),
			new ExchangeRate("AEW", 123.45, rounded(123.45 * balanceInBTC(), 2), false),
			new ExchangeRate("FTW", 998.65, rounded(998.65 * balanceInBTC(), 2), false),
			new ExchangeRate("SBT", 345.43, rounded(345.43 * balanceInBTC(), 2), false),
			new ExchangeRate("GNT", 183.12, rounded(183.12 * balanceInBTC(), 2), false),
			new ExchangeRate("WWE", 10.97, rounded(10.97 * balanceInBTC(), 2), false)
	);

	public SnitcoinSim() {
	}

	private static double rounded(double btc, int scale) {
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
		return 0.042;
	}

	@Override
	public double balanceConverted() {
		return rounded(balanceInBTC() * currentDefaultRate().rate, 2);
	}

	@Override
	public Bitmap qrCode(String address, int size) {
		return QrCode.bitmap(address, size);
	}

	@Override
	public String requestUri() {
		return BitcoinURI.convertToBitcoinURI(currentReceiveAddress(), Coin.valueOf(4200000), "", "");
	}

	@Override
	public List<ExchangeRate> exchangeRates() {
		return exchangeRates;
	}

	@Override
	public ExchangeRate currentDefaultRate() {
		for (ExchangeRate rate : exchangeRates)
			if (rate.isDefault)
				return rate;
		return null;
	}

	@Override
	public void setDefault(ExchangeRate rate) {
		currentDefaultRate().isDefault = false;
		rate.isDefault = true;
	}

}
