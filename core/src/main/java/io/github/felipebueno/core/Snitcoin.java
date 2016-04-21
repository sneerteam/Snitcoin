package io.github.felipebueno.core;

import android.graphics.Bitmap;

import org.bitcoinj.core.Address;

import java.util.List;

public interface Snitcoin {

	Address currentReceiveAddress();
	double balanceInBTC();
	double balanceConverted();
	Bitmap qrCode(String address, int size);
	String requestUri();

	List<ExchangeRate> exchangeRates();

	ExchangeRate currentDefaultRate();

	void setDefault(ExchangeRate rate);

	ExchangeRate rateBy(String code);

}

