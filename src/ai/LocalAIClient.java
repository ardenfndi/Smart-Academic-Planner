package ai;

import com.google.gson.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LocalAIClient {
    public static String sendPrompt(String userPrompt, String systemPrompt) throws IOException {
        String url = "http://localhost:1234/v1/chat/completions";
        String model = "mythomax-l2-13b";

        JsonObject payload = new JsonObject();
        payload.addProperty("model", model);
        payload.addProperty("temperature", 0.2);  
        payload.addProperty("max_tokens", 500);  

        JsonArray messages = new JsonArray();

        JsonObject systemMsg = new JsonObject();
        systemMsg.addProperty("role", "system");
        systemMsg.addProperty("content", systemPrompt);

        JsonObject userMsg = new JsonObject();
        userMsg.addProperty("role", "user");
        userMsg.addProperty("content", userPrompt);

        messages.add(systemMsg);
        messages.add(userMsg);
        payload.add("messages", messages);

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = connection.getOutputStream()) {
            os.write(new Gson().toJson(payload).getBytes(StandardCharsets.UTF_8));
            os.flush();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            responseBuilder.append(line);
        }

        connection.disconnect();

        System.out.println("AI RESPONSE (RAW): " + responseBuilder);

        JsonObject root = JsonParser.parseString(responseBuilder.toString()).getAsJsonObject();
        JsonArray choices = root.getAsJsonArray("choices");

        if (choices != null && choices.size() > 0) {
            JsonObject message = choices.get(0).getAsJsonObject().getAsJsonObject("message");
            return message.get("content").getAsString();
        }

        throw new IOException("Can not find AI answer!");
    }
}
