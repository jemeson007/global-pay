package com.globalpay.repository;

import com.globalpay.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Optional<Transaction> findByTransactionRef(String transactionRef);
    
    @Query("SELECT t FROM Transaction t WHERE (t.sender.id = :userId OR t.receiver.id = :userId) ORDER BY t.createdAt DESC")
    Page<Transaction> findByUserId(@Param("userId") String userId, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.sender.id = :userId ORDER BY t.createdAt DESC")
    Page<Transaction> findSentTransactions(@Param("userId") String userId, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.receiver.id = :userId ORDER BY t.createdAt DESC")
    Page<Transaction> findReceivedTransactions(@Param("userId") String userId, Pageable pageable);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.status = 'COMPLETED' AND t.createdAt >= :since")
    long countCompletedSince(@Param("since") LocalDateTime since);
}
