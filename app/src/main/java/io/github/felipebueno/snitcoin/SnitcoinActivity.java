package io.github.felipebueno.snitcoin;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.bitcoinj.core.AbstractWalletEventListener;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.uri.BitcoinURI;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.utils.Threading;

import java.util.concurrent.Executor;

import io.github.felipebueno.snitcoin.util.QrCode;
import io.github.felipebueno.snitcoin.util.WalletUtils;

import static io.github.felipebueno.snitcoin.util.Constants.ADDRESS_FORMAT_GROUP_SIZE;
import static io.github.felipebueno.snitcoin.util.Constants.ADDRESS_FORMAT_LINE_SIZE;
import static io.github.felipebueno.snitcoin.util.Constants.APP_NAME;
import static io.github.felipebueno.snitcoin.util.Constants.TAG;
import static io.github.felipebueno.snitcoin.util.Constants.params;

public class SnitcoinActivity extends AppCompatActivity implements ClipboardManager.OnPrimaryClipChangedListener {

	private static final String to = "mryzM1aqio4pJmKboFMHAG2R13ifucbStR";
	private static final String WALLET_FILE_PREFIX = APP_NAME.replaceAll("[^a-zA-Z0-9.-]", "_") + "-" + params.getPaymentProtocolId();

	private Address forwardingAddress;
	private WalletAppKit kit;
	private Handler handler;
	private TextView txtBalanceBTC;
	private TextView txtBalanceConverted;
	private TextView txtAddress;
	private ClipboardManager clipboardManager;
	private Address address;
	private ImageView qrAddress;
	private Wallet wallet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_snitcoin);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("");

		clipboardManager = (ClipboardManager) SnitcoinActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);

		qrAddress = (ImageView) findViewById(R.id.addressQR);

		txtBalanceBTC = (TextView) findViewById(R.id.txtBalanceBTC);
		txtBalanceConverted = (TextView) findViewById(R.id.txtBalanceConverted);

		txtAddress = (TextView) findViewById(R.id.txtAddress);
		txtAddress.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Toast.makeText(SnitcoinActivity.this, "Bitcoin address copied to clipboard", Toast.LENGTH_SHORT).show();
				clipboardManager.setPrimaryClip(ClipData.newPlainText("Bitcoin address", address.toString()));
				return true;
			}
		});

		bitcoinjExperiments();
	}

	private void bitcoinjExperiments() {
		BriefLogFormatter.init();

		handler = new Handler();

		Threading.USER_THREAD = new Executor() {
			@Override
			public void execute(Runnable runnable) {
				handler.post(runnable);
			}
		};

		try {
			forwardingAddress = new Address(params, to);
		} catch (AddressFormatException e) {
			e.printStackTrace();
		}

		kit = new WalletAppKit(params, this.getFilesDir(), WALLET_FILE_PREFIX) {
			@Override
			protected void onSetupCompleted() {
				if (wallet().getKeychainSize() < 1)
					wallet().importKey(new ECKey());
			}
		};
		kit.setDownloadListener(new SnitcoinDownloadProgressTracker());
		kit.setBlockingStartup(false);
		kit.setUserAgent(APP_NAME, "1.0");
		kit.setAutoSave(true);
		kit.startAsync();
		kit.awaitRunning();

		wallet = kit.wallet();
		address = wallet.currentReceiveAddress();

		Log.d(TAG, "kit.getBalance(): " + wallet.getBalance().toFriendlyString());
		txtBalanceBTC.setText(wallet.getBalance().toFriendlyString());

		final int size = getResources().getDimensionPixelSize(R.dimen.bitmap_dialog_qr_size);
		qrAddress.setImageBitmap(QrCode.bitmap(address.toString(), size));

		Log.d(TAG, "kit.currentAddress(): " + address);

		updateAddressView();

		wallet.addEventListener(new AbstractWalletEventListener() {
			@Override
			public void onCoinsReceived(Wallet w, Transaction tx, Coin prevBalance, Coin newBalance) {
				Log.d(TAG, "onCoinsReceived wallet: " + w);
				Log.d(TAG, "onCoinsReceived transaction: " + tx);
				Log.d(TAG, "onCoinsReceived prevBalance: " + prevBalance);
				Log.d(TAG, "onCoinsReceived newBalance: " + newBalance);

				address = wallet.freshReceiveAddress();
				updateAddressView();

				forwardReceivedCoinsExperiment(tx);
			}

			@Override
			public void onCoinsSent(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {
				Log.d(TAG, "onCoinsSent wallet: " + wallet);
				Log.d(TAG, "onCoinsSent transaction: " + tx);
				Log.d(TAG, "onCoinsSent prevBalance: " + prevBalance);
				Log.d(TAG, "onCoinsSent newBalance: " + newBalance);
			}

			@Override
			public void onWalletChanged(Wallet wallet) {
//				Log.d(TAG, "onWalletChanged wallet: " + wallet);
			}
		});
	}

	private void updateAddressView() {
		final CharSequence label = WalletUtils.formatHash(address.toString(), ADDRESS_FORMAT_GROUP_SIZE, ADDRESS_FORMAT_LINE_SIZE);
		txtAddress.setText(label);
	}

	private void forwardReceivedCoinsExperiment(Transaction tx) {
		// Send coins experiment
		Coin value = tx.getValueSentToMe(wallet);
		Log.d(TAG, "Forwarding " + value.toFriendlyString() + " BTC");
		// Now send the coins back! Send with a small fee attached to ensure rapid confirmation.
		final Coin amountToSend = value.subtract(Transaction.REFERENCE_DEFAULT_MIN_TX_FEE);
		Wallet.SendResult sendResult = null;
		try {
			sendResult = wallet.sendCoins(kit.peerGroup(), forwardingAddress, amountToSend);
		} catch (InsufficientMoneyException e) {
			Log.e(TAG, e.getMessage().toString());
		}
		Log.d(TAG, "Sending ...");
		// Register a callback that is invoked when the transaction has propagated across the network.
		// This shows a second style of registering ListenableFuture callbacks, it works when you don't
		// need access to the object the future returns.
		final Wallet.SendResult finalSendResult = sendResult;
		sendResult.broadcastComplete.addListener(new Runnable() {
			@Override
			public void run() {
				// The wallet has changed now, it'll get auto saved shortly or when the app shuts down.
				Log.d(TAG, "Sent coins onwards! Transaction hash is " + finalSendResult.tx.getHashAsString());
			}
		}, Threading.USER_THREAD);
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

		if (id == R.id.action_share_address) {
			String uri = requestUri();
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, uri);
			startActivity(Intent.createChooser(intent, getString(R.string.request_coins_share_dialog_title)));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private String requestUri() {
		Coin amount = wallet.getBalance().divide(10);
		String label = "Label Test";
		String msg = "The message goes here";
		return BitcoinURI.convertToBitcoinURI(address, amount, label, msg);
	}

}
