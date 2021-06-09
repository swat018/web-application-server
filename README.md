# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리
* 구현 단계에서는 각 요구사항을 구현하는데 집중한다. 
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다. 

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답
- HTTP Header
    - GET /index.html HTTP/1.1
    - Host: localhost:8080
    - Connection: keep-alive
    - Accept: */*
- HINT 1단계
    - InputStream을 한 줄 단위로 읽기 위해 BufferedReader를 생성한다.
    - BufferedReader.readLine() 메서드를 활용해 라인별로 HTTP 요청 정보를 읽는다.
    - HTTP 요청 정보 전체를 출력한다.
        - 헤더 마지막은 while (!"".equals(line)) {}로 확인 가능하다.
        - linden null 값인 경우에 대한 예외 처리도 해야 한다. 그렇지 않을 경우 무한 루프에 빠진다. (if (line == null) { return;})
- HINT 2단계
    - HTTP 요청 정보의 첫 번째 라인에서 요청 URL을 추출한다.
        - String[] tokens = line.split(" ");를 활용해 문자열을 분리할 수 있다.
    - 구현은 별도의 유틸 클래스를 만들고 단위 테스트를 만들어 진행하면 편하다.
- HINT 3단계
    - 요청 URL에 해당하는 파일을 webapp 디렉토리에서 읽어 전달하면 된다.
    - 구글에서 "java files readaiibytes"로 검색해 파일 데이터를 byte[]로 읽는다.    

### 요구사항 2 - get 방식으로 회원가입
- HTML과 URL을 비교해 보고 사용자가 입력한 값을 파싱해 model.User 클래스에 저장한다.
- HTTP Header
    - GET /usr/create?usrId=javajigi&password=password&name=JaeSung
    - HTTP /1.1
- HINT
    - HTTP 요청의 첫 번째 라인에서 요청 URL을 추출한다
    - 요청 URL에서 접근 경로와 이름=값으로 전달되는 데이터를 추출해 User 클래스에 담는다.
    - 구현은 가능하면 JUnit을 활용해 단위 테스트를 진행하면서 하면 좀 더 효과적으로 개발 가능하다.
    - 이름=값 파싱은 util.HttpRequestUtils 클래스의 parseQueryString() 메서드를 활용한다.
    - 요청 URL과 이름 값을 분리해야 한다.
        - String url = "/?data=234";
        - int index = url.indexOf("?");
        - String requestPath = url.substring(0, index);
        - String params = url.substring(index+1);

### 요구사항 3 - post 방식으로 회원가입
- [http://localhost:8080/user/form.html](http://localhost:8080/user/form.html) 파일의  form 태그 methodFMF get에서 post로 수정한 후 회원가입이 정상적으로 동작하도록 구현한다.
- **HTTP Header와  Body**
    - POST /user/create HTTP/1.1
    - Host: localhost:8080
    - Connection: keep-alive
    - Content-Length: 59
    - Content-Type: application/X-www-form-urlencoded
    - Accept: */*
    - **userId=javajigi&password=password&name=JaeSung**
- **HINT**
    - Post로 데이터를 전달할 경우 전달하는 데이터는 HTTP 본문에 담긴다.
    - HTTP 본문은 HTTP 헤더 이후 빈 공백을 가지는 한 줄(line) 다음부터 시작한다.
    - HTTP 본문에 전달되는 데이터는 GET 방식으로 데이터를 전달할 때의 이름=값과 같다.
    - BufferedReader에서 본뮨 데이터는  util.IOUtils 클래스의 readData() 메서드를 확용한다. 본문의 길이는 HTTP 헤더의 Content-Length의 값이다
    - 회원가입시 입력한 모든 데이터를 추출해 User 객체를 생성한다.

### 요구사항 4 - redirect 방식으로 이동
- "회원가입"을 완료한 후 /index.html 페이지로 이동한다.
- **HINT**
  - HTTP 응답 헤더의 status code를 200이 아니라 302 code를 사용한다.
  - [http://en.wikipedia.org/wiki/HTTP_302](http://en.wikipedia.org/wiki/HTTP_302) 문서 참고

### 요구사항 5 - cookie (로그인하기)
- "로그인" 메뉴를 클릭하면 [http://localhost:8080/user/login.html](http://localhost:8080/user/login.html으로) 으로 이동해 로그인
- 로그인이 성공하면 /index.html 페이지로 이동. 로그인이 실패하면 /user/login_failed.html로 이동.
- 로그인이 성공하면 쿠키를 활용해 로그인 상태를 유지할 수 있어야 한다.
- **로그인이 성공**할 경우 `요청 헤더의 Cookie 헤더 값이 logined=true`, **로그인이 실패**하면 `Cookie 헤더 값이 logined=false`로 전달되어야 한다.
- **HINT 1단계**
  - 로그인 성공시 HTTP 응답 헤더(response header)에 Set-Cookie를 추가해 로그인 성공 여부를 전달한다.
  - **응답 헤더의 예시**
    - HTTP/1.1 200 OK
    - Content-Type: text/html
    - **Set-Cookie: logined=true**
  - 위와 같이 응답을 보내면 브라우저는 다음과 같이 HTTP 요청 헤더에 Cookie 값으로 전달한다. 이렇게 전달받은 Cookie 값으로 로그인 유무를 판단한다.
  - **다음 요청에 대한 요청 헤더 예시**
    - GET /index.html HTTP/1.1
    - Host: localhost:8080
    - Connection: keep-alive
    - Accep: */ * ****
    - **Cookie: logined=true**
- **HINT 2단계**
  - 정상적으로 로그인 되었는지 확인하려면 앞 단계에서 회원가입한 데이터를 유지해야 한다.
    - 앞 단계에서 회원가입할 때 생성한  User 객체를 DataBase.addUser() 메서드를 활용해 저장한다.
  - 아이디와 비밀번호가 같은지를 확인해 로그인이 성공하면 응답 헤더의 Set-Cookie 값을 logined=true, 로그인이 실패할 경우 Set-Cookie 값을 logined=false로 설정한다.
  - 응답 헤더에 Set-Cookie 값을 설정한 후 요청 헤더에 Cookie 값이 전달되는지 확인한다.

### 요구사항 6 - stylesheet 적용
* 

### heroku 서버에 배포 후
* 