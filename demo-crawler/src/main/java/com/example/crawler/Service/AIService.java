package com.example.crawler.Service;

import com.example.crawler.Config.Info;
import reactor.core.publisher.Flux;

import java.io.IOException;

public interface AIService {
    Flux<String> AiExtract(String url) throws IOException;
}
