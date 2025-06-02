package controllers;

import ai.LocalAIClient;
import ai.PromptBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ScheduleViewController {

    @FXML
    private TextArea resultTextArea;

    public void initialize() {
        try {
            String userPrompt = PromptBuilder.buildUserPrompt();
            String systemPrompt = PromptBuilder.buildSystemPrompt();

            String response = LocalAIClient.sendPrompt(userPrompt, systemPrompt);

            if (response.toLowerCase().contains("conflict")) {
                resultTextArea.setText(response);
            } else {
                resultTextArea.setText("Generated Schedule:\n\n" + response);
            }

        } catch (Exception e) {
            resultTextArea.setText("Error generating schedule.");
            e.printStackTrace();
        }
    }
}
