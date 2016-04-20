package io.github.felipebueno.snitcoin;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.bitcoinj.core.Address;

import io.github.felipebueno.core.Snitcoin;
import io.github.felipebueno.core.sims.SnitcoinSim;
import io.github.felipebueno.core.util.Currency;

import static io.github.felipebueno.core.util.Constants.*;
import static io.github.felipebueno.core.util.WalletUtils.formatHash;

public class SnitcoinActivity extends AppCompatActivity implements ClipboardManager.OnPrimaryClipChangedListener {

	private static final String TAG = "SNITEST";

	private TextView txtBalanceBTC;
	private TextView txtBalanceConverted;
	private TextView txtAddress;
	private ClipboardManager clipboardManager;
	private ImageView qrAddress;
	public static Snitcoin snitcoin;
	private Address address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_snitcoin);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("");

		snitcoin = new SnitcoinSim();

		clipboardManager = (ClipboardManager) SnitcoinActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);

		qrAddress = (ImageView) findViewById(R.id.addressQR);

		txtBalanceBTC = (TextView) findViewById(R.id.txtBalanceBTC);
		txtBalanceConverted = (TextView) findViewById(R.id.txtBalanceConverted);

		txtAddress = (TextView) findViewById(R.id.txtAddress);
		txtAddress.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Toast.makeText(SnitcoinActivity.this, "Bitcoin address copied to clipboard", Toast.LENGTH_SHORT).show();
				clipboardManager.setPrimaryClip(ClipData.newPlainText("Bitcoin address", "fklsdjfdkls80980"));
				return true;
			}
		});

		bitcoinjExperiments();
	}

	private void bitcoinjExperiments() {
		address = snitcoin.currentReceiveAddress();

		txtBalanceBTC.setText("BTC " + snitcoin.balanceInBTC());
		qrAddress.setImageBitmap(snitcoin.qrCode(address.toString(), getResources().getDimensionPixelSize(R.dimen.bitmap_dialog_qr_size)));

		updateBalanceConvertedView();
		updateAddressView();
	}

	private void updateBalanceConvertedView() {
		txtBalanceConverted.setText("USD " + snitcoin.balanceIn(Currency.USD));
	}

	private void updateAddressView() {
		txtAddress.setText(formatHash(address.toString(), ADDRESS_FORMAT_GROUP_SIZE, ADDRESS_FORMAT_LINE_SIZE));
	}

	@Override
	protected void onResume() {
		super.onResume();
		clipboardManager.addPrimaryClipChangedListener(this);
	}

	@Override
	public void onPause() {
		clipboardManager.removePrimaryClipChangedListener(this);
		super.onPause();
	}

	@Override
	public void onPrimaryClipChanged() {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_snitcoin, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
			case R.id.action_share_address:
				String uri = snitcoin.requestUri();
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TEXT, uri);
				startActivity(Intent.createChooser(intent, getString(R.string.request_coins_share_dialog_title)));
				return true;
			case R.id.action_exchange_rates:
				startActivity(new Intent().setClass(this, ExchangeRatesActivity.class));
		}
		return super.onOptionsItemSelected(item);
	}

}
