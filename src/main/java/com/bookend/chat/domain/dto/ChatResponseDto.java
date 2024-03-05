package com.bookend.chat.domain.dto;

import com.bookend.chat.domain.entity.Chat;
import com.bookend.review.domain.entity.Book;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChatResponseDto {

    private Long chatId;
    private Long bookId;

    public ChatResponseDto(Chat chat) {
        this.chatId = chat.getChatId();
        this.bookId = chat.getBookId();
    }

}
