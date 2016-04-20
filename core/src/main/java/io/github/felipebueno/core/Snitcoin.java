package io.github.felipebueno.core;

import android.graphics.Bitmap;

import org.bitcoinj.core.Address;

import java.math.BigDecimal;
import java.util.List;

import io.github.felipebueno.core.util.Currency;

public interface Snitcoin {

	Address currentReceiveAddress();
	double balanceInBTC();
	BigDecimal balanceIn(Currency currency);
	Bitmap qrCode(String address, int size);
	String requestUri();

	List<ExchangeRate> exchangeRates();

}

