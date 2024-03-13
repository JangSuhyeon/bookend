package com.bookend.chat.config;

import com.bookend.chat.domain.dto.ChatMessageDto;
import com.bookend.chat.domain.entity.ChatMessage;
import com.bookend.chat.domain.entity.ChatUser;
import com.bookend.chat.repository.ChatMessageRepository;
import com.bookend.chat.repository.ChatUserRepository;
import com.bookend.security.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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

    // 현재 연결된 웹소켓 세션들
    private final Set<WebSocketSession> sessions = new HashSet<>();

    // 채팅방 당 연결된 세션들 (roomId : {session1, session2})
    private final Map<Long, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    private final ChatUserRepository chatUserRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    // 소켓 연결 후
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("{} 연결됨", session.getId());
        sessions.add(session); // 연결된 세션 목록에 추가
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);

        // payload -> chatMessageDto로 변환
        ChatMessageDto chatMessageDto = mapper.readValue(payload, ChatMessageDto.class);
        chatMessageDto.setUser(userRepository.findById(chatMessageDto.getUserId()).orElse(null));
        log.info("session {}", chatMessageDto.toString());

        // Todo 이해 안됨 START
        Long roomId = chatMessageDto.getChatId();

        // 해당 id의 방이 없으면 새로 추가
        if (!chatRoomSessionMap.containsKey(roomId)) {
            chatRoomSessionMap.put(roomId, new HashSet<>());
        }
        Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(roomId); // 해당 방 가져오기

        // messageType이 ENTER 메세지 저장
        if (chatMessageDto.getMessageType().equals(ChatMessageDto.MessageType.ENTER)) {
            chatRoomSession.add(session); // 세션을 해당 방에 넣고
        }
        if (chatRoomSession.size() >= 3) {        // Todo 해당 방에 참석한 사람이 3명 이상이면
            removeClosedSession(chatRoomSession); // 현재 생성된 세션에 해당 방의 세션이 없으면 제거..?
        }
        // Todo 이해 안됨 END

        // // messageType이 QUIT 메세지 저장
        if (chatMessageDto.getMessageType().equals(ChatMessageDto.MessageType.QUIT)) {
            sessions.remove(session);
//            chatMessage.setMessage(chatMessage.getSender() + "님이 퇴장했습니다..");
//            sendToEachSocket(sessions,new TextMessage(objectMapper.writeValueAsString(chatMessage)) );
        }

        // messageType이 TAlK이면 메세지 저장
        if (chatMessageDto.getMessageType() == ChatMessageDto.MessageType.TALK) {

            // userId와 chatId로 ChatUser 조회하기
            ChatUser chatUser = chatUserRepository.findByUserIdAndChatId(chatMessageDto.getUserId(), chatMessageDto.getChatId());
            ChatMessage chatMessage = ChatMessage.builder()
                    .chatId(chatUser.getChatId())
                    .user(chatMessageDto.getUser())
                    .messageType(chatMessageDto.getMessageType())
                    .message(chatMessageDto.getMessage())
                    .firstEntry(chatMessageDto.getFirstEntry())
                    .build();
            chatMessageRepository.save(chatMessage);

            // 저장한 채팅을 다시 dto로 변경 (send_time은 db에 저장될 때 값이 입력되므로..)
            chatMessageDto = new ChatMessageDto(chatMessage);
        }

        // 해당 채팅방에 있는 모든 세션에 메세지 전송
        sendMessageToChatRoom(chatMessageDto, chatRoomSession);

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

    private void removeClosedSession(Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.removeIf(session -> !sessions.contains(session));
    }
}
