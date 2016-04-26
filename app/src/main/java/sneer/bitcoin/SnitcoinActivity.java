package sneer.bitcoin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.bitcoinj.core.Address;

import sneer.bitcoin.core.ExchangeRate;

import static sneer.bitcoin.SnitcoinApp.snitcoin;
import static sneer.bitcoin.Utils.PREF_EXCHANGE_RATES;
import static sneer.bitcoin.Utils.RATE;
import static sneer.bitcoin.Utils.setDefaultRateFrom;
import static sneer.bitcoin.core.util.Constants.ADDRESS_FORMAT_GROUP_SIZE;
import static sneer.bitcoin.core.util.Constants.ADDRESS_FORMAT_LINE_SIZE;
import static sneer.bitcoin.core.util.WalletUtils.formatHash;

public class SnitcoinActivity extends AppCompatActivity {

	private static final String TAG = "SNITEST";

	private TextView txtBalanceBTC;
	private TextView txtBalanceConverted;
	private TextView txtAddress;
	private ImageView qrAddress;
	private Address address;
	private SharedPreferences prefs;
	private ImageButton btnShareAddress;
	private Spinner spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_snitcoin);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("");

		qrAddress = (ImageView) findViewById(R.id.addressQR);

		txtBalanceBTC = (TextView) findViewById(R.id.txtBalanceBTC);
		txtBalanceConverted = (TextView) findViewById(R.id.txtBalanceConverted);

		txtAddress = (TextView) findViewById(R.id.txtAddress);

		btnShareAddress = (ImageButton) findViewById(R.id.btnShareAddress);
		btnShareAddress.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String uri = snitcoin.requestUri();
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TEXT, uri);
				startActivity(Intent.createChooser(intent, getString(R.string.request_coins_share_dialog_title)));
			}
		});

		final CurrenciesAdapter adapter = new CurrenciesAdapter(this, R.layout.currency_spinner_item, snitcoin.currencyCodes());
		spinner = (Spinner) findViewById(R.id.spnBalanceConverted);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String code = (String) parent.getItemAtPosition(position);
				ExchangeRate rate = snitcoin.rateBy(code);
				snitcoin.setDefault(rate);
				adapter.notifyDataSetChanged();
				prefs.edit().putString(RATE, rate.code).apply();
				updateBalanceConvertedView();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		bitcoinjExperiments();
	}

	private void bitcoinjExperiments() {
		address = snitcoin.currentReceiveAddress();

		txtBalanceBTC.setText(snitcoin.balanceInBTC() + " BTC");
		qrAddress.setImageBitmap(snitcoin.qrCode(address.toString(), getResources().getDimensionPixelSize(R.dimen.bitmap_dialog_qr_size)));

		prefs = getSharedPreferences(PREF_EXCHANGE_RATES, MODE_PRIVATE);
		setDefaultRateFrom(prefs, this);

		updateBalanceConvertedView();
		updateAddressView();
	}

	private void updateBalanceConvertedView() {
		txtBalanceConverted.setText("" + snitcoin.balanceConverted());
	}

	private void updateAddressView() {
		txtAddress.setText(formatHash(address.toString(), ADDRESS_FORMAT_GROUP_SIZE, ADDRESS_FORMAT_LINE_SIZE));
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateBalanceConvertedView();
	}

}
