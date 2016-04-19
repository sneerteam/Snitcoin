package io.github.felipebueno.snitcoin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import sneer.android.Message;
import sneer.android.PartnerSession;

public class BitcoinRequestActivity extends AppCompatActivity {

	private PartnerSession session;
	private Button btnRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bitcoin_request);

		btnRequest = (Button) findViewById(R.id.btnSend);
		btnRequest.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		startSession();
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
