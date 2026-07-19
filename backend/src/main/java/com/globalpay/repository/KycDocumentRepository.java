package com.globalpay.repository;

import com.globalpay.entity.KycDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KycDocumentRepository extends JpaRepository<KycDocument, String> {
    @Query("SELECT kd FROM KycDocument kd WHERE kd.user.id = :userId ORDER BY kd.createdAt DESC")
    List<KycDocument> findByUserId(@Param("userId") String userId);
    
    @Query("SELECT kd FROM KycDocument kd WHERE kd.user.id = :userId AND kd.documentType = :documentType ORDER BY kd.createdAt DESC")
    List<KycDocument> findByUserIdAndDocumentType(@Param("userId") String userId, @Param("documentType") KycDocument.DocumentType documentType);
    
    @Query("SELECT kd FROM KycDocument kd WHERE kd.user.id = :userId AND kd.verificationStatus = :status ORDER BY kd.verifiedAt DESC NULLS LAST")
    List<KycDocument> findByUserIdAndVerificationStatus(@Param("userId") String userId, @Param("status") KycDocument.VerificationStatus status);
}
