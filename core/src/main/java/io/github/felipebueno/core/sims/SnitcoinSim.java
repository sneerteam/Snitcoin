package io.github.felipebueno.core.sims;

import android.graphics.Bitmap;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.Coin;
import org.bitcoinj.uri.BitcoinURI;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.github.felipebueno.core.ExchangeRate;
import io.github.felipebueno.core.Snitcoin;
import io.github.felipebueno.core.util.Currency;
import io.github.felipebueno.core.util.QrCode;

import static io.github.felipebueno.core.util.Constants.PARAMS;
import static io.github.felipebueno.core.util.Constants.TO;

public class SnitcoinSim implements Snitcoin {

	public SnitcoinSim() {
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
	public BigDecimal balanceIn(Currency currency) {
		double btc = 0;
		switch (currency) {
			case USD:
				btc = balanceInBTC() * 428.08;
				break;
			case BRL:
				btc = balanceInBTC() * 428.08 * 3.58;
				break;
		}
		return new BigDecimal(btc).setScale(2, BigDecimal.ROUND_CEILING);
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
		List<ExchangeRate> ret = new ArrayList<>();
		ExchangeRate usd = new ExchangeRate("USD", 436.98, true);
		ExchangeRate brl = new ExchangeRate("BRL", 1550.36, false);
		ret.add(usd);
		ret.add(brl);
		return ret;
	}

}
