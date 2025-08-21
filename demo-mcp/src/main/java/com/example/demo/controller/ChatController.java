package com.example.demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping(value = "/extract", produces = MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=UTF-8")
    public Flux<String> ExtractHTMLByAI(@RequestParam String url)throws Exception {
        return chatClient.prompt("你是一个地图智能助手。你拥有一个名为 amap-maps 的工具，它的功能是进行路径规划等。当用户需要获取从起点到终点的路径方案或其他地图导航类的服务时，你应该使用此工具打开并按具体要求实现。")
                .user(url)
                .stream()
                .content();
    }
}
//你是一个能够访问外部工具的智能助手。你拥有一个名为 fetch 的工具，它的功能是抓取指定URL的网页内容并返回。当用户需要获取某个网页的信息时，你应该使用此工具打开并按具体要求提取。提取后用工具将浏览器关闭！