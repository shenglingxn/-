package com.campus.trade.order.service;

import com.campus.trade.common.ApiResult;
import com.campus.trade.order.entity.Order;
import com.campus.trade.order.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    public OrderService(OrderRepository or) { orderRepository = or; }

    public ApiResult<Order> create(Order order) {
        order.setOrderNo(UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        order.setStatus("pending");
        return ApiResult.success(orderRepository.save(order));
    }

    public ApiResult<Page<Order>> myBuyOrders(Long userId, int page, int size, String status) {
        var p = PageRequest.of(page, size);
        if (status != null && !status.isEmpty()) {
            return ApiResult.success(orderRepository.findByBuyerIdAndStatusOrderByCreatedAtDesc(userId, status, p));
        }
        return ApiResult.success(orderRepository.findByBuyerIdOrderByCreatedAtDesc(userId, p));
    }

    public ApiResult<Page<Order>> mySellOrders(Long userId, int page, int size) {
        return ApiResult.success(orderRepository.findBySellerIdOrderByCreatedAtDesc(userId, PageRequest.of(page, size)));
    }

    public ApiResult<Order> updateStatus(Long id, String status) {
        return orderRepository.findById(id).map(order -> {
            order.setStatus(status);
            return ApiResult.success(orderRepository.save(order));
        }).orElse(ApiResult.error(404, "订单不存在"));
    }
}
