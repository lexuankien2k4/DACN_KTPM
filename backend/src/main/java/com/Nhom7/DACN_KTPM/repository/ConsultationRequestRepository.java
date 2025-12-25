package com.Nhom7.DACN_KTPM.repository;

import com.Nhom7.DACN_KTPM.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultationRequestRepository extends JpaRepository<Consultation, Long> {

    // SỬA: showroomId dùng Integer để khớp với SQL int
    List<Consultation> findByShowroomIdAndScheduledAtBetween(
            Integer showroomId, LocalDateTime start, LocalDateTime end);

    // SỬA: showroomId dùng Integer
    Optional<Consultation> findByShowroomIdAndScheduledAt(
            Integer showroomId, LocalDateTime scheduledAt);
}