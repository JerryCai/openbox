package com.googlecode.openbox.common.algorithm;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class MutexGroupSimulator {
	private static final Logger logger = LogManager.getLogger();

	public enum Result {
		INIT, OK, FAIL
	}

	private int[][] mutexMartix;

	private Result[] result;

	private int okCounter;
	private Result[] okResult;

	private int failCounter;
	private Result[] failResult;

	public MutexGroupSimulator(int[][] mutexMartix) {
		this.mutexMartix = mutexMartix;
		int groupNum = this.mutexMartix.length;
		this.okCounter = this.failCounter = 0;
		this.result = createInitResults(groupNum);
		this.okResult = createInitResults(groupNum);
		this.failResult = createInitResults(groupNum);
	}

	public static Result[] createInitResults(int groupNum) {

		Result[] result = new Result[groupNum];
		for (int i = 0; i < groupNum; i++) {
			result[i] = Result.INIT;
		}
		return result;
	}

	public void addOkGroup(int okGroupIndex) {
		okResult[okGroupIndex] = Result.OK;
		okCounter++;
		fillMutexResult(okGroupIndex);
	}

	public void addFailGroup(int failGroupIndex) {
		failResult[failGroupIndex] = Result.FAIL;
		failCounter++;

	}

	public abstract String getDisplay(Result result);

	public boolean getSimulateResult() {

		boolean result = simulate();
		if (logger.isInfoEnabled()) {
			logger.info("\nSimulator Result ==>[" + result
					+ "], You can refer below details \n" + this);
		}
		return result;

	}

	private boolean simulate() {
		for (int i = 0; i < okResult.length; i++) {
			if (okResult[i] == Result.INIT) {
				okResult[i] = Result.FAIL;
			}
		}
		for (int i = 0; i < failResult.length; i++) {
			if (failResult[i] == Result.INIT) {
				failResult[i] = Result.OK;
			}
		}
		if (mutexMartix.length != (okCounter + failCounter)) {
			return false;
		}
		for (int i = 0; i < result.length; i++) {
			if (result[i] != okResult[i] || result[i] != failResult[i]) {
				return false;
			}
		}
		return true;
	}

	public int[] getMergerOks() {

		List<Integer> mergerOks = new LinkedList<Integer>();
		for (int i = 0; i < result.length; i++) {
			if (result[i] == Result.OK) {
				for (int j = 0; j < mutexMartix[i].length; j++) {
					boolean duplicated = false;
					for (Integer exists : mergerOks) {
						if (exists.intValue() == mutexMartix[i][j]) {
							duplicated = true;
							break;
						}
					}
					if (!duplicated) {
						mergerOks.add(mutexMartix[i][j]);
					}
				}
			}
		}

		int size = mergerOks.size();
		int[] mergers = new int[size];
		for (int i = 0; i < size; i++) {
			mergers[i] = mergerOks.get(i);
		}
		return mergers;
	}

	private void fillMutexResult(int okGroupIndex) {
		if (result[okGroupIndex] != Result.FAIL) {
			result[okGroupIndex] = Result.OK;
		}
		for (int i = 0; i < mutexMartix[okGroupIndex].length; i++) {
			for (int j = 0; j < mutexMartix.length; j++) {
				if (result[j] != Result.INIT) {
					continue;
				}

				for (int k = 0; k < mutexMartix[j].length; k++) {
					if (mutexMartix[j][k] == mutexMartix[okGroupIndex][i]) {
						// that means this group / row has Mutex conflict with
						// given okGroupIndex group/row
						result[j] = Result.FAIL;
						break;
					}
				}
			}
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(
				"\n=============================start==============================\nMutexGroupSimulator Status List Below :\n Martix[][]\n{\n");
		for (int i = 0; i < mutexMartix.length; i++) {
			sb.append("  {");
			for (int j = 0; j < mutexMartix[i].length; j++) {
				sb.append(mutexMartix[i][j]);
				if (j < mutexMartix[i].length - 1) {
					sb.append(',');
				}
			}
			sb.append("}");
			if (i < mutexMartix.length - 1) {
				sb.append(",\n");
			}
		}
		sb.append("\n}");
		appendResult(sb, okResult, "Received OK Result");
		appendResult(sb, failResult, "Received Failed Result");
		appendResult(sb, result, "Simulator Expected Result");
		sb.append("=============================end==============================");
		return sb.toString();
	}

	private void appendResult(StringBuilder sb, Result[] result, String title) {
		sb.append("\n");
		sb.append(title);
		sb.append(" is : \n-----------\n");
		for (int m = 0; m < result.length; m++) {
			sb.append(getDisplay(result[m])).append("\n");
		}
		sb.append("-----------\n");
	}

	public static void main(String... args) {
		int[][] mutexMartix = { { 1, 3, 49, 5, 3, 5, 6, 6, 3, 2 },
				{ 4, 35, 4, 3, 6, 7, 4, 3, 2 }, { 2, 6, 9, 0, 8, 6, 5 },
				{ 100, 200, 300, 499 }, { 200 }

		};
		MutexGroupSimulator mgs = new MutexGroupSimulator(mutexMartix) {

			@Override
			public String getDisplay(Result result) {

				return result.name();
			}

		};

		mgs.addOkGroup(2);
		mgs.addOkGroup(4);
		mgs.addFailGroup(1);
		mgs.addFailGroup(0);
		mgs.addFailGroup(3);
		mgs.getSimulateResult();
	}
}
