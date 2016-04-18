package io.github.felipebueno.snitcoin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BitcoinRequestReceivedActivity extends AppCompatActivity {

	private EditText txtYes;
	private Button btnSend;
	private Button btnCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bitcoin_request_received);

		btnSend = (Button) findViewById(R.id.btnSend);
		btnSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(BitcoinRequestReceivedActivity.this, "Bitcoin sent", Toast.LENGTH_SHORT).show();
				finish();
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

}
