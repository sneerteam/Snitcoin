package io.github.felipebueno.core;

import android.graphics.Bitmap;

import org.bitcoinj.core.Address;

import java.util.List;

import io.github.felipebueno.core.util.Currency;

public interface Snitcoin {

	Address currentReceiveAddress();
	double balanceInBTC();
	double balanceIn(Currency currency);
	Bitmap qrCode(String address, int size);
	String requestUri();

	List<ExchangeRate> exchangeRates();

	ExchangeRate currentDefaultRate();

	void setDefault(ExchangeRate rate);

}

