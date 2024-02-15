$(document).ready(function() {
    var currentDate = new Date();
    var currentYear = currentDate.getFullYear();
    var currentMonth = currentDate.getMonth();
    var daysInMonth = new Date(currentYear, currentMonth + 1, 0).getDate();
    var firstDayOfMonth = new Date(currentYear, currentMonth, 1).getDay();
    var calendar = $('#calendar');
    var monthYear = $('.cal-month > h2'); // 월과 년도를 표시하는 요소 선택
    var monthNames = ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"];
    var reviews;

    // 달력 초기화
    function generateCalendar() {
        calendar.empty();
        var weekHtml = '<div class="week">';
        for (var i = 0; i < firstDayOfMonth; i++) {
            weekHtml += '<div class="day"></div>';
        }
        for (var day = 1; day <= daysInMonth; day++) {

            var calDay = currentYear + "-" + (currentMonth+1).toString().padStart(2, '0') + "-" + day.toString().padStart(2, '0'); // 캘린더 날짜
            var matchedReview = reviews.find(function(review) {               // 해당 날짜의 독후감이 있는지 확인
                return review.regDt === calDay;
            });
            if (matchedReview) {
                weekHtml += '<div class="day with-review">' + day + '</div>'; // 해당 날짜에 독후감이 있으면 클래스 추가
            } else {
                weekHtml += '<div class="day">' + day + '</div>';             // 해당 날짜에 독후감이 없으면 기본
            }

            if ((firstDayOfMonth + day) % 7 === 0) {
                weekHtml += '</div><div class="week">';
            }
        }

        weekHtml += '</div>';
        calendar.append(weekHtml);
        updateMonthYear();
        highlightToday();
    }

    // 년월 업데이트
    function updateMonthYear() {
        monthYear.text(monthNames[currentMonth] + ' ' + currentYear); // 월과 년도를 표시하는 요소 업데이트
    }

    // 오늘일자 표시
    function highlightToday() {
        $('.day').removeClass('today');

        var currentDate = new Date();
        var currentYear = currentDate.getFullYear();
        var currentMonth = currentDate.getMonth();

        var numbers = monthYear.text().match(/\d+/g);
        var month = parseInt(numbers[0]);
        var year = parseInt(numbers[1]);

        // 현재 년도와 월이 같고, 날짜도 같은 경우에만 'today' 클래스 추가
        if (currentYear === year && currentMonth+1 === month) {
            $('.day').eq(currentDate.getDate() + firstDayOfMonth - 1).addClass('today');
        }
    }

    // 페이지 첫 로드 시 호출
    getReviews(currentDate);
    generateCalendar();

    // 이전달 클릭
    $('#prevMonth').click(function() {
        currentMonth--;
        if (currentMonth < 0) {
            currentYear--;
            currentMonth = 11;
        }
        updateCalendar();
    });

    // 다음달 클릭
    $('#nextMonth').click(function() {
        currentMonth++;
        if (currentMonth > 11) {
            currentYear++;
            currentMonth = 0;
        }
        updateCalendar();
    });

    // 달력 업데이트
    function updateCalendar() {
        daysInMonth = new Date(currentYear, currentMonth + 1, 0).getDate();
        firstDayOfMonth = new Date(currentYear, currentMonth, 1).getDay();
        getReviews(new Date(currentYear, currentMonth + 1, 0));
        generateCalendar();
    }

    // 해당년월의 독후감 목록 가져오기
    function getReviews(currentDate) {
        var searchYear = currentDate.getFullYear();
        var searchMonth = currentDate.getMonth() + 1;

        $.ajax({
            type:'POST',
            url:'/review/calendar',
            data:{year:searchYear,
                  month:searchMonth},
            async:false,
            success:function (res) {
                reviews = res.reviewList;
            }
        })
    }

    // 독후감 작성일 클릭 시 해당 일자의 독후감 목록 조회
    $(document).on('click', '.day.with-review', function() {
        var currentYM = $('.cal-month > h2').text();
        var searchYear = parseInt(currentYM.substring(3));
        var searchMonth =  parseInt(currentYM);
        var searchDay = $(this).text();

        $.ajax({
            type:'POST',
            url:'/review/calendar',
            data:{year:searchYear,
                month:searchMonth,
                day:searchDay},
            async:false,
            success:function (res) {
                // 독후감 목록 뿌리기 전에 초기화
                $('.day-reviewList').css("display", "flex");
                $(".day-reviewList > ul").empty();

                // 상단에 클릭한 일자 출력
                $('.day-reviewList > h2').text(searchYear + '년 ' + searchMonth + '월 ' + searchDay + '일');

                // 독후감 목록 출력
                res.reviewList.forEach(function(review) {
                    var li = $("<li class='day-review-item' data-review-id = " + review.reviewId + ">");

                    li.html(`
                        <div class="day-review-top">
                            <div class="day-review-thumb">
                                <img src="${review.book.cover}">
                            </div>
                            <div class="day-review-title">${review.book.title}</div>
                        </div>
                        <p class="day-review-con">${review.shortReview}</p>
                    `);

                    $(".day-reviewList > ul").append(li);
                });
            }
        })
    })

    // 독후감 클릭 시 상세 화면으로 이동
    $('.day-reviewList > ul').on('click', '.day-review-item', function() {
        var reviewId = $(this).data('review-id');   // 독후감 PK
        window.location.href = '/review/' + reviewId;
    });
});