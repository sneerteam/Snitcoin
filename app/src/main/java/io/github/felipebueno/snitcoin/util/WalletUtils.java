package io.github.felipebueno.snitcoin.util;

public class WalletUtils {

	public static String formatHash(String address, int groupSize, int lineSize) {
		StringBuilder builder = new StringBuilder();

		int len = address.length();
		for (int i = 0; i < len; i += groupSize) {
			int end = i + groupSize;
			String part = address.substring(i, end < len ? end : len);

			builder.append(part);
			if (end < len) {
				boolean endOfLine = lineSize > 0 && end % lineSize == 0;
				builder.append(endOfLine ? '\n' : Constants.CHAR_SPACE);
			}
		}

		return builder.toString();
	}

}
