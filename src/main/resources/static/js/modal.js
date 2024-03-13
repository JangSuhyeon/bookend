function openChatOutModal(chatId) {
    $('#chatOutBtn').attr('href', '/chat/out?chatId=' + chatId);
    $("#chatOutModal").css("display", "block");
    $("#chatOutModal-background").css("display", "block");
}
$(function () {
    // 모달 닫기
    $(".close, #chatOutModal-background, .modal-btn-n").click(function(){
        $("#chatOutModal").css("display", "none");
        $("#chatOutModal-background").css("display", "none");
    });

})