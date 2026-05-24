package com.campus.trade.message.service;

import com.campus.trade.common.ApiResult;
import com.campus.trade.message.entity.Message;
import com.campus.trade.message.repository.MessageRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    public MessageService(MessageRepository mr) { messageRepository = mr; }

    public ApiResult<Message> send(Message msg) {
        return ApiResult.success(messageRepository.save(msg));
    }

    public ApiResult<List<Message>> conversation(Long userId1, Long userId2) {
        return ApiResult.success(messageRepository.findConversation(userId1, userId2));
    }

    public ApiResult<Long> unreadCount(Long userId) {
        return ApiResult.success(messageRepository.countUnread(userId));
    }
}
