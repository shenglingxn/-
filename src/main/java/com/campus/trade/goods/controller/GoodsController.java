package com.campus.trade.goods.controller;

import com.campus.trade.common.ApiResult;
import com.campus.trade.goods.entity.Goods;
import com.campus.trade.goods.service.GoodsService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {
    private final GoodsService goodsService;
    public GoodsController(GoodsService gs) { goodsService = gs; }

    @GetMapping("/list")
    public ApiResult<Page<Goods>> list(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int size,
                                       @RequestParam(required = false) String category,
                                       @RequestParam(required = false) String keyword) {
        return goodsService.list(page, size, category, keyword);
    }

    @GetMapping("/{id}")
    public ApiResult<Goods> detail(@PathVariable Long id) { return goodsService.detail(id); }

    @PostMapping("/create")
    public ApiResult<Goods> create(@RequestBody Goods goods, Authentication auth) {
        if (auth == null) return ApiResult.error(401, "请先登录");
        goods.setUserId((Long) auth.getPrincipal());
        return goodsService.create(goods);
    }

    @GetMapping("/mine")
    public ApiResult<List<Goods>> myGoods(Authentication auth) {
        if (auth == null) return ApiResult.error(401, "请先登录");
        return goodsService.myList((Long) auth.getPrincipal());
    }

    @GetMapping("/onsale")
    public ApiResult<Page<Goods>> onsale(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "50") int size) {
        return goodsService.listOnSale(page, size);
    }

    @PostMapping("/delist")
    public ApiResult<String> delist(@RequestParam Long goodsId) {
        return goodsService.delist(goodsId);
    }
}
