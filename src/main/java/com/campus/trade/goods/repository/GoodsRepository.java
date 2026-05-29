package com.campus.trade.goods.repository;

import com.campus.trade.goods.entity.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
    Page<Goods> findByStatusOrderByCreatedAtDesc(String status, Pageable pageable);
    Page<Goods> findByCategoryAndStatusOrderByCreatedAtDesc(String category, String status, Pageable pageable);
    Page<Goods> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
<<<<<<< HEAD
    @Query("SELECT g FROM Goods g, com.campus.trade.user.entity.User u WHERE g.userId = u.id AND g.status = 'onsale' AND (g.title LIKE %:kw% OR u.username LIKE %:kw%) ORDER BY g.createdAt DESC")
=======
    @Query("SELECT g FROM Goods g WHERE g.status = 'onsale' AND (g.title LIKE %:kw% OR g.description LIKE %:kw%) ORDER BY g.createdAt DESC")
>>>>>>> 464492e47d40cf433f66cc94246af5cfd132a45b
    Page<Goods> search(@Param("kw") String keyword, Pageable pageable);
    List<Goods> findByUserId(Long userId);
}
