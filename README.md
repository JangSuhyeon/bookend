# 📕 Bookend

▪️ **한 줄 소개 :** Bookend는 독후감을 작성하고 같은 도서를 읽은 사람들과 채팅을 할 수 있는 서비스 입니다.

▪️ **사용한 기술 :** SpringBoot, SpringSecurity, JPA, html/css, Thymeleaf, jQuery, MariaDB, Gradle

▪️ **Noton :** https://www.notion.so/Bookend-770389ecca504596938bd5a81342407d?pvs=4

---

### 🔗 포트폴리오 사이트
http://www.jangsuhyeon.com/
<br><br>
### 🎬 시연 영상
https://youtu.be/ZP1BvX5S2BQ
<br><br>
### 📖 상세내용

![이미지 1](https://github.com/JangSuhyeon/bookend/assets/65846005/d8c224e2-76b5-4e66-b357-623866042d92)
![이미지 3](https://github.com/JangSuhyeon/bookend/assets/65846005/2bb0bea7-c9af-475f-9a2e-b1bd2132ee01)
![이미지 2](https://github.com/JangSuhyeon/bookend/assets/65846005/92f3a043-6357-4a2c-b984-bef718b85b29)

✨ 북엔드는 **독후감을 작성**하고 같은 도서를 읽은 **사람들과 채팅**을 할 수 있는 서비스입니다.

오프라인 또는 온라인 대면 독서 모임보다 **좀 더 가볍게 책에 대해서 대화를 하고 싶어하는 사람들을 위해 개발**하게 되었습니다.

독후감을 작성하고, 자신이 작성한 독후감 상세페이지에서 채팅방에 입장할 수 있습니다.

자신의 독후감을 공개해 놓았을 때만 다른 이용자가 볼 수 있습니다.
<br><br>
### 🔑 주요기능

- 구글 **OAuth2** 로그인 (현재는 등록된 이메일만 로그인 가능합니다.😢)
- **Form 로그인** 기능을 이용한 게스트 로그인
- 자신이 작성한 독후감 **목록 조회**
- 독후감 **작성 및 저장, 수정, 삭제**
- **알라딘 API를 이용**하여 독후감을 작성할 도서 조회
- 도서명으로 독후감 **검색**
- 같은 독후감을 작성한 이용자끼리 **채팅방 입장 및 이용 가능**(현재는 로컬환경에서만 테스트 할 수 있습니다.)
- 캘린더에서 날짜를 클릭하면 **해당 일자에 작성한 독후감 목록 조회** 가능
<br><br>
### 💻 주요 코드 보러가기
https://www.notion.so/980fef8f864b4325be75a31903f9aea5?pvs=21
<br><br>
### 💡 **깨달은 점**

- SpringSecurity를 이용하여 로그인 기능을 구현할 때 콘솔 로그에도 찍히지 않고 인터셉트 되는 경우가 많아서 기능이 작동되지 않는 이유를 찾기가 어려웠다. → 해당 기능의 흐름을 파악해서 정리함.
- 쿼리를 작성하여 데이터를 조회하는 것처럼 JPA 메소드를 이용하는 것이 어려웠다.
    
    → JPA 메소드 뿐만 아니라 여러가지 대체 기능들을 공부함.
    
- 화면을 처음 로드했을때는 데이터를 Model에 넣고, 이후에 검색을 했을때는 ajax로 호출하려고 했을때 코드가 깔끔하지 않았다.
    
    → Thymeleaf 기능을 이용하여 데이터를 화면에 뿌려줄 수 있는 기능을 공부함.


### ☑️ 추가로 개발하고 싶은 기능

- 모든 사용자가 구글 OAuth2 로그인을 이용할 수 있도록
- 운영서버에서도 채팅방 기능을 사용할 수 있도록
