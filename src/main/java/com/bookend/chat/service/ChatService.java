package com.bookend.chat.service;

import com.bookend.chat.domain.dto.ChatMessageDto;
import com.bookend.chat.domain.dto.ChatRoomResponseDto;
import com.bookend.chat.domain.dto.ChatUserResponseDto;
import com.bookend.chat.domain.entity.ChatRoom;
import com.bookend.chat.domain.entity.ChatMessage;
import com.bookend.chat.domain.entity.ChatUser;
import com.bookend.chat.repository.ChatMessageRepository;
import com.bookend.chat.repository.ChatRoomRepository;
import com.bookend.chat.repository.ChatUserRepository;
import com.bookend.review.domain.entity.Book;
import com.bookend.review.domain.entity.Review;
import com.bookend.review.repository.BookRepository;
import com.bookend.review.repository.ReviewRepository;
import com.bookend.security.domain.entity.User;
import com.bookend.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final BookRepository bookRepository;
    private final ChatUserRepository chatUserRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    // bookId로 채팅방 찾기
    public ChatRoomResponseDto findByBookId(Long bookId) {

        // 기존에 생성된 해당 bookId의 채팅방이 있는지 확인
        ChatRoom chatRoom = chatRoomRepository.findByBookBookId(bookId);

        // 기존에 없으면 채팅방 생성
        if (chatRoom == null) {
            Book book = bookRepository.findByBookId(bookId); // bookId로 Book 조회
            chatRoom = chatRoomRepository.save(new ChatRoom(book));      // 생성한 채팅방 저장
        }

        return new ChatRoomResponseDto(chatRoom);
    }

    // 채팅방에 처음 입장한 순간부터 지금까지의 채팅 불러오기
    public HashMap<String, Object> findChatByUserAndChatId(Long userId, Long chatId) {

        HashMap<String, Object> result = new HashMap<>();
        List<ChatMessageDto> chatMessageDtoList = new ArrayList<>();
        boolean firstEntry = false;

        // 1. 현재 들어가 있는 채팅방인지 확인
        ChatUser chatUser = chatUserRepository.findByUserUserIdAndChatRoomChatRoomIdAndOutYn(userId, chatId, false); // outYn이 Y이면 방에서 나간 상태

        // 2. 현재 들어가 있는 채팅방이면 대화 목록 불러오기
        if (chatUser != null) {
            List<ChatMessage> chatMessageList = chatMessageRepository.findByChatRoomChatRoomIdAndSendTimeAfter(chatId, chatUser.getEnterDateTime()); // 채팅방 입장 시간 이후로의 대화 내용 조회

            // entity -> dto
            chatMessageDtoList = chatMessageList.stream()
                    .map(ChatMessageDto::new)
                    .collect(Collectors.toList());

            // 각 유저의 reviewId 넣기 (모달로 독후감 보기 위함)
            for (ChatMessageDto chatMessageDto : chatMessageDtoList) {
                Long bookId = chatMessageDto.getBookId();
                Review review = reviewRepository.findByUserUserIdAndBookBookId(chatMessageDto.getUserId(), bookId);
                if(review!=null) chatMessageDto.setReview(review);
            }
        } else {
            // 3. 없으면 저장하기
            ChatRoom chatRoom = chatRoomRepository.findById(chatId).orElse(null);
            User user = userRepository.findById(userId).orElse(null);
            if(chatRoom !=null && user != null) {
                chatUser = new ChatUser(chatRoom, user);
                chatUserRepository.save(chatUser);
            }

            firstEntry = true; // 채팅방에서 표시해주기 위해 첫 입장 표시
        }

        result.put("chatMessageDtoList", chatMessageDtoList);
        result.put("firstEntry", firstEntry);

        return result;
    }

    // userId로 채팅방 목록 불러오기
    public List<ChatUserResponseDto> findByUserId(Long userId) {

        // userId로 해당 유저가 속해있는 chatId 조회
        List<ChatUserResponseDto> chatUserList = chatUserRepository.findByUserUserIdAndOutYn(userId, false) // 현재 들어가있는 채팅방만 조회
                .stream()
                .map(ChatUserResponseDto::new)
                .collect(Collectors.toList());

        // 마지막 메세지 넣기
        for (ChatUserResponseDto chatUserResponseDto : chatUserList) {
            ChatMessage chatMessage = chatMessageRepository.findFirstByChatRoomChatRoomIdAndSendTimeAfterOrderBySendTimeDesc(
                    chatUserResponseDto.getChatRoomId(), chatUserResponseDto.getEnterDateTime());
            if(chatMessage!=null) chatUserResponseDto.setLastChatMessage(chatMessage);
        }

        return chatUserList;
    }

    // 채팅방 나가기
    public void quitChat(Long chatId, Long userId) {
        ChatUser chatUser = chatUserRepository.findByUserUserIdAndChatRoomChatRoomIdAndOutYn(userId, chatId, false);
        chatUser.setOutYn(true);
        chatUserRepository.save(chatUser);
    }
}
