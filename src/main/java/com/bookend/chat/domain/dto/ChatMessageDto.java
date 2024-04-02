package com.bookend.chat.domain.dto;

import com.bookend.chat.domain.entity.ChatRoom;
import com.bookend.chat.domain.entity.ChatMessage;
import com.bookend.review.domain.entity.Book;
import com.bookend.review.domain.entity.Review;
import com.bookend.security.domain.entity.User;
import jakarta.persistence.Column;
import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {

    // 메시지 타입 : 입장, 채팅
    public enum MessageType {
        ENTER, TALK, QUIT
    }

    private Long ChatMessageId;      // PK
    private String message;          // 메시지
    private Boolean firstEntry;      // 첫 입장 여부
    private String sendTime;         // 수신 시간
    private String sendDay;          // 수신 일자
    private MessageType messageType; // 메시지 타입

    // Review
    private Long reviewId;           // PK
    private String shortReview;      // 한줄평
    private String longReview;       // 독후감
    private int score;               // 점수
    private Boolean openYn;          // 공개여부
    private Date regDt;              // 작성일
    private Date modDt;              // 수정일

    // ChatRoom
    private Long chatRoomId;         // PK

    // Book
    private Long bookId;             // PK
    private String title;            // 도서명
    private String isbn;             // 도서고유번호
    private String author;           // 작가
    private String publisher;        // 출판사
    private String cover;            // 책커버 주소

    // User
    private Long userId;             // PK
    private String name;             // 회원이름
    private String picture;          // 프로필 사진

    public ChatMessageDto(ChatMessage chatMessage) {
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatDay = new SimpleDateFormat("yyyy년 M월 d일");

        this.message = chatMessage.getMessage();
        this.sendTime = formatTime.format(chatMessage.getSendTime());
        this.sendDay = formatDay.format(chatMessage.getSendTime());
        this.messageType = chatMessage.getMessageType();

        ChatRoom chatRoom = chatMessage.getChatRoom();
        Book book = chatRoom.getBook();
        this.chatRoomId = chatRoom.getChatRoomId();
        this.bookId = book.getBookId();
        this.title = book.getTitle();
        this.isbn = book.getIsbn();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.cover = book.getCover();

        User user = chatMessage.getUser();
        this.userId = user.getUserId();
        this.name = user.getName();
        this.picture = user.getPicture();
    }

    public void setUser(User user){
        this.userId = user.getUserId();
        this.name = user.getName();
    }

    public void setReview(Review review){
        this.reviewId = review.getReviewId();
        this.shortReview = review.getShortReview();
        this.longReview = review.getLongReview();
        this.score = review.getScore();
        this.openYn = review.getOpenYn();
        this.regDt = review.getRegDt();
        this.modDt = review.getModDt();
    }

}
