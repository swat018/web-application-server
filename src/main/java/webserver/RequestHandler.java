package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            if (line == null) {
                return;
            }

            String url = HttpRequestUtils.getUrl(line);
            Map<String, String> headers = new HashMap<String, String>();
            int contentLength = 0;
            while(!"".equals(line)) {
                log.debug("header : {}", line);
                line = br.readLine();
                if (line.contains("Content-Length")) {
                    contentLength = getContentLength(line);
                }
            }
            log.debug("Content-Length : {}", headers.get("Content-Length"));

            if (url.startsWith("/user/create")) {
                String requestBody = IOUtils.readData(br, contentLength);
                log.debug("Request Body : {}", requestBody);
                Map<String,String> params = HttpRequestUtils.parseQueryString(requestBody);
                User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
                log.debug("User : {}", user);
                DataBase.addUser(user);

                DataOutputStream dos = new DataOutputStream(out);
                response302Header(dos);
            } else if ("/user/login".equals(url)) {
                String requestBody = IOUtils.readData(br, contentLength);
                log.debug("Request Body : {}", requestBody);
                Map<String,String> params = HttpRequestUtils.parseQueryString(requestBody);
                log.debug("UserId : {}, passwd : {} ", params.get("userId"), params.get("password"));
                User user = DataBase.findUserById(params.get("uerId"));
                log.debug("password : {}", user);

//                if (user == null) {
//                    log.debug("User Not Found");
//                }else if (user.getPassword().equals(params.get("password"))) {
//                    log.debug("login success!!");
//                } else {
//                    log.debug("Password Mismach!!");
//                }
//
//                DataOutputStream dos = new DataOutputStream(out);
//                response302Header(dos);
            } else {
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = Files.readAllBytes(new File("./webapp"+url).toPath());
                response200Header(dos, body.length);
                responseBody(dos, body);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private int getContentLength(String line){
        String[] heaserTokens = line.split(":");
        return Integer.parseInt(heaserTokens[1].trim());
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Set-Cookied: logined=true \r\n");
            dos.writeBytes("Location: /index.html \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
