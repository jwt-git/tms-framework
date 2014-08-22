package org.tmsframework.util;

import java.util.UUID;

/**
 * ����϶�UUID,����36���Ƶ�128λuuid
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
		return (digits(mostSigBits >> 32, 8) + digits(mostSigBits >> 16, 4)
				+ digits(mostSigBits, 4) + digits(leastSigBits >> 48, 4) + digits(
				leastSigBits, 12));
	}

	private static String digits(long val, int digits) {
		long hi = 1L << (digits * 4);
		return Long.toString(hi | (val & (hi - 1)), 36).substring(1);
	}
}
