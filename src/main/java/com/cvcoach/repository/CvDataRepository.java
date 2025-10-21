package com.cvcoach.repository;

import com.cvcoach.model.CvDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for CV data persistence
 */
@Repository
public interface CvDataRepository extends JpaRepository<CvDataEntity, Long> {

    /**
     * Find the most recently created CV data entry
     *
     * @return Optional containing the latest CV data or empty if none exists
     */
    Optional<CvDataEntity> findTopByOrderByCreatedAtDesc();

    /**
     * Find the most recently updated CV data entry
     *
     * @return Optional containing the latest CV data or empty if none exists
     */
    Optional<CvDataEntity> findTopByOrderByUpdatedAtDesc();
}