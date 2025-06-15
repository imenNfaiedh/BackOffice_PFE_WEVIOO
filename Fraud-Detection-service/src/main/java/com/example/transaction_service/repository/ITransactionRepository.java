package com.example.transaction_service.repository;

import com.example.transaction_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByBankAccount_User_UserIdAndTransactionDateAfter(Long userId, Date afterDate);

    List<Transaction> findByBankAccount_User_KeycloakId(String keycloakId);

    @Query(value = "SELECT TO_CHAR(t.FDS004_TRANSACTION_DATE, 'Month') AS month, COUNT(t.FDS004_TRANSACTION_ID) AS count " +
            "FROM FDS004T_TRANSACTION t " +
            "GROUP BY TO_CHAR(t.FDS004_TRANSACTION_DATE, 'Month'), EXTRACT(MONTH FROM t.FDS004_TRANSACTION_DATE) " +
            "ORDER BY EXTRACT(MONTH FROM t.FDS004_TRANSACTION_DATE)", nativeQuery = true)
    List<Object[]> countTransactionsByMonth();


}
