package com.googlecode.openbox.common.algorithm;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Homogenizer {
	private static final Logger logger = LogManager.getLogger();

	public interface Action<T> {
		
		String getName();

		int getNum();

		T handle(final int index ,final int counter);

	}

	private static int[] getCounters(int num) {
		int[] counters = new int[num];
		for (int i = 0; i < num; i++) {
			counters[i] = 0;
		}
		return counters;

	}

	public static <T> List<T> homogenize(Action<T>[] actions) {

		int total = 0;
		int num = actions.length;
		for (int i = 0; i < num; i++) {
			total = total + actions[i].getNum();
		}
		List<T> result = new LinkedList<T>();
		int index = 0;
		int[] counters = getCounters(actions.length);
		while (index < total) {
			for (int i = 0; i < counters.length; i++) {
				Action<T> action = actions[i];
				if (counters[i] < action.getNum()) {
					result.add(action.handle(index , counters[i]));
					index++;
					counters[i]++;
					if(logger.isDebugEnabled()){
						logger.debug("homogenize add --> "+action.getName());
					}
				}
			}
		}
		return result;

	}
}
