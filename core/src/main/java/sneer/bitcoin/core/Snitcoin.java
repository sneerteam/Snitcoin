package sneer.bitcoin.core;

import android.graphics.Bitmap;

import org.bitcoinj.core.Address;

import java.math.BigDecimal;
import java.util.List;

public interface Snitcoin {

	Address currentReceiveAddress();
	BigDecimal balanceInBTC();
	double balanceConverted();
	Bitmap qrCode(String address, int size);
	String requestUri();

	List<ExchangeRate> exchangeRates();

	ExchangeRate currentDefaultRate();

	void setDefault(ExchangeRate rate);

	ExchangeRate rateBy(String code);

	BigDecimal amountInBTC(double amount);

	double amountConverted(BigDecimal btc);

}
