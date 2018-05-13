package com.sezayir.service;

import java.time.Instant;
import java.util.DoubleSummaryStatistics;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sezayir.entity.Transaction;
import com.sezayir.util.Utility;

@Service
public class TransactionService {

	public ConcurrentHashMap<Instant, Transaction> startTransaction() {

		ConcurrentHashMap<Instant, Transaction> concurentMap = new ConcurrentHashMap<Instant, Transaction>();

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
		Runnable task = () -> {
			double amount = Utility.generateRandomDouble();
			Instant instant = Instant.now();
			long timeStampMillis = instant.toEpochMilli();

			Transaction transatcion = new Transaction();
			transatcion.setAmount(amount);
			transatcion.setTimestamp(timeStampMillis);
			concurentMap.put(instant, transatcion);
			System.out.println(Thread.currentThread().getName() + " " + transatcion);

		};

		executor.scheduleWithFixedDelay(task, 0, Utility.generateRandomInt(), TimeUnit.SECONDS);

		executor.scheduleWithFixedDelay(task, 0, Utility.generateRandomInt(), TimeUnit.SECONDS);
		return concurentMap;

	}

	public String displayStatistc(ConcurrentHashMap<Instant, Transaction> concurentMap) {
		// Map -> Stream -> Filter -> Map
		ConcurrentMap<Instant, Transaction> collect = concurentMap.entrySet().stream()
				.filter(x -> Utility.isLastMinute(x.getKey()))
				.collect(Collectors.toConcurrentMap(x -> x.getKey(), x -> x.getValue()));

		cleanPreviousMinutesTransactions(concurentMap, collect);

		DoubleSummaryStatistics statistic = Utility.getSummary(collect);

		return statistic.toString();
	}

	private void cleanPreviousMinutesTransactions(ConcurrentHashMap<Instant, Transaction> concurentMap, ConcurrentMap<Instant, Transaction> collect) {
		// clean oldest transactions for performance
		concurentMap.clear();
		// put last minute transactions
		concurentMap.putAll(collect);
		System.out.println("Clean previous minute transaction. Map Size:" + concurentMap.size());
	}
}
