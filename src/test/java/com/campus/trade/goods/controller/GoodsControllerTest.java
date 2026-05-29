package com.campus.trade.goods.controller;

import com.campus.trade.common.ApiResult;
import com.campus.trade.goods.entity.Goods;
import com.campus.trade.goods.service.GoodsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoodsControllerTest {

    @Mock
    private GoodsService goodsService;

    @InjectMocks
    private GoodsController goodsController;

    private Goods testGoods;

    @BeforeEach
    void setUp() {
        testGoods = Goods.builder()
                .id(1L)
                .userId(100L)
                .title("测试商品")
                .description("描述")
                .category("教材教辅")
                .price(new BigDecimal("50.00"))
                .images("/uploads/test.jpg")
                .status("onsale")
                .build();
    }

    @Test
    void list_shouldCallServiceAndReturnGoods() {
        when(goodsService.list(0, 20, null, null))
                .thenReturn(ApiResult.success(null));

        ApiResult<?> result = goodsController.list(0, 20, null, null);

        assertEquals(200, result.getCode());
        verify(goodsService).list(0, 20, null, null);
    }

    @Test
    void detail_shouldReturnGoodsWithImages() {
        when(goodsService.detail(1L)).thenReturn(ApiResult.success(testGoods));

        ApiResult<Goods> result = goodsController.detail(1L);

        assertEquals(200, result.getCode());
        assertEquals("测试商品", result.getData().getTitle());
        assertEquals("教材教辅", result.getData().getCategory());
        assertEquals("/uploads/test.jpg", result.getData().getImages());
    }

    @Test
    void detail_notFound_shouldReturn404() {
        when(goodsService.detail(999L)).thenReturn(ApiResult.error(404, "商品不存在"));

        ApiResult<Goods> result = goodsController.detail(999L);

        assertEquals(404, result.getCode());
    }

    @Test
    void pending_shouldCallService() {
        when(goodsService.listPending(0, 20)).thenReturn(ApiResult.success(null));

        ApiResult<?> result = goodsController.pendingGoods(0, 20);

        assertEquals(200, result.getCode());
        verify(goodsService).listPending(0, 20);
    }

    @Test
    void review_approved_shouldCallServiceWithTrue() {
        testGoods.setStatus("onsale");
        when(goodsService.review(1L, true)).thenReturn(ApiResult.success(testGoods));

        ApiResult<Goods> result = goodsController.reviewGoods(1L, true);

        assertEquals(200, result.getCode());
        assertEquals("onsale", result.getData().getStatus());
        verify(goodsService).review(1L, true);
    }

    @Test
    void review_rejected_shouldCallServiceWithFalse() {
        testGoods.setStatus("rejected");
        when(goodsService.review(1L, false)).thenReturn(ApiResult.success(testGoods));

        ApiResult<Goods> result = goodsController.reviewGoods(1L, false);

        assertEquals(200, result.getCode());
        assertEquals("rejected", result.getData().getStatus());
        verify(goodsService).review(1L, false);
    }

    @Test
    void review_notFound_shouldReturn404() {
        when(goodsService.review(999L, true)).thenReturn(ApiResult.error(404, "商品不存在"));

        ApiResult<Goods> result = goodsController.reviewGoods(999L, true);

        assertEquals(404, result.getCode());
        assertEquals("商品不存在", result.getMessage());
    }
}