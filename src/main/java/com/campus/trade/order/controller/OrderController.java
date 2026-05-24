package com.campus.trade.order.controller;

import com.campus.trade.common.ApiResult;
import com.campus.trade.order.entity.Order;
import com.campus.trade.order.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService os) { orderService = os; }

    @PostMapping("/create")
    public ApiResult<Order> create(@RequestBody Order order, Authentication auth) {
        order.setBuyerId((Long) auth.getPrincipal());
        return orderService.create(order);
    }

    @GetMapping("/buy")
    public ApiResult<Page<Order>> buyOrders(Authentication auth,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "20") int size,
                                            @RequestParam(required = false) String status) {
        return orderService.myBuyOrders((Long) auth.getPrincipal(), page, size, status);
    }

    @GetMapping("/sell")
    public ApiResult<Page<Order>> sellOrders(Authentication auth,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int size) {
        return orderService.mySellOrders((Long) auth.getPrincipal(), page, size);
    }

    @PutMapping("/{id}/status")
    public ApiResult<Order> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return orderService.updateStatus(id, status);
    }
}
