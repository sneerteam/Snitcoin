package io.github.felipebueno.snitcoin;

import android.app.Application;

import io.github.felipebueno.core.Snitcoin;
import io.github.felipebueno.core.sims.SnitcoinSim;

public class SnitcoinApp extends Application {

	public static Snitcoin snitcoin;

	@Override
	public void onCreate() {
		super.onCreate();

		snitcoin = new SnitcoinSim();
	}
}
