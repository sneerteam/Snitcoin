package sneer.bitcoin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;

import sneer.android.PartnerSession;
import sneer.bitcoin.core.ExchangeRate;

import static android.view.View.*;
import static sneer.bitcoin.SnitcoinApp.snitcoin;
import static sneer.bitcoin.Utils.PREF_EXCHANGE_RATES;
import static sneer.bitcoin.Utils.RATE;

public class BitcoinRequestActivity extends AppCompatActivity {

	private PartnerSession session;
	private Button btnRequest;
	private SharedPreferences prefs;
	private TextView txtAmountInBitcoins;
	private EditText edtAmount;
	private Button btnCancel;
	private Spinner spnCurrencies;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bitcoin_request);

		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btnRequest = (Button) findViewById(R.id.btnRequest);
		btnRequest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		edtAmount = (EditText) findViewById(R.id.edtAmount);
		edtAmount.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				updateCurrencyAndAmmountInBTC();
			}
		});

		txtAmountInBitcoins = (TextView) findViewById(R.id.txtAmountInBitcoins);

		final CurrenciesAdapter adapter = new CurrenciesAdapter(this, R.layout.currency_spinner_item_dark, snitcoin.currencyCodes());
		spnCurrencies = (Spinner) findViewById(R.id.spnCurrencies);
		adapter.add("BTC");
		spnCurrencies.setAdapter(adapter);
		spnCurrencies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String code = (String) parent.getItemAtPosition(position);
				txtAmountInBitcoins.setVisibility(VISIBLE);

				if (code.equals("BTC")) {
					txtAmountInBitcoins.setVisibility(GONE);
					return;
				}

				ExchangeRate rate = snitcoin.rateBy(code);
				snitcoin.setDefault(rate);
				adapter.notifyDataSetChanged();
				prefs.edit().putString(RATE, rate.code).apply();
				updateCurrencyAndAmmountInBTC();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});;

		updateCurrencyAndAmmountInBTC();
	}

	private void updateCurrencyAndAmmountInBTC() {
		prefs = getSharedPreferences(PREF_EXCHANGE_RATES, MODE_PRIVATE);

		txtAmountInBitcoins.setText("(0.000000 BTC)");

		String ammountStr = edtAmount.getText().toString();
		if (TextUtils.isEmpty(ammountStr)) return;

		Double ammountToConvert = Double.valueOf(ammountStr);
		BigDecimal ammountInBTC = SnitcoinApp.snitcoin.amountInBTC(ammountToConvert);

		txtAmountInBitcoins.setText("(" + ammountInBTC + " BTC)");
	}

}
