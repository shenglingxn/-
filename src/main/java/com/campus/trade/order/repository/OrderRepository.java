package com.campus.trade.order.repository;

import com.campus.trade.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByBuyerIdOrderByCreatedAtDesc(Long buyerId, Pageable pageable);
    Page<Order> findBySellerIdOrderByCreatedAtDesc(Long sellerId, Pageable pageable);
    Page<Order> findByBuyerIdAndStatusOrderByCreatedAtDesc(Long buyerId, String status, Pageable pageable);
    boolean existsByGoodsId(Long goodsId);
}
