package com.campus.trade.goods.service;

import com.campus.trade.common.ApiResult;
import com.campus.trade.goods.entity.Goods;
import com.campus.trade.goods.repository.GoodsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoodsServiceTest {

    @Mock
    private GoodsRepository goodsRepository;

    @InjectMocks
    private GoodsService goodsService;

    private Goods testGoods;

    @BeforeEach
    void setUp() {
        testGoods = Goods.builder()
                .id(1L)
                .userId(100L)
                .title("测试商品")
                .description("测试描述")
                .category("教材教辅")
                .price(new BigDecimal("50.00"))
                .images("/uploads/test.jpg")
                .contactPhone("13800138000")
                .location("主校区")
                .build();
    }

    @Test
    void create_shouldSetStatusToPending_notOnsale() {
        when(goodsRepository.save(any(Goods.class))).thenReturn(testGoods);

        Goods input = Goods.builder().title("新商品").price(new BigDecimal("30")).build();
        ApiResult<Goods> result = goodsService.create(input);

        ArgumentCaptor<Goods> captor = ArgumentCaptor.forClass(Goods.class);
        verify(goodsRepository).save(captor.capture());
        assertEquals("pending", captor.getValue().getStatus());
        assertEquals(200, result.getCode());
    }

    @Test
    void create_shouldPreserveImagesField() {
        String images = "/uploads/img1.jpg,/uploads/img2.jpg";
        Goods input = Goods.builder()
                .title("带图片商品")
                .price(new BigDecimal("100"))
                .images(images)
                .build();

        when(goodsRepository.save(any(Goods.class))).thenReturn(input);

        ApiResult<Goods> result = goodsService.create(input);

        ArgumentCaptor<Goods> captor = ArgumentCaptor.forClass(Goods.class);
        verify(goodsRepository).save(captor.capture());
        assertEquals(images, captor.getValue().getImages());
        assertEquals("pending", captor.getValue().getStatus());
        assertEquals(200, result.getCode());
    }

    @Test
    void review_approved_shouldSetStatusToOnsale() {
        testGoods.setStatus("pending");
        when(goodsRepository.findById(1L)).thenReturn(Optional.of(testGoods));
        when(goodsRepository.save(any(Goods.class))).thenReturn(testGoods);

        ApiResult<Goods> result = goodsService.review(1L, true);

        assertEquals(200, result.getCode());
        ArgumentCaptor<Goods> captor = ArgumentCaptor.forClass(Goods.class);
        verify(goodsRepository).save(captor.capture());
        assertEquals("onsale", captor.getValue().getStatus());
    }

    @Test
    void review_rejected_shouldSetStatusToRejected() {
        testGoods.setStatus("pending");
        when(goodsRepository.findById(1L)).thenReturn(Optional.of(testGoods));
        when(goodsRepository.save(any(Goods.class))).thenReturn(testGoods);

        ApiResult<Goods> result = goodsService.review(1L, false);

        assertEquals(200, result.getCode());
        ArgumentCaptor<Goods> captor = ArgumentCaptor.forClass(Goods.class);
        verify(goodsRepository).save(captor.capture());
        assertEquals("rejected", captor.getValue().getStatus());
    }

    @Test
    void review_shouldKeepImagesAfterApproval() {
        testGoods.setStatus("pending");
        testGoods.setImages("/uploads/keep.jpg");
        when(goodsRepository.findById(1L)).thenReturn(Optional.of(testGoods));
        when(goodsRepository.save(any(Goods.class))).thenReturn(testGoods);

        ApiResult<Goods> result = goodsService.review(1L, true);

        assertEquals(200, result.getCode());
        ArgumentCaptor<Goods> captor = ArgumentCaptor.forClass(Goods.class);
        verify(goodsRepository).save(captor.capture());
        assertEquals("/uploads/keep.jpg", captor.getValue().getImages());
    }

    @Test
    void review_notFound_shouldReturn404() {
        when(goodsRepository.findById(999L)).thenReturn(Optional.empty());

        ApiResult<Goods> result = goodsService.review(999L, true);

        assertEquals(404, result.getCode());
        assertEquals("商品不存在", result.getMessage());
        verify(goodsRepository, never()).save(any());
    }

    @Test
    void review_alreadyOnsale_shouldBeReplaceable() {
        testGoods.setStatus("onsale");
        when(goodsRepository.findById(1L)).thenReturn(Optional.of(testGoods));
        when(goodsRepository.save(any(Goods.class))).thenReturn(testGoods);

        ApiResult<Goods> result = goodsService.review(1L, false);

        assertEquals(200, result.getCode());
        ArgumentCaptor<Goods> captor = ArgumentCaptor.forClass(Goods.class);
        verify(goodsRepository).save(captor.capture());
        assertEquals("rejected", captor.getValue().getStatus());
    }

    @Test
    void list_shouldOnlyShowOnsaleGoods() {
        Page<Goods> mockPage = new PageImpl<>(Collections.singletonList(testGoods));
        when(goodsRepository.findByStatusOrderByCreatedAtDesc(eq("onsale"), any(Pageable.class)))
                .thenReturn(mockPage);

        ApiResult<Page<Goods>> result = goodsService.list(0, 20, null, null);

        assertEquals(200, result.getCode());
        verify(goodsRepository).findByStatusOrderByCreatedAtDesc(eq("onsale"), any(Pageable.class));
        verify(goodsRepository, never()).search(anyString(), any());
    }

    @Test
    void list_withKeyword_shouldUseSearchQuery() {
        Page<Goods> mockPage = new PageImpl<>(Collections.emptyList());
        when(goodsRepository.search(eq("测试"), any(Pageable.class))).thenReturn(mockPage);

        ApiResult<Page<Goods>> result = goodsService.list(0, 20, null, "测试");

        assertEquals(200, result.getCode());
        verify(goodsRepository).search(eq("测试"), any(Pageable.class));
    }

    @Test
    void listPending_shouldQueryPendingStatus() {
        Page<Goods> mockPage = new PageImpl<>(Collections.singletonList(testGoods));
        when(goodsRepository.findByStatusOrderByCreatedAtDesc(eq("pending"), any(Pageable.class)))
                .thenReturn(mockPage);

        ApiResult<Page<Goods>> result = goodsService.listPending(0, 20);

        assertEquals(200, result.getCode());
        verify(goodsRepository).findByStatusOrderByCreatedAtDesc(eq("pending"), any(Pageable.class));
    }

    @Test
    void detail_shouldReturnGoods() {
        when(goodsRepository.findById(1L)).thenReturn(Optional.of(testGoods));

        ApiResult<Goods> result = goodsService.detail(1L);

        assertEquals(200, result.getCode());
        assertEquals(testGoods, result.getData());
    }
}