package com.why.tool.random;

import java.util.Random;

public class RandomUtil {

	private static ThreadLocal<Random> randoms = new ThreadLocal<Random>();

	private static Random getR() {
		Random random = randoms.get();
		if (random == null) {
			random = new Random();
			randoms.set(random);
		}
		return random;
	}
	
	/** 类似Excel的RANDBETWEEN(90,110)/100，默认保留2位小数 */
	public static float rand(float min, float max){
		int minInt = (int)(min*100);
		int maxInt = (int)(max*100);
		return randbetween(minInt, maxInt, 100);
	}
	
	public static float randbetween(int min, int max, float multiple){
		int number = getRandomNumber(min, max);
		return number / multiple;
	}

	public static int getRandomNumber(int max) {
		return getRandomNumber(0, max);
	}

	/** 闭区间包括了min和max */
	public static int getRandomNumber(int min, int max) {
		int result = getR().nextInt((max - min + 1) << 5);
		return (result >> 5) + min;
	}

	public static boolean getRandomWithPercentage(float percentage) {
		float nextFloat = getR().nextFloat();
		return nextFloat < percentage / 100f;
	}
	
	public static int getRandomChoiceWithPercentArr(int[] percentArr) {
		return getRandomChoiceWithArr(100, percentArr);
	}

	public static int getRandomChoiceWithPermillageArr(int[] permililageArr) {
		return getRandomChoiceWithArr(1000, permililageArr);
	}
	
	public static int getRandomChoiceTenThousandArr(int[] tenThousandArr){
		return getRandomChoiceWithArr(10000, tenThousandArr);
	}
	
	public static int getRandomChoiceWithArr(int expectSum, int[] arr){
		int sum = 0;
		for (int i : arr) {
			sum += i;
		}
		if (sum != expectSum)
			throw new RuntimeException("sum must be "+expectSum+"!");

		int r = getRandomNumber(expectSum-1);
		int total = 0;
		for (int i = 0; i < arr.length; i++) {
			total += arr[i];
			if (r < total) {
				return i;
			}
		}
		throw new RuntimeException("It can't be here!");
	}

	/**
	 * 数组中有可能为0的 需要忽略掉 [80,60,50,20]
	 */
	public static int getAverageChoiceIndex(float[] percentageArr) {
		float choice = randomFloat();
		float occurChoice = 1;
		float totalOccurChoice = 0;
		for (float percentage : percentageArr) {
			occurChoice *= (1 - percentage / 100);
			totalOccurChoice += percentage;
		}
		occurChoice = 1 - occurChoice;

		if (choice < occurChoice) {
			int index = 0;
			float currentIndexChoice = 0;
			for (float percentage : percentageArr) {
				currentIndexChoice += percentage / totalOccurChoice * occurChoice;
				if (choice < currentIndexChoice) {
					return index;
				}
				index++;
			}
		}
		return -1;
	}

	public static int getRandomChoice(int[] choiceArr) {
		int sum = 0;
		for (int i : choiceArr) {
			sum += i;
		}

		if (sum < 1)
			throw new IllegalArgumentException("seems the choice array is not correct");

		int r = getRandomNumber(sum - 1);
		int total = 0;
		for (int i = 0; i < choiceArr.length; i++) {
			total += choiceArr[i];
			if (r < total) {
				return i;
			}
		}

		throw new RuntimeException("It can't be here!");
	}
	
	/**
	 * 产生概率区间,对比下类似的randomBasedOnWeight方法
	 * @param ratios 概率权重数组，和可以不为100；但产生的概率的和为100
	 * @return
	 */
	public static int getRandomChoiceWithRatioArr(int[] ratios) {
		int sum = 0;
		for (int i : ratios) {
			sum += i;
		}

		int r = getRandomNumber(1, sum);
		int total = 0;
		for (int i = 0; i < ratios.length; i++) {
			total += ratios[i];
			if (r <= total) {
				return i;
			}
		}
		throw new RuntimeException("It can't be here!");
	}

	public static int randomBasedOnWeight(int[] weights) {
		int[] sumWeight = new int[weights.length + 1];
		sumWeight[0] = 0;
		for (int i = 0; i < weights.length; i++) {
			sumWeight[i + 1] = sumWeight[i] + weights[i];
		}
		int rand = getR().nextInt(sumWeight[sumWeight.length - 1]);
		for (int i = 0; i < sumWeight.length - 1; i++) {
			if (rand >= sumWeight[i] && rand < sumWeight[i + 1]) {
				return i;
			}
		}
		throw new RuntimeException("Shouldn't reach here!");
	}
	
	/** [0.0, 1.0) */
	public static float randomFloat() {
		return getR().nextFloat();
	}
	
	public static boolean randomBoolean() {
		return getR().nextBoolean();
	}
	
	public static long randomLong() {
		return getR().nextLong();
	}

}
