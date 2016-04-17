package io.github.felipebueno.snitcoin.util;

import android.text.Editable;
import android.text.SpannableStringBuilder;

import javax.annotation.Nullable;

public class WalletUtils {

	public static Editable formatHash(final String address, final int groupSize, final int lineSize) {
		return formatHash(null, address, groupSize, lineSize, Constants.CHAR_SPACE);
	}

	public static Editable formatHash(@Nullable final String prefix, final String address, final int groupSize, final int lineSize, final char groupSeparator) {
		final SpannableStringBuilder builder = prefix != null ? new SpannableStringBuilder(prefix) : new SpannableStringBuilder();

		final int len = address.length();
		for (int i = 0; i < len; i += groupSize) {
			final int end = i + groupSize;
			final String part = address.substring(i, end < len ? end : len);

			builder.append(part);
			if (end < len) {
				final boolean endOfLine = lineSize > 0 && end % lineSize == 0;
				builder.append(endOfLine ? '\n' : groupSeparator);
			}
		}

		return builder;
	}

}
