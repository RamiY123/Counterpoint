package com.example.demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class OpenAiConfig {

    @Value("${app.debate.max-response-words:100}")
    private int maxResponseWords;

    @Value("${spring.ai.openai.api-key:}")
    private String configuredApiKey;

    private final ApiKeyHolder apiKeyHolder;

    public OpenAiConfig(ApiKeyHolder apiKeyHolder) {
        this.apiKeyHolder = apiKeyHolder;
    }

    public int getMaxResponseWords() {
        return maxResponseWords;
    }

    private String getEffectiveApiKey() {
        // First check the runtime holder (set via UI)
        String runtimeKey = apiKeyHolder.getApiKey();
        if (runtimeKey != null && !runtimeKey.isEmpty() && !runtimeKey.startsWith("sk-placeholder")) {
            return runtimeKey;
        }
        // Fall back to configured key
        return configuredApiKey;
    }

    @Bean
    @Scope("prototype")
    public ChatClient chatClient() {
        String apiKey = getEffectiveApiKey();
        OpenAiApi openAiApi = new OpenAiApi(apiKey);
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .withModel("gpt-4o")
                .withTemperature(0.7)
                .build();
        OpenAiChatModel chatModel = new OpenAiChatModel(openAiApi, options);
        return ChatClient.builder(chatModel).build();
    }
}
