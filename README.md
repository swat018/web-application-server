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
* 

### 요구사항 3 - post 방식으로 회원가입
* 

### 요구사항 4 - redirect 방식으로 이동
* 

### 요구사항 5 - cookie
* 

### 요구사항 6 - stylesheet 적용
* 

### heroku 서버에 배포 후
* 