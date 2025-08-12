package com.example.crawler.implement;

import com.example.crawler.Config.Info;
import com.example.crawler.Service.AIService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;

@Service
public class AiImpl implements AIService {
    private ChatClient chatClient;

    public AiImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public Flux<String> AiExtract(String url) throws IOException {
        return chatClient.prompt()
                .user(url)
                .stream()
                .content();

    }

}
