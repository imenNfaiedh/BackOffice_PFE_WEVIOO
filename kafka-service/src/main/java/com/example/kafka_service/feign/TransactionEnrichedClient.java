//package com.example.kafka_service.feign;
//
////import com.example.transaction_service.dto.TransactionEnrichedDto;
////import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//
//
////@FeignClient(name = "Fraud-Detection-service", url = "http://localhost:8087")
//
//public interface TransactionEnrichedClient {
//
//    @GetMapping("/transaction/enrich/{transactionId}")
//     Object enrichTransaction(@PathVariable("transactionId") Long transactionId);
//}
