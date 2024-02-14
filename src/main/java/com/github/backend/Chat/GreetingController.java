package com.github.backend.Chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
@RequiredArgsConstructor
public class GreetingController {


    @MessageMapping("/hello")  // app/hello -> pub
    @SendTo("/topic/greetings") // 브로커에게 전송, ToDo: greetings를 room_cid로 받아야함 -> 구독 경로 or SimpMessageSendingOperations 클래스의 convertAndSend()를 사용해서 SendTo 제거
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

}