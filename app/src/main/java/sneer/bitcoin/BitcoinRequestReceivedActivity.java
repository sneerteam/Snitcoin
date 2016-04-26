package sneer.bitcoin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

import sneer.bitcoin.core.ExchangeRate;

import static android.view.View.*;
import static sneer.bitcoin.SnitcoinApp.snitcoin;
import static sneer.bitcoin.Utils.PREF_EXCHANGE_RATES;
import static sneer.bitcoin.Utils.RATE;

public class BitcoinRequestReceivedActivity extends AppCompatActivity {

	private EditText edtYes;
	private Button btnSend;
	private Button btnCancel;
	private Spinner spnCurrencies;
	private SharedPreferences prefs;
	private TextView txtBalanceBTC;
	private TextView txtBalanceConverted;
	private TextView txtAmountAskedInBTC;
	private TextView txtAmountAskedConverted;
	private TextView txtDoYou;
	private TextView txtNotEnough;
	private Button btnOpenWallet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bitcoin_request_received);

		txtBalanceBTC = (TextView) findViewById(R.id.txtBalanceBTC);
		txtBalanceConverted = (TextView) findViewById(R.id.txtBalanceConverted);
		txtAmountAskedInBTC = (TextView) findViewById(R.id.txtAmountAskedInBTC);
		txtAmountAskedConverted = (TextView) findViewById(R.id.txtAmountAskedConverted);
		txtDoYou = (TextView) findViewById(R.id.txtDoYou);
		txtNotEnough = (TextView) findViewById(R.id.txtNotEnough);


		final CurrenciesAdapter adapter = new CurrenciesAdapter(this, R.layout.currency_spinner_item_dark, snitcoin.currencyCodes());
		spnCurrencies = (Spinner) findViewById(R.id.spnCurrencies);
		spnCurrencies.setAdapter(adapter);
		spnCurrencies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String code = (String) parent.getItemAtPosition(position);
				ExchangeRate rate = snitcoin.rateBy(code);
				snitcoin.setDefault(rate);
				adapter.notifyDataSetChanged();
				prefs.edit().putString(RATE, rate.code).apply();
				updateCurrencyAndAmmountInBTC();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(BitcoinRequestReceivedActivity.this, "Transaction canceled", Toast.LENGTH_SHORT).show();
				finish();
			}
		});

		btnSend = (Button) findViewById(R.id.btnSend);
		btnSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(BitcoinRequestReceivedActivity.this, "Bitcoin sent", Toast.LENGTH_SHORT).show();
				finish();
			}
		});

		btnOpenWallet = (Button) findViewById(R.id.btnOpenWallet);
		btnOpenWallet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(BitcoinRequestReceivedActivity.this, SnitcoinActivity.class));
				finish();
			}
		});

		edtYes = (EditText) findViewById(R.id.edtYes);
		edtYes.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				btnSend.setEnabled(s.toString().toLowerCase().equals("yes"));
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateCurrencyAndAmmountInBTC();
	}

	private void updateCurrencyAndAmmountInBTC() {
		prefs = getSharedPreferences(PREF_EXCHANGE_RATES, MODE_PRIVATE);
		String currency = prefs.getString(RATE, "USD");
		BigDecimal asked = BigDecimal.valueOf(1.0265478);
		BigDecimal balance = snitcoin.balanceInBTC();
		txtBalanceBTC.setText(balance + " BTC");
		txtBalanceConverted.setText("" + snitcoin.balanceConverted());
		txtAmountAskedInBTC.setText(asked + " BTC");
		txtAmountAskedConverted.setText("(" + snitcoin.amountConverted(asked) + " " + currency + ")");

		txtDoYou.setVisibility(VISIBLE);
		edtYes.setVisibility(VISIBLE);
		btnSend.setVisibility(VISIBLE);
		btnOpenWallet.setVisibility(GONE);
		txtNotEnough.setVisibility(GONE);

		if (balance.compareTo(asked) == -1) {
			txtDoYou.setVisibility(GONE);
			edtYes.setVisibility(GONE);
			btnSend.setVisibility(GONE);
			btnOpenWallet.setVisibility(VISIBLE);
			txtNotEnough.setVisibility(VISIBLE);
		}


	}

}
