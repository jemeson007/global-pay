package com.globalpay.repository;

import com.globalpay.entity.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipientRepository extends JpaRepository<Recipient, String> {
    @Query("SELECT r FROM Recipient r WHERE r.user.id = :userId ORDER BY r.lastUsedAt DESC NULLS LAST")
    List<Recipient> findByUserId(@Param("userId") String userId);
    
    @Query("SELECT r FROM Recipient r WHERE r.user.id = :userId AND r.isActive = true")
    List<Recipient> findActiveByUserId(@Param("userId") String userId);
    
    Optional<Recipient> findByIdAndUserId(String id, String userId);
}
