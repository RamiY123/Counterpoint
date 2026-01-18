package com.example.demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class OpenAiConfig {

    @Value("${app.debate.max-response-words:100}")
    private int maxResponseWords;

    public int getMaxResponseWords() {
        return maxResponseWords;
    }

    @Bean
    @Scope("prototype")
    public ChatClient chatClient(OpenAiChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }
}
