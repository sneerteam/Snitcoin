package io.github.felipebueno.snitcoin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

import static io.github.felipebueno.snitcoin.SnitcoinApp.*;
import static io.github.felipebueno.snitcoin.Utils.PREF_EXCHANGE_RATES;
import static io.github.felipebueno.snitcoin.Utils.RATE;

public class BitcoinRequestReceivedActivity extends AppCompatActivity {

	private EditText txtYes;
	private Button btnSend;
	private Button btnCancel;
	private Button btnCurrencies;
	private SharedPreferences prefs;
	private TextView txtAmount;
	private TextView txtThisPerson;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bitcoin_request_received);

		txtAmount = (TextView) findViewById(R.id.txtAmount);
		txtThisPerson = (TextView) findViewById(R.id.txtThisPerson);

		btnCurrencies = (Button) findViewById(R.id.btnCurrencies);
		btnCurrencies.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent().setClass(BitcoinRequestReceivedActivity.this, ExchangeRatesActivity.class));
			}
		});

		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(BitcoinRequestReceivedActivity.this, "Transaction canceled", Toast.LENGTH_SHORT).show();
				finish();
			}
		});

		btnSend = (Button) findViewById(R.id.btnSend);
		btnSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(BitcoinRequestReceivedActivity.this, "Bitcoin sent", Toast.LENGTH_SHORT).show();
				finish();
			}
		});

		txtYes = (EditText) findViewById(R.id.edtYes);
		txtYes.addTextChangedListener(new TextWatcher() {
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
		BigDecimal btc = BigDecimal.valueOf(0.074);
		btnCurrencies.setText(currency);
		txtAmount.setText("You have BTC "+ snitcoin.balanceInBTC() + " (" + snitcoin.balanceConverted() + " " + currency + ")");
		txtThisPerson.setText("This person is asking for " + btc + " BTC (" + snitcoin.ammountConverted(btc) + " " + currency + "). Do you want to send this amount?)");
	}

}
