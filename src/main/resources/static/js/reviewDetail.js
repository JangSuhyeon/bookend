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
$(function () {

    // 취향도 표시
    var score = parseInt($('.score').val()); // 점수 가져오기
    drawStars(score, $('.heart'));

    $('#book-delete-btn').click(function () {
        $.ajax({
            type:'DELETE',
            url:'/review/' + $('#reviewId').val(),
            success:function (res) {
                location.href="/";
            },
            error:function () {
                alert('삭제에 실패했습니다.');
            }
        })
    })

})