/**
 * 
 */
package com.crawljax.web.jatyta.utils;

import java.util.Random;

/**
 * @author mgimenez Class for static math methods for support.
 */
public class MathUtils {
	/**
	 * Returns a random long
	 * 
	 * @param rng
	 *            the {@link Random} object to generate the result;
	 * @param n
	 *            The long value of the roof.
	 * @return The random long value between 0 and n.
	 */
	public static long nextLong(Random rng, long n) {
		if (n <= 0)
			throw new IllegalArgumentException("n must be positive");

		if ((n & -n) == n) // i.e., n is a power of 2
			return (long) ((n * (long) rng.nextInt(31)) >> 31);

		long bits, val;
		do {
			bits = (rng.nextLong() << 1) >>> 1;
			val = bits % n;
		} while (bits - val + (n - 1) < 0L);
		return val;
	}
}
