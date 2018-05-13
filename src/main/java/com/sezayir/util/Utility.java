package com.sezayir.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import com.sezayir.entity.Transaction;

public class Utility {

	public static double generateRandomDouble() {
		double number = Math.random() * 100 + 1;
		double roundedNumber = (double) Math.round(number * 100) / 100;

		return roundedNumber;
	}
	
	public static int generateRandomInt() {
		return new Random().nextInt(5)+1;
	}

	public static boolean isLastMinute(Instant preiviousInstance) {
		Instant currentInstant = Instant.now();
		Duration between = Duration.between(currentInstant, preiviousInstance);
		long absoluteResult = between.abs().toMinutes();
		return absoluteResult == 0 ? true : false;
	}

	public static DoubleSummaryStatistics getSummary(ConcurrentMap<Instant, Transaction> transactionMap) {

		List<Transaction> transactionsList = new ArrayList<>();
		transactionsList.addAll(transactionMap.values());

		DoubleSummaryStatistics statistic = transactionsList.stream()
				.collect(Collectors.summarizingDouble(Transaction::getAmount));

		statistic.getCount();
		statistic.getMin();
		statistic.getMax();
		statistic.getSum();
		statistic.getAverage();
		return statistic;
	}
	

}
