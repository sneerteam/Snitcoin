package sneer.bitcoin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CurrenciesAdapter extends ArrayAdapter<String> {

	private final int layout;

	public CurrenciesAdapter(Context context, int layout, List<String> values) {
		super(context, layout, values);
		this.layout = layout;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View v = inflater.inflate(layout, parent, false);
		TextView txtCurrencyCode = (TextView) v.findViewById(R.id.text1);
		txtCurrencyCode.setText(getItem(position));

		return v;
	}

}
