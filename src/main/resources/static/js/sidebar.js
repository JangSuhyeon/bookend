$(function () {
    // 채팅방 클릭 시 이동
    $('.review-chat-list > ul').on('click', '.review-chat', function () {
        var bookId = $(this).data('book-id');   // 책 PK
        window.location.href = '/chat/' + bookId;
    });
});