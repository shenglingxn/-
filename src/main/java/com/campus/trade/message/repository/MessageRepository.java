package com.campus.trade.message.repository;

import com.campus.trade.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByToUserIdOrderByCreatedAtDesc(Long toUserId, Pageable pageable);
    @Query("SELECT m FROM Message m WHERE (m.fromUserId = :u1 AND m.toUserId = :u2) OR (m.fromUserId = :u2 AND m.toUserId = :u1) ORDER BY m.createdAt ASC")
    List<Message> findConversation(@Param("u1") Long u1, @Param("u2") Long u2);
    @Query("SELECT COUNT(m) FROM Message m WHERE m.toUserId = :uid AND m.isRead = 0")
    long countUnread(@Param("uid") Long uid);
}
