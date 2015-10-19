package com.why.tool.math;

/**
 * 
 * @author luzj
 * 
 */
public class MathUtil {

	/**
	 * 以mod取整
	 * 
	 * @param src
	 * @param mod
	 * @return
	 */
	public static int round(int src, int mod) {
		return round((float) src, mod);
	}

	public static int round(float src, int mod) {
		return Math.round(src / mod) * mod;
	}

	public static long round(double src, int mod) {
		return Math.round(src / mod) * mod;
	}

	public static long round(long src, int mod) {
		return round((double) src, mod);
	}

	/**
	 * MROUND rounds up, away from zero, if the remainder of dividing number by
	 * multiple is greater than or equal to half the value of multiple.
	 * 
	 * @param number
	 *            is the value to round.
	 * @param multiple
	 *            is the multiple to which you want to round number.
	 * @return Returns a number rounded to the desired multiple.
	 */
	public static float mround(double number, float multiple) {
		if (number * multiple < 0)
			throw new IllegalArgumentException("Arguments hava different signs!");
		return Math.round(number / multiple) * multiple;
	}
}
