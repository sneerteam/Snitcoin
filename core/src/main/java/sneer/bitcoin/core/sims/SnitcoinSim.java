package sneer.bitcoin.core.sims;

import android.graphics.Bitmap;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.uri.BitcoinURI;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sneer.bitcoin.core.ExchangeRate;
import sneer.bitcoin.core.Snitcoin;
import sneer.bitcoin.core.util.QrCode;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static sneer.bitcoin.core.util.Constants.PARAMS;
import static sneer.bitcoin.core.util.Constants.TO;

public class SnitcoinSim implements Snitcoin {

	public List<ExchangeRate> exchangeRates = Arrays.asList(
			new ExchangeRate("USD", 436.98, true),
			new ExchangeRate("ABC", 223.32, false),
			new ExchangeRate("BRL", 1550.36, false),
			new ExchangeRate("FBI", 554.12, false),
			new ExchangeRate("NSA", 78.65, false),
			new ExchangeRate("ISO", 123.45, false),
			new ExchangeRate("FTW", 998.65, false),
			new ExchangeRate("SBT", 345.43, false),
			new ExchangeRate("CNT", 183.12, false),
			new ExchangeRate("WWE", 10.97, false),
			new ExchangeRate("BLA", 43.93, false)
	);

	public List<String> currencyCodes = new ArrayList<>();

	public SnitcoinSim() {

	}

	public static BigDecimal round(BigDecimal btc, int scale) {
		return btc.setScale(scale, ROUND_HALF_UP);
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
	public BigDecimal balanceInBTC() {
		return BigDecimal.valueOf(0.04265478);
	}

	@Override
	public double balanceConverted() {
		return round(balanceInBTC().multiply(BigDecimal.valueOf(currentDefaultRate().rate)), 2).doubleValue();
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
	public List<String> currencyCodes() {
		currencyCodes.clear();
		for (ExchangeRate rate:exchangeRates)
			currencyCodes.add(rate.code);
		return currencyCodes;
	}


	@Override
	public ExchangeRate currentDefaultRate() {
		ExchangeRate ret = null;
		for (ExchangeRate rate : exchangeRates)
			if (rate.isDefault) {
				ret = rate;
				break;
			}
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
			if (code.equals(rate.code)) {
				ret = rate;
				break;
			}
		return ret;
	}

	@Override
	public BigDecimal amountInBTC(double amount) {
		return BigDecimal.valueOf(amount).divide(BigDecimal.valueOf(currentDefaultRate().rate), 8, ROUND_HALF_UP);
	}

	@Override
	public double amountConverted(BigDecimal btc) {
		return round(btc.multiply(BigDecimal.valueOf(currentDefaultRate().rate)), 2).doubleValue();
	}

}
