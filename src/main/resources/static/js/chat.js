let socket;
$(function () {

    // 스크롤 가장 아래로 이동
    $('.chat-con').scrollTop($('.chat-con')[0].scrollHeight);

    // 소켓 연결
    socket = new WebSocket("ws://localhost:8080/ws/chat");
    socket.onopen = function (e) {
        console.log('connect');
        enterRoom(socket);
    };
    // 메세지 수신
    socket.onmessage = function (e) {
        var msg = JSON.parse(e.data);
        var messageType = msg.messageType;  // 메세지 타립
        var message = msg.message;          // 메세지 내용
        var user = msg.user;         // 수신자 정보
        var msgUserId = user.userId.toString();         // 수신자 userId
        var userId = $('#userId').val();    // 현재 로그인 한 유저
        var firstEntry = msg.firstEntry;    // 첫 입장 여부
        var senderNm = user.name;    // 수신자 이름
        var sendTime = msg.sendTime;        // 발신 시간
        var sendDate = msg.sendDay;        // 발신 일자
        var lastUserId = $('.chat-user').last().find('.sender-user-id').val();  // 마지막 수신자 userId
        var lastChatDate = $('.chat-date').last().find('span').text();  // 마지막 수신자 userId

        // 채팅을 수신 시
        if (messageType === 'TALK') {

            // 직전 채팅의 날짜와 다르면 표시
            if (sendDate != lastChatDate) {
                var chatDateDiv = $('<div>').addClass('chat-date');
                var chatDateSpan = $('<span>').text(sendDate);
                chatDateDiv.append(chatDateSpan);
                $('.chat-con').append(chatDateDiv);
            }

            // 직전 채팅의 수신자와 동일한 수신자 이면 프로필 생략
            if (typeof lastUserId === "undefined" || lastUserId !== msgUserId || sendDate != lastChatDate) {
                // 채팅 사용자 정보
                var chatUserDiv = $('<div>').addClass('chat-user');
                var senderUserId = $('<input>').attr({
                    type: 'hidden',
                    class: 'sender-user-id',
                    value: msgUserId
                });
                var chatProfileDiv = $('<div>').addClass('chat-profile').append($('<img>').attr('src', user.picture));
                var chatName = $('<h2>').addClass('chat-name').text(senderNm);
                chatUserDiv.append(chatProfileDiv,senderUserId, chatName);

                // 발신자가 자기 자신이면 me 클래스 추가
                if (msgUserId === userId) {
                    chatUserDiv.addClass('me');
                }

                $('.chat-con').append(chatUserDiv);
            }

            // 채팅 메시지
            var chatMessageDiv = $('<div>').addClass('chat-box chat-box-t1').append($('<p>').text(message));
            var chatTime = $('<span>').text(sendTime);
            chatMessageDiv.append(chatTime);

            // 발신자가 자기 자신이면 me 클래스 추가
            if (msgUserId === userId) {
                chatMessageDiv.addClass('me');
            }
            $('.chat-con').append(chatMessageDiv);
        } else if(messageType === 'ENTER' && firstEntry) { // 채팅방 첫 입장 시
            var chatEntryDiv = $('<div>').addClass('chat-entry');
            var firstEntry = $('<span>').text(senderNm + '님이 입장하셨습니다.');
            chatEntryDiv.append(firstEntry);
            $('.chat-con').append(chatEntryDiv);
        }

        $('.chat-con').scrollTop($('.chat-con')[0].scrollHeight);
    }
    // 소켓 닫기
    socket.onclose=function(e){
        console.log('disconnet');
    }
    // 소켓 에러
    socket.onerror = function (e){
        console.log(e);
    }

    // input 태그에서 enter 입력하면 버튼 이벤트
    $('#msgInput').keypress(function(event){
        if(event.keyCode == 13){
            $('#msgSendBtn').click();
        }
    });

    $('#chatOutBtn').click(function (event) {
        event.preventDefault();

        var chatMsg = {
            "messageType" : "QUIT",
            "chatId" : $('#chatId').val(),
            "userId" : $('#userId').val(),
            // "message" : $('#msgInput').val(),
            "senderNm" : $('#userNm').val()
        };
        socket.send(JSON.stringify(chatMsg));

        window.location.href = $(this).attr("href");
    })

});

// 채팅방 입장
function enterRoom(socket) {
    var enterMsg = {
        "messageType" : "ENTER",
        "chatId" : $('#chatId').val(),
        "userId" : $('#userId').val(),
        "message" : '',
        "firstEntry" : $('#firstEntry').val(),
        "senderNm" : $('#userNm').val()
    }
    socket.send(JSON.stringify(enterMsg));
}

// 메세지 전송 버튼 클릭
function sendMsg() {
    let messgae = $('#msgInput').val().trim();

    if (messgae !== null && messgae !== '') { // 메세지가 공백이 아니면
        var chatMsg = {
            "messageType" : "TALK",
            "chatId" : $('#chatId').val(),
            "userId" : $('#userId').val(),
            "message" : $('#msgInput').val(),
            "senderNm" : $('#userNm').val()
        };
        socket.send(JSON.stringify(chatMsg));
    }

    $('#msgInput').val('');
}