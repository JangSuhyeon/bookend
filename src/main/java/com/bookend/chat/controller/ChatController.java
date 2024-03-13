package com.bookend.chat.controller;

import com.bookend.chat.domain.dto.ChatMessageDto;
import com.bookend.chat.domain.dto.ChatResponseDto;
import com.bookend.chat.domain.dto.ChatUserResponseDto;
import com.bookend.chat.domain.entity.Chat;
import com.bookend.chat.service.ChatService;
import com.bookend.security.domain.SessionUser;
import com.bookend.security.domain.annotation.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    // 채팅방 목록 불러오기
    @GetMapping("")
    @ResponseBody
    public HashMap<String, List> selectChatList(@LoginUser SessionUser loginUser) {
        HashMap<String, List> result = new HashMap<>();

        // 로그인한 유저의 채팅방 목록 불러오기
        List<ChatUserResponseDto> chatList = chatService.findByUserId(loginUser.getUserId());
        result.put("chatList", chatList);

        return result;
    }


    // 채팅방으로 이동
    @GetMapping("/{bookId}")
    public String goToChat(@PathVariable("bookId")Long bookId,
                           Model model,
                           @LoginUser SessionUser loginUser) {

        log.info("{} 에 {} 님이 입장했습니다.", bookId, loginUser.getName());
        long userId = loginUser.getUserId();

        // 해당 bookId의 채팅방 존재 여부에 따라 분기처리
        ChatResponseDto chat = chatService.findByBookId(bookId);
        long chatId = chat.getChatId();

        // 채팅방에 처음 입장한 순간부터 지금까지의 채팅 불러오기
        HashMap<String, Object> result = chatService.findChatByUserAndChatId(userId, chatId);
        List<ChatMessageDto> chatMessageList = (List<ChatMessageDto>) result.get("chatMessageDtoList");
        boolean firstEntry = (boolean) result.get("firstEntry");
        for (ChatMessageDto chatMessageDto : chatMessageList) {
            System.out.println(chatMessageDto.toString());
        }
        model.addAttribute("chat", chat);
        model.addAttribute("chatMessageList", chatMessageList);
        model.addAttribute("firstEntry", firstEntry);
        model.addAttribute("loginUser", loginUser);

        return "chat/chat";
    }

    // 채팅방 나가기
    @GetMapping("/out")
    public String quitChat(@RequestParam("chatId")Long chatId,
                           @LoginUser SessionUser loginUser) {

        chatService.quitChat(chatId, loginUser.getUserId());

        return "redirect:/";
    }

}
