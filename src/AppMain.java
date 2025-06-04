import ai.LocalAIClient;
import ai.PromptBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class AppMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        String url = "jdbc:mysql://localhost:3306/smart_academic_planner";
        String user = "root";
        String password = "12345";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM UserCourses");
            System.out.println("User courses deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to delete database.");
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/CourseSelection.fxml"));
            primaryStage.setTitle("Smart Academic Planner");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            System.out.println("Can not load GUI : " + e.getMessage());
            e.printStackTrace();
        }

        new Thread(() -> {
            try {
                long start = System.currentTimeMillis();
                String systemPrompt = PromptBuilder.buildSystemPrompt();
                String userPrompt = PromptBuilder.buildUserPrompt();
                String aiResponse = LocalAIClient.sendPrompt(userPrompt, systemPrompt);
                long end = System.currentTimeMillis();
                System.out.println("AI süresi: " + (end - start) + " ms");
                System.out.println("Generated AI Schedule:\n" + aiResponse);
            } catch (Exception e) {
                System.out.println("AI error: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
