// 취향도 표시하는 함수
function drawStars(score, container) {
    var stars = '';
    var blackStars = '<span class="star">❤️</span>'; // 까만별 문자
    var whiteStars = '<span class="star">🤍</span>';   // 하얀별 문자

    // 까만별 그리기
    for (var i = 0; i < score; i++) {
        stars += blackStars;
    }

    // 하얀별 그리기
    for (var i = score; i < 5; i++) {
        stars += whiteStars;
    }

    return stars;
}

// 페이지 로드
function loadPageAjax() {
    // 입력값 가져오기
    var searchReview = $('#searchReview').val().trim();

    // 검색 조화를 위한 현재 페이지 가져오기
    var page = $('#page').val();

    $.ajax({
        type:'POST',
        url:'/review/load',
        data:JSON.stringify({"searchReview": searchReview,
            "page" :$('#page').val( ),
            "pageSize" : $('#pageSize').val()}),
        contentType:'application/json; charset=UTF-8',
        success:function (res) {
            var bookReviewList = res.reviewList.content;
            if (bookReviewList.length > 0) {
                $('#page').val(res.page.pageNumber);
                $('#pageSize').val(res.page.pageSize);

                // 받은 데이터에서 reviewList 배열 순회
                $.each(bookReviewList, function (index, review) {
                    // 리뷰 목록에 새로운 항목 추가
                    $('.review-contents-list ul').append(
                        '<li class="review-item" data-review-id="' + review.reviewId + '">' +
                        '<div class="review-book-img"><img src="' + review.book.cover + '" alt="cover"></div>' +
                        '<div class="review-book-contents">' +
                        '<h2>' + review.book.title + '</h2>' +
                        '<p>' + review.shortReview + '</p>' +
                        '</div>' +
                        '<div class="review-book-info">' +
                        '<p>' + review.regDt + '</p>' +
                        '<p class="list-score">' + drawStars(review.score) + '</p>' +
                        '<p class="' + (review.openYn ? 'review-openYn' : 'review-closeYn') + '">' + (review.openYn ? '공개' : '비공개') + '</p>' +
                        '</div>' +
                        '</li>'
                    );
                });
            }else if(searchReview !== "" && bookReviewList.length === 0 && page === '-1') { // 검색어가 있는데 조회된 결과가 없고 검색 후 첫 로드일 때(2페이지 부터는 해당 문구가 필요없기 때문)
                $('.review-contents-list ul').append('<li class="no-review-message">검색된 독후감이 없습니다.</li>');
            }
        }
    })
}

$(function () {

    // 각 리뷰에 대한 평점 표시
    $('.list-score').each(function() {
        var score = parseInt($(this).closest('li').find('.list-score-value').val()); // 점수 가져오기
        $(this).append(drawStars(score));
    });

    // li 요소 클릭 시 독후감 상세 화면으로 이동
    $('.review-item').click(function() {
        var reviewId = $(this).data('review-id');   // 독후감 PK
        window.location.href = '/review/' + reviewId;
    });

    // 독후감 로드
    $(window).scroll(function() {
        // 현재 스크롤바의 위치
        var scrollTop = $(window).scrollTop();
        // 브라우저 창의 높이
        var windowHeight = $(window).height();
        // 문서 전체의 높이
        var documentHeight = $(document).height();

        // 스크롤바가 문서의 맨 아래에 도달했는지 확인
        if (scrollTop + windowHeight == documentHeight) {
            loadPageAjax(); // 독후감 로드
        }
    });

    // 사용자 입력에 따른 이벤트 핸들러 (input에 직접 입력 이벤트를 걸었더니 반응이 너무 느려서 수정)
    var debounceLoadPageAjax = _.debounce(function() {
        // 페이징 초기화
        $('#page').val(-1);

        // 기존 리뷰 목록을 비우고 새로 받은 데이터로 채우기
        $('.review-contents-list ul').empty();

        // 독후감 로드
        loadPageAjax();
    }, 300); // 300 밀리초(0.3초) 간격으로 debounce

    // 사용자 입력 이벤트 핸들러에 debounceLoadPageAjax 함수를 연결
    $("#searchReview").on("input", debounceLoadPageAjax);

})