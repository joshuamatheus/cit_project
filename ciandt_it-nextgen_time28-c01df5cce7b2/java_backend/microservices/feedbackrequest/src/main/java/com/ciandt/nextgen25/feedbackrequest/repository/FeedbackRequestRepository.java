package com.ciandt.nextgen25.feedbackrequest.repository;

import com.ciandt.nextgen25.feedbackrequest.entity.FeedbackRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FeedbackRequestRepository extends JpaRepository<FeedbackRequest, UUID> {

    List<FeedbackRequest> findByRequesterId(Long requesterId);

    @Query("SELECT fr FROM FeedbackRequest fr JOIN fr.appraisers a WHERE a.email = :email")
    List<FeedbackRequest> findByAppraiserEmail(String email);

    @Query("SELECT fr FROM FeedbackRequest fr WHERE fr.approvedAt IS NOT NULL")
    List<FeedbackRequest> findAllCompleted();

    @Query("SELECT fr FROM FeedbackRequest fr WHERE fr.approvedAt IS NULL")
    List<FeedbackRequest> findAllPending();

}