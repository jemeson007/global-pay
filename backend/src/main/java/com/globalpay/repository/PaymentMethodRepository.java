package com.globalpay.repository;

import com.globalpay.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, String> {
    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.user.id = :userId ORDER BY pm.isDefault DESC, pm.createdAt DESC")
    List<PaymentMethod> findByUserId(@Param("userId") String userId);
    
    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.user.id = :userId AND pm.isDefault = true")
    Optional<PaymentMethod> findDefaultByUserId(@Param("userId") String userId);
    
    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.user.id = :userId AND pm.isActive = true")
    List<PaymentMethod> findActiveByUserId(@Param("userId") String userId);
}
