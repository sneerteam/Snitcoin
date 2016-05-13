package sneer.bitcoin;

import android.support.multidex.MultiDexApplication;

import sneer.bitcoin.core.Snitcoin;
import sneer.bitcoin.core.sims.SnitcoinSim;

public class SnitcoinApp extends MultiDexApplication {

	public static Snitcoin snitcoin;

	@Override
	public void onCreate() {
		super.onCreate();

		snitcoin = new SnitcoinSim();
	}
}
