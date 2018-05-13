package com.sezayir.controller;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sezayir.entity.Transaction;
import com.sezayir.service.TransactionService;

/**
 * 
 * @author dagtekin
 *
 */

@RestController
public class TransactionController {

	@Autowired
	TransactionService service;

	ConcurrentHashMap<Instant, Transaction> concurentMap;

	@RequestMapping("/transactions")
	public String startTransaction() {
		concurentMap = service.startTransaction();
		return "Success.Transaction is started.";

	}

	@RequestMapping("/statistics")
	public String displayLatestTransactions() {

		return service.displayStatistc(concurentMap);

	}

}
