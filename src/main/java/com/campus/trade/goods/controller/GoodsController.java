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
        goods.setUserId((Long) auth.getPrincipal());
        return goodsService.create(goods);
    }

    @GetMapping("/mine")
    public ApiResult<List<Goods>> myGoods(Authentication auth) {
        return goodsService.myList((Long) auth.getPrincipal());
    }
<<<<<<< HEAD

    @GetMapping("/pending")
    public ApiResult<Page<Goods>> pendingGoods(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "20") int size) {
        return goodsService.listPending(page, size);
    }

    @PostMapping("/review")
    public ApiResult<Goods> reviewGoods(@RequestParam Long goodsId,
                                        @RequestParam boolean approved) {
        return goodsService.review(goodsId, approved);
    }

    @GetMapping("/onsale")
    public ApiResult<Page<Goods>> onsaleGoods(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "50") int size) {
        return goodsService.listOnsale(page, size);
    }

    @PostMapping("/delist")
    public ApiResult<?> delistGoods(@RequestParam Long goodsId) {
        return goodsService.delist(goodsId);
    }
=======
>>>>>>> 464492e47d40cf433f66cc94246af5cfd132a45b
}
