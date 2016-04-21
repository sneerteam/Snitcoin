package io.github.felipebueno.snitcoin;

import android.content.Context;
import android.content.SharedPreferences;

import static io.github.felipebueno.snitcoin.SnitcoinActivity.snitcoin;

public class Utils {

	public static final String PREF_EXCHANGE_RATES = "ExchangeRates";
	public static final String RATE = "rate";

	public static void setDefaultRateFrom(SharedPreferences prefs, Context context) {
		String code = prefs.getString(RATE, "");
		if (code != "")
			snitcoin.setDefault(snitcoin.rateBy(code));
	}
}
