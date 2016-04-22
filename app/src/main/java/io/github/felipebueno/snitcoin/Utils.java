package io.github.felipebueno.snitcoin;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.Toast;

import static io.github.felipebueno.snitcoin.SnitcoinApp.snitcoin;

public class Utils {

	public static final String PREF_EXCHANGE_RATES = "ExchangeRates";
	public static final String RATE = "rate";

	public static void setDefaultRateFrom(SharedPreferences prefs, Context context) {
		String code = prefs.getString(RATE, "");
		if (!TextUtils.isEmpty(code))
			snitcoin.setDefault(snitcoin.rateBy(code));
	}

	public static void toast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

}
