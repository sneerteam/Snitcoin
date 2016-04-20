package io.github.felipebueno.snitcoin;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.github.felipebueno.core.ExchangeRate;

import static io.github.felipebueno.snitcoin.SnitcoinActivity.snitcoin;

public class ExchangeRatesActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exchange_rates);

		ExchangeRate rate = new ExchangeRate("USD", 436.98, true);

		ListAdapter adapter = new ExchangeRatesAdapter(this, snitcoin.exchangeRates());
		ListView theListView = (ListView) findViewById(R.id.lstExchangeRates);
		theListView.setAdapter(adapter);

		theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(ExchangeRatesActivity.this, "content->" + String.valueOf(parent.getItemAtPosition(position)) + "\nparent->" + parent + "\nview->" + view + "\nposition->" + position + "\nid->" + id, Toast.LENGTH_SHORT).show();
			}
		});
	}

	class ExchangeRatesAdapter extends ArrayAdapter<ExchangeRate> {

		public ExchangeRatesAdapter(Context context, List<ExchangeRate> values) {
			super(context, R.layout.row_layout_exchange_rates, values);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			View v = inflater.inflate(R.layout.row_layout_exchange_rates, parent, false);

			ExchangeRate rate = getItem(position);
			TextView exchangeRateRowCurrencyCode = (TextView) v.findViewById(R.id.exchangeRateRowCurrencyCode);
			TextView exchangeRateRowRateValue = (TextView) v.findViewById(R.id.exchangeRateRowRateValue);
			TextView exchangeRateRowDefault = (TextView) v.findViewById(R.id.exchangeRateRowDefault);

			exchangeRateRowCurrencyCode.setText(rate.code);
			exchangeRateRowRateValue.setText("" + rate.val);

			if (rate.isDefault)
				exchangeRateRowDefault.setText("(default)");
			else
				exchangeRateRowDefault.setText("");

			return v;
		}
	}
}
