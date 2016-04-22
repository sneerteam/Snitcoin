package io.github.felipebueno.snitcoin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;

import sneer.android.Message;
import sneer.android.PartnerSession;

import static io.github.felipebueno.snitcoin.Utils.PREF_EXCHANGE_RATES;
import static io.github.felipebueno.snitcoin.Utils.RATE;

public class BitcoinRequestActivity extends AppCompatActivity {

	private PartnerSession session;
	private Button btnRequest;
	private SharedPreferences prefs;
	private Button btnCurrencies;
	private TextView txtAmountInBitcoins;
	private EditText edtAmount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bitcoin_request);

		btnCurrencies = (Button) findViewById(R.id.btnCurrencies);

		btnCurrencies.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent().setClass(BitcoinRequestActivity.this, ExchangeRatesActivity.class));
			}
		});

		btnRequest = (Button) findViewById(R.id.btnRequest);
		btnRequest.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
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

		updateCurrencyAndAmmountInBTC();

//		startSession();
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateCurrencyAndAmmountInBTC();
	}

	private void updateCurrencyAndAmmountInBTC() {
		prefs = getSharedPreferences(PREF_EXCHANGE_RATES, MODE_PRIVATE);
		btnCurrencies.setText(prefs.getString(RATE, "USD"));

		txtAmountInBitcoins.setText("(0.000000)");

		String ammountStr = edtAmount.getText().toString();
		if (TextUtils.isEmpty(ammountStr)) return;

		Double ammountToConvert = Double.valueOf(ammountStr);
		BigDecimal ammountInBTC = SnitcoinApp.snitcoin.ammountInBTC(ammountToConvert);

		txtAmountInBitcoins.setText("(" + ammountInBTC + ")");
	}

	private void startSession() {
		session = PartnerSession.join(this, new PartnerSession.Listener() {
			@Override
			public void onMessage(Message message) {
				//Called for every message sent by you and by your partner.
			}

			@Override
			public void onUpToDate() {
				//Called when there are no more messages pending in the session.
			}
		});
	}

}
