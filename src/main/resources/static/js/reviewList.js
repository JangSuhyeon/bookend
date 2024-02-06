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

    // 각 리뷰에 대한 평점 표시
    $('.list-score').each(function() {
        var score = parseInt($(this).closest('li').find('.list-score-value').val()); // 점수 가져오기
        drawStars(score, $(this));
    });

    // li 요소 클릭 시 독후감 상세 화면으로 이동
    $('.review-item').click(function() {
        var reviewId = $(this).data('review-id');   // 독후감 PK
        window.location.href = '/review/' + reviewId;
    });

    // 도서 검색 input에 입력 시 이벤트 발생
    $("#searchReview").on("input", function(){
        // 입력값 가져오기
        var searchReview = $(this).val();

        $.ajax({
            type:'POST',
            url:'/review/search',
            data:JSON.stringify({"searchReview": searchReview}),
            contentType:'application/json; charset=UTF-8',
            success:function (res) {
                console.log(res);
                var bookReviewList = res.reviewList;

                // 기존 리뷰 목록을 비우고 새로 받은 데이터로 채우기
                $('.review-contents-list ul').empty();

                if (res.reviewList.length === 0) {
                    // 검색된 독후감이 없을 경우 메시지 표시
                    $('.review-contents-list ul').append('<li class="no-review-message">검색된 독후감이 없습니다.</li>');
                } else {
                    // 받은 데이터에서 reviewList 배열 순회
                    $.each(res.reviewList, function (index, review) {
                        // 리뷰 목록에 새로운 항목 추가
                        $('.review-contents-list ul').append(
                            '<li class="review-item" data-review-id="' + review.reviewId + '">' +
                            '<input type="hidden" class="list-score-value" value="' + review.score + '"/>' +
                            '<div class="review-book-img"><img src="' + review.book.cover + '" alt="cover"></div>' +
                            '<div class="review-book-contents">' +
                            '<h2>' + review.book.title + '</h2>' +
                            '<p>' + review.shortReview + '</p>' +
                            '</div>' +
                            '<div class="review-book-info">' +
                            '<p>' + review.regDt + '</p>' +
                            '<p class="list-score"></p>' +
                            '<p class="' + (review.openYn ? 'review-openYn' : 'review-closeYn') + '">' + (review.openYn ? '공개' : '비공개') + '</p>' +
                            '</div>' +
                            '</li>'
                        );
                    });
                }
            }
        })
    });

})