package org.tmsframework.util;

import java.util.UUID;

/**
 * 产生较短UUID,返回36进制的128位uuid
 * 
 * @author sam.zhang
 * 
 */
public final class ShortUUIDGenerator {

	public static String fromString(String name) {
		return toShortString(UUID.fromString(name));
	}

	public static String nameUUIDFromBytes(byte[] bytes) {
		return toShortString(UUID.nameUUIDFromBytes(bytes));
	}

	public static String randomUUID() {
		return toShortString(UUID.randomUUID());
	}

	public static String generate() {
		return randomUUID();
	}

	private static String toShortString(UUID u) {
		return UUIDtoString(u);
	}

	private static String UUIDtoString(UUID u) {
		long mostSigBits = u.getMostSignificantBits();
		long leastSigBits = u.getLeastSignificantBits();
		return (digits(mostSigBits >> 32) + digits(mostSigBits)
				+ digits(leastSigBits >> 32) + digits(
				leastSigBits));

	}

	private static String digits(long val) {
		return Long.toString((val & 4294967295L), 36);
	}
}
