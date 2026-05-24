package com.campus.trade.order.entity;

import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name = "tb_order")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_no", unique = true, length = 32) private String orderNo;
    @Column(name = "goods_id") private Long goodsId;
    @Column(name = "buyer_id") private Long buyerId;
    @Column(name = "seller_id") private Long sellerId;
    @Column(name = "total_price") private BigDecimal totalPrice;
    @Builder.Default private String status = "pending";
    @Column(name = "shipping_method", length = 50) private String shippingMethod;
    @Column(columnDefinition = "TEXT") private String remark;
    @Column(name = "created_at", updatable = false) private LocalDateTime createdAt;
    @Column(name = "updated_at") private LocalDateTime updatedAt;
    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
    @PreUpdate protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}
