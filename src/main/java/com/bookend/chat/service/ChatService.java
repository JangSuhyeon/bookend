package com.bookend.chat.service;

import com.bookend.chat.domain.dto.ChatMessageDto;
import com.bookend.chat.domain.dto.ChatResponseDto;
import com.bookend.chat.domain.dto.ChatUserResponseDto;
import com.bookend.chat.domain.entity.Chat;
import com.bookend.chat.domain.entity.ChatMessage;
import com.bookend.chat.domain.entity.ChatUser;
import com.bookend.chat.repository.ChatMessageRepository;
import com.bookend.chat.repository.ChatRepository;
import com.bookend.chat.repository.ChatUserRepository;
import com.bookend.review.domain.entity.Book;
import com.bookend.review.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final BookRepository bookRepository;
    private final ChatUserRepository chatUserRepository;
    private final ChatMessageRepository chatMessageRepository;

    // bookId로 채팅방 찾기
    public ChatResponseDto findByBookId(Long bookId) {

        // 기존에 생성된 해당 bookId의 채팅방이 있는지 확인
        Chat chat = chatRepository.findByBookId(bookId);

        // 기존에 없으면 채팅방 생성
        if (chat == null) {
            Book book = bookRepository.findByBookId(bookId); // bookId로 Book 조회
            chat = chatRepository.save(new Chat(book));      // 생성한 채팅방 저장

        }

        return new ChatResponseDto(chat);
    }

    // 채팅방에 처음 입장한 순간부터 지금까지의 채팅 불러오기
    public HashMap<String, Object> findChatByUserAndChatId(Long userId, Long chatId) {

        HashMap<String, Object> result = new HashMap<>();
        List<ChatMessageDto> chatMessageDtoList = new ArrayList<>();
        boolean firstEntry = false;

        // 1. 기존에 입장한 적이 있는 채팅방인지 확인
        ChatUser chatUser = chatUserRepository.findByUserIdAndChatId(userId, chatId);

        // 2. 기존에 입장한 적이 있으면 대화 목록 불러오기
        if (chatUser != null) {
            List<ChatMessage> chatMessageList = chatMessageRepository.findByChatId(chatId);

            // entity -> dto
            chatMessageDtoList = chatMessageList.stream()
                    .map(ChatMessageDto::new)
                    .collect(Collectors.toList());
        } else {
            // 없으면 저장하기
            chatUser = new ChatUser(chatId, userId);
            chatUserRepository.save(chatUser);

            firstEntry = true; // 첫 입장 표시
        }

        result.put("chatMessageDtoList", chatMessageDtoList);
        result.put("firstEntry", firstEntry);

        return result;
    }
}
