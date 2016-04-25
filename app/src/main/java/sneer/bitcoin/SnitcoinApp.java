package sneer.bitcoin;

import android.app.Application;

import sneer.bitcoin.core.Snitcoin;
import sneer.bitcoin.core.sims.SnitcoinSim;

public class SnitcoinApp extends Application {

	public static Snitcoin snitcoin;

	@Override
	public void onCreate() {
		super.onCreate();

		snitcoin = new SnitcoinSim();
	}
}
