package com.jinho.randb.domain.chatgpt.api;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ChatController {
    private final OpenAiChatModel openAiChatModel;
    //private final VertexAiGeminiChatModel vertexAiGeminiChatModel;


    public ChatController(OpenAiChatModel openAiChatModel, VertexAiGeminiChatModel vertexAiGeminiChatModel) {
        this.openAiChatModel = openAiChatModel;
        //this.vertexAiGeminiChatModel = vertexAiGeminiChatModel;
    }

    @PostMapping("api/chat")
    public Map<String, String> chat(@RequestBody String message) {
        Map<String, String> responses = new HashMap<>();

        String openAiResponse = openAiChatModel.call(message);
        responses.put("openai(chatGPT) 응답", openAiResponse);


        //String vertexAiGeminiResponse = vertexAiGeminiChatModel.call(message);
        //responses.put("vertexai(gemini) 응답", vertexAiGeminiResponse);

        return responses;
    }

}
