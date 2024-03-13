$(function () {
    // 채팅방 클릭 시 이동
    $('.review-chat-list > ul').on('click', '.review-chat:not(.review-chat-out)', function () {
        var bookId = $(this).data('book-id');   // 책 PK
        window.location.href = '/chat/' + bookId;
    });
    $('.review-chat-list > ul').on('click', '.review-chat-out', function (event) {
        event.stopPropagation(); // 부모 요소로의 이벤트 전파를 막습니다.
    });

    // 채팅방 목록 불러오기
   $.ajax({
       type:'GET',
       url:'/chat',
       success:function (res) {

           // 채팅방 수
           var chatList = res.chatList;
           if (chatList.length > 0) {
               var cnt = $('<span>').text('(' + chatList.length + ')');
               $('.review-chat-list > h2').append(cnt);
           }

           // 채팅방 목록con
           chatList.forEach (function (chat, index) {
               var $li = $('<li>').addClass('review-chat').attr('data-book-id', chat.bookId);
               var $profileImage = $('<div>').addClass('review-chat-list-profile').html('<div><img src="'+chat.bookCover+'"></div>');
               var $infoDiv = $('<div>').addClass('review-chat-list-info');
               $infoDiv.append('<h2>'+chat.bookTitle+'</h2>');
               if (chat.lastChatMessage === null){
                   $infoDiv.append('<p></p>');
               }else{
                   $infoDiv.append('<p>'+chat.lastChatMessage+'</p>');
               }
               var $a = $('<a>').addClass('review-chat-out').html('<svg xmlns="http://www.w3.org/2000/svg" width="17" height="18" viewBox="0 0 512 512"><!--!Font Awesome Free 6.5.1 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.--><path fill="#9EA6AF" d="M377.9 105.9L500.7 228.7c7.2 7.2 11.3 17.1 11.3 27.3s-4.1 20.1-11.3 27.3L377.9 406.1c-6.4 6.4-15 9.9-24 9.9c-18.7 0-33.9-15.2-33.9-33.9l0-62.1-128 0c-17.7 0-32-14.3-32-32l0-64c0-17.7 14.3-32 32-32l128 0 0-62.1c0-18.7 15.2-33.9 33.9-33.9c9 0 17.6 3.6 24 9.9zM160 96L96 96c-17.7 0-32 14.3-32 32l0 256c0 17.7 14.3 32 32 32l64 0c17.7 0 32 14.3 32 32s-14.3 32-32 32l-64 0c-53 0-96-43-96-96L0 128C0 75 43 32 96 32l64 0c17.7 0 32 14.3 32 32s-14.3 32-32 32z"/></svg>');
               $a.on('click', function() {
                   openChatOutModal(chat.chatId);
               });
               $li.append($profileImage, $infoDiv, $a);

               $('.review-chat-list > ul').append($li);
           });
       }
   })
});