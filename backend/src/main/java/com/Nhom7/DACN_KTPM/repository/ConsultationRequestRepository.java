package com.Nhom7.DACN_KTPM.repository;

import com.Nhom7.DACN_KTPM.entity.ConsultationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultationRequestRepository extends JpaRepository<ConsultationRequest, Integer> {

    List<ConsultationRequest> findByShowroomIdAndScheduledAtBetween(
            Integer showroomId, LocalDateTime start, LocalDateTime end);

    Optional<ConsultationRequest> findByShowroomIdAndScheduledAt(
            Integer showroomId, LocalDateTime scheduledAt);

    // (Chúng ta sẽ thêm các phương thức lọc (filter) phức tạp hơn sau nếu cần)
}