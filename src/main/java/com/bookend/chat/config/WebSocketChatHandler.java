package com.bookend.chat.config;

import com.bookend.chat.domain.dto.ChatMessageDto;
import com.bookend.chat.domain.entity.ChatMessage;
import com.bookend.chat.domain.entity.ChatUser;
import com.bookend.chat.repository.ChatMessageRepository;
import com.bookend.chat.repository.ChatUserRepository;
import com.bookend.review.domain.entity.Review;
import com.bookend.review.repository.ReviewRepository;
import com.bookend.security.domain.entity.User;
import com.bookend.security.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final ObjectMapper mapper;

    // 채팅방 당 연결된 세션들 (roomId : {session1, session2})
    private final Map<Long, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    private final ChatUserRepository chatUserRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    // 소켓 연결 후
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("{} 연결", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);

        // payload -> chatMessageDto로 변환
        ChatMessageDto chatMessageDto = mapper.readValue(payload, ChatMessageDto.class);
        User user = userRepository.findById(chatMessageDto.getUserId()).orElse(null);
        if(user!=null){
            chatMessageDto.setUser(user);
            log.info("session {}", chatMessageDto.toString());

            Long roomId = chatMessageDto.getChatRoomId();
            // 해당 id의 방이 없으면 새로 추가
            if (!chatRoomSessionMap.containsKey(roomId)) {
                log.info("{} 채팅방을 세션 목록에 추가함.", roomId);
                chatRoomSessionMap.put(roomId, new HashSet<>());
            }
            Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(roomId); // 해당 방 가져오기

            // messageType이 ENTER이면 세션에 저장
            if (chatMessageDto.getMessageType().equals(ChatMessageDto.MessageType.ENTER)) {
                chatRoomSession.add(session);   // 해당 세션 추가
                chatRoomSessionMap.put(roomId, chatRoomSession); // 반영
            }

            // messageType이 TAlK이면 메세지 저장
            if (chatMessageDto.getMessageType() == ChatMessageDto.MessageType.TALK) {
                // userId와 chatId로 ChatUser 조회하기
                ChatUser chatUser = chatUserRepository.findByUserUserIdAndChatRoomChatRoomIdAndOutYn(chatMessageDto.getUserId(), chatMessageDto.getChatRoomId(), false);
                ChatMessage chatMessage = ChatMessage.builder()
                        .chatRoom(chatUser.getChatRoom())
                        .user(user)
                        .messageType(chatMessageDto.getMessageType())
                        .message(chatMessageDto.getMessage())
                        .build();
                chatMessageRepository.save(chatMessage);

                // 저장한 채팅을 다시 dto로 변경 (send_time은 db에 저장될 때 값이 입력되므로..)
                chatMessageDto = new ChatMessageDto(chatMessage);
            }

            // reviewId 추가
            Long bookId = chatMessageDto.getBookId();
            Review review = reviewRepository.findByUserUserIdAndBookBookId(chatMessageDto.getUserId(), bookId);
            if(review!=null) chatMessageDto.setReview(review);

            // 해당 채팅방에 있는 모든 세션에 메세지 전송
            sendMessageToChatRoom(chatMessageDto, chatRoomSession);
        }else{
            log.error("user is null");
        }
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        }catch(IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void sendMessageToChatRoom(ChatMessageDto chatMessageDto, Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.parallelStream().forEach(session -> sendMessage(session, chatMessageDto));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("{} 연결종료", session.getId());

        chatRoomSessionMap.forEach((roomId, chatRoomSession) -> {
            chatRoomSession.remove(session);
        });
    }
}
