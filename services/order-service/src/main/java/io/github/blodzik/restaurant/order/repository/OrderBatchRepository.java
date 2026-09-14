package io.github.blodzik.restaurant.order.repository;

import io.github.blodzik.restaurant.order.entity.OrderBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderBatchRepository extends JpaRepository<OrderBatch, Long> {
    Optional<OrderBatch> findByTableIdAndFiredAtIsNull(Long tableId);

    @Query("SELECT COALESCE(MAX(b.batchNumber), 0) FROM OrderBatch b WHERE b.tableId = :tableId")
    Integer findMaxBatchNumberForTable(@Param("tableId") Long tableId);
}
