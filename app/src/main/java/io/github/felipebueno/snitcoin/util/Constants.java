package io.github.felipebueno.snitcoin.util;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.TestNet3Params;

public class Constants {

	public static final String TAG = "SNITEST";
	public static NetworkParameters params = TestNet3Params.get();
	//	public static NetworkParameters params = RegTestParams.get();
	public static final String APP_NAME = "Snitcoin";

	public static final char CHAR_SPACE = ' ';
	public static final int ADDRESS_FORMAT_GROUP_SIZE = 4;
	public static final int ADDRESS_FORMAT_LINE_SIZE = 12;

}
