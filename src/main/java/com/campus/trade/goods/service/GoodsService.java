package com.campus.trade.goods.service;

import com.campus.trade.common.ApiResult;
import com.campus.trade.goods.entity.Goods;
import com.campus.trade.goods.repository.GoodsRepository;
import com.campus.trade.order.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GoodsService {
    private final GoodsRepository goodsRepository;
    private final OrderRepository orderRepository;
    public GoodsService(GoodsRepository gr, OrderRepository or) { goodsRepository = gr; orderRepository = or; }

    public ApiResult<Page<Goods>> list(int page, int size, String category, String keyword) {
        var p = PageRequest.of(page, size);
        Page<Goods> result;
        if (keyword != null && !keyword.isEmpty()) {
            result = goodsRepository.search(keyword, p);
        } else if (category != null && !category.isEmpty()) {
            result = goodsRepository.findByCategoryAndStatusOrderByCreatedAtDesc(category, "onsale", p);
        } else {
            result = goodsRepository.findByStatusOrderByCreatedAtDesc("onsale", p);
        }
        return ApiResult.success(result);
    }

    public ApiResult<Goods> detail(Long id) {
        return goodsRepository.findById(id).map(ApiResult::success)
                .orElse(ApiResult.error(404, "商品不存在"));
    }

    public ApiResult<Goods> create(Goods goods) {
        goods.setStatus("pending");
        return ApiResult.success(goodsRepository.save(goods));
    }

    public ApiResult<List<Goods>> myList(Long userId) {
        return ApiResult.success(goodsRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, 100)).getContent());
    }

    public ApiResult<Page<Goods>> listPending(int page, int size) {
        return ApiResult.success(goodsRepository.findByStatusOrderByCreatedAtDesc("pending", PageRequest.of(page, size)));
    }

    public ApiResult<Goods> review(Long id, boolean approved) {
        return goodsRepository.findById(id).map(goods -> {
            goods.setStatus(approved ? "onsale" : "rejected");
            return ApiResult.success(goodsRepository.save(goods));
        }).orElse(ApiResult.error(404, "商品不存在"));
    }

    public ApiResult<Page<Goods>> listOnsale(int page, int size) {
        return ApiResult.success(goodsRepository.findByStatusOrderByCreatedAtDesc("onsale", PageRequest.of(page, size)));
    }

    public ApiResult<?> delist(Long goodsId) {
        if (!goodsRepository.existsById(goodsId)) {
            return ApiResult.error(404, "商品不存在");
        }
        Goods goods = goodsRepository.findById(goodsId).get();
        if (!"onsale".equals(goods.getStatus())) {
            return ApiResult.error(400, "该商品不在售状态");
        }
        if (orderRepository.existsByGoodsId(goodsId)) {
            return ApiResult.error(400, "该商品已有订单，无法下架");
        }
        goods.setStatus("delisted");
        goodsRepository.save(goods);
        return ApiResult.success("下架成功");
    }
}
