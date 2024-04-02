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
        var msgUserId = msg.userId.toString();  // 수신자 userId
        var userId = $('#userId').val();    // 현재 로그인 한 유저
        var firstEntry = msg.firstEntry;    // 첫 입장 여부
        var senderNm = msg.name;            // 수신자 이름
        var sendTime = msg.sendTime;        // 발신 시간
        var sendDate = msg.sendDay;         // 발신 일자
        var picture = msg.picture;          // 프로필사진
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
                var chatProfileDiv = $('<div>').addClass('chat-profile').append($('<img>').attr('src', picture));
                var chatName = $('<h2>').addClass('chat-name').text(senderNm);
                chatUserDiv.append(chatProfileDiv,senderUserId, chatName);

                // 발신자가 자기 자신이면 me 클래스 추가
                if (msgUserId === userId) {
                    chatUserDiv.addClass('me');
                }else if (msg.openYn) {
                    // 해당 이용자가 작성한 독후감이 공개 상태라면
                    var reviewLinkSpan = $('<span>').html('<svg xmlns="http://www.w3.org/2000/svg" width="12" height="13" viewBox="0 0 320 512"><path fill="#4c55e5" d="M310.6 233.4c12.5 12.5 12.5 32.8 0 45.3l-192 192c-12.5 12.5-32.8 12.5-45.3 0s-12.5-32.8 0-45.3L242.7 256 73.4 86.6c-12.5-12.5-12.5-32.8 0-45.3s32.8-12.5 45.3 0l192 192z"/></svg>');
                    var reviewLinkText = $('<p>').text(senderNm + '님의 독후감 보러가기');
                    var chatReviewMoreDiv = $('<div>').addClass('chat-review-more').append(reviewLinkSpan, reviewLinkText);
                    chatReviewMoreDiv.on('click', function() {
                        openReivewModal(msg.reviewId);
                    });
                    chatUserDiv.append(chatReviewMoreDiv);
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

    // 채팅방 나가기
    $('#chatOutBtn').click(function (event) {
        event.preventDefault();

        var chatMsg = {
            "messageType" : "QUIT",
            "chatRoomId" : $('#chatRoomId').val(),
            "userId" : $('#userId').val(),
            // "message" : $('#msgInput').val(),
            "senderNm" : $('#userNm').val()
        };
        socket.send(JSON.stringify(chatMsg));

        window.location.href = $(this).attr("href");
    })

    // 독후감 버튼 클릭 시 해당 유저의 해당 도서 독후감 모달 오픈
    $('.chat-user ').on('click', '.chat-review-more', function() {
        var senderReviewId = $(this).siblings('.sender-review-id').val();
        openReivewModal(senderReviewId);
    })

    // 모달 닫기
    $("#chatModalBackground").click(function(){
        $("#chatReviewModal").css("display", "none");
        $("#chatModalBackground").css("display", "none");
    });
});

// 취향도 표시하는 함수
function drawStars(score, container) {
    var blackStars = '<span class="star">❤️</span>'; // 까만별 문자
    var whiteStars = '<span class="star">🤍</span>'; // 하얀별 문자

    // 까만별 그리기
    for (var i = 0; i < score; i++) {
        container.append(blackStars);
    }

    // 하얀별 그리기
    for (var i = score; i < 5; i++) {
        container.append(whiteStars);
    }
}

// 채팅방 입장
function enterRoom(socket) {
    var enterMsg = {
        "messageType" : "ENTER",
        "chatRoomId" : $('#chatRoomId').val(),
        "userId" : $('#userId').val(),
        "message" : '',
        "firstEntry" : $('#firstEntry').val(),
        "senderNm" : $('#userNm').val()
    }
    if (!isOpen(socket)) return;
    socket.send(JSON.stringify(enterMsg));
}

// 메세지 전송 버튼 클릭
function sendMsg() {
    let messgae = $('#msgInput').val().trim();

    if (messgae !== null && messgae !== '') { // 메세지가 공백이 아니면
        var chatMsg = {
            "messageType" : "TALK",
            "chatRoomId" : $('#chatRoomId').val(),
            "userId" : $('#userId').val(),
            "message" : $('#msgInput').val(),
            "senderNm" : $('#userNm').val()
        };
        if (!isOpen(socket)) return;
        socket.send(JSON.stringify(chatMsg));
    }

    $('#msgInput').val('');
}

function isOpen(ws) { return ws.readyState === ws.OPEN }

// 리뷰 모달 띄우기
function openReivewModal(reviewId) {
    $.ajax({
        type:'GET',
        url:'/review/modal',
        data:{'senderReviewId' : reviewId},
        success:function (res) {
            $('#chatReviewModal').css({"display": "block"});
            $("#chatModalBackground").css("display", "block");

            var review = res.review;
            $('#modalWriter').text(review.name);
            $('#modalTitle').text(review.title);
            $('#modalAuthor').text(review.author);
            $('#modalPublisher').text(review.publisher);
            $('#modalCover').attr('src', review.cover);
            $('#modalShortReview').text(review.shortReview);
            $('#modalLongReview').text(review.longReview);

            $('#modalScore').empty();
            drawStars(review.score, $('#modalScore')); // 점수 표시
        }
    })
}