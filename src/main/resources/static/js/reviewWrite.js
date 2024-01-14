$(function () {

    // 알라딘 Open API를 이용한 도서 검색
    var title = $('#title');
    var cover = $('#cover');
    var author = $('#author');
    var publisher = $('#publisher');
    $('#searchBook').select2({
        ajax: {
            type:'POST',
            url:'/review/book/search',
            dataType: 'json',
            delay: 250,
            contentType : 'application/json',
            data: function (params) {
                var query = {searchKeyword: params.term}; // 검색 키워드
                return JSON.stringify(query);
            },
            processResults: function (data) {
                return {
                    results: $.map(data.item, function (item) {
                        var option = '<div class="select2-wrapper"><p class="select2-title">' + item.title + '</p>'; // 도서명

                        // 이미지 경로가 있는 경우 이미지를 추가
                        if (item.cover) {
                            option += '<img src="' + item.cover + '" alt="Book Image" class="book-image"/>';
                        }
                        option += '</div>';

                        return {
                            text: option,
                            id: item.isbn,
                            title:item.title,
                            author:item.author,
                            publisher:item.publisher,
                            cover:item.cover
                        };
                    })
                };
            },
            cache: true // 결과를 캐시하여 중복 요청을 방지
        },
        placeholder: '도서명을 검색해주세요:)',
        minimumInputLength: 1,
        escapeMarkup: function (markup) {
            return markup; // 템플릿을 HTML 이스케이프 처리하지 않도록 설정
        },
        language: {
            inputTooShort: function () {
                return "도서명을 검색해주세요:)";
            },
            noResults: function () {
                return "검색된 도서가 없습니다 :(";
            },
            searching: function () {
                return "검색 중입니다;)";
            }
        }
    }).on('select2:select', function (e) {
        // 선택한 옵션의 이미지를 표시할 요소에 이미지 업데이트
        var selectedOption = e.params.data;
        if (selectedOption.cover) {
            cover.attr('src', selectedOption.cover);
        } else {
            // 이미지가 없는 경우 이미지 요소 숨기기
            cover.hide();
        }

        title.val(selectedOption.title);
        author.text(selectedOption.author);
        publisher.text(selectedOption.publisher);
    }).on('select2:unselect', function () {
        // 옵션 선택 해제 시 이미지 요소 숨기기
        cover.hide();
    });

    // 취향도 선택
    $('input[type="checkbox"]').change(function () {
        var clickedIndex = $(this).parent().index(); // 부모(label) 엘리먼트의 인덱스를 가져옵니다.
        $('input[type="checkbox"]').each(function (index) {
            if (index <= clickedIndex) {
                $(this).prop('checked', true);
                $(this).siblings('span').text('❤️');
            } else {
                $(this).prop('checked', false);
                $(this).siblings('span').text('🤍');
            }
        });
    });

    // 독후감 저장
    $('#book-save-btn').click(function () {

        // 취향도 구하기
       var checkedCheckboxes = $(".heart input[type='checkbox']:checked");
       var heartCnt = checkedCheckboxes.length;

       // validate
        var isbn = $('#searchBook').val();
        var title = $('#title').val().trim();
        var author = $('#author').text().trim();
        var publisher = $('#publisher').text().trim();
        var openYn = $('input[name="openYn"]:checked').val();
        var cover = $('#cover').attr('src');
        var shortReview = $('#shortReview').val().trim();
        var longReview = $('#longReview').val().trim();

        if (!isbn) {
            alert('도서를 선택해주세요.');
        } else if (!shortReview){
            alert('한줄평을 작성해주세요.');
        } else if (shortReview.length > 130) {
            alert('한줄평을 130글자 미만으로 작성해주세요'); // Todo 실시간 info
        } else {
           var requestParam = {
               'isbn' : isbn,
               'title' : title,
               'author' : author,
               'publisher' : publisher,
               'openYn' : openYn,
               'cover' : cover,
               'score' : heartCnt,
               'shortReview' : shortReview,
               'longReview' : longReview
           }

            // 저장
            $.ajax({
                type:'POST',
                url:'/review/write',
                data:JSON.stringify(requestParam),
                contentType: 'application/json',
                success:function (res) {
                    location.href="/";
                },
                error:function () {
                    alert('저장에 실패했습니다.');
                }
            })
        }
    });
})