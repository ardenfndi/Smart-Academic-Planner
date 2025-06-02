package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Course;
import models.CourseStorage;
import utils.PrerequisiteChecker;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class CourseSelectionController {

    @FXML private TextField courseNameField;
    @FXML private ComboBox<String> dayComboBox;
    @FXML private Spinner<Integer> startHour, startMinute, endHour, endMinute;
    @FXML private ListView<String> courseListView;

    @FXML
    public void initialize() {
        dayComboBox.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");

        startHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 8));
        startMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 10));
        endHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 10));
        endMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 10));
    }

    @FXML
    void addCourse() {
        String courseCode = courseNameField.getText().trim();
        String day = dayComboBox.getValue();

        if (courseCode.isEmpty() || day == null) {
            showAlert("Please fill all fields.");
            return;
        }

        if (!PrerequisiteChecker.hasPrerequisites(courseCode, "")) {
            String prereqs = PrerequisiteChecker.getPrerequisiteNames(courseCode);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Prerequisite Required");
            alert.setHeaderText(null);
            alert.setContentText("This course requires prerequisite(s): " + prereqs + 
                                 ". Have you completed them?");

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(yesButton, noButton);

            var result = alert.showAndWait();
            if (result.isEmpty() || result.get() == noButton) {
                showAlert("You must complete the prerequisite courses before adding this one.");
                return; 
            }
        }

        String time = String.format("%02d:%02d-%02d:%02d",
                startHour.getValue(), startMinute.getValue(),
                endHour.getValue(), endMinute.getValue());

        Course course = new Course(courseCode, day, time);
        CourseStorage.addCourse(course);
        courseListView.getItems().add(course.toString());

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/smart_academic_planner", "root", "12345")) {

            String sql = "INSERT INTO UserCourses (course_code, course_name, day, start_time, end_time) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, courseCode);
            pstmt.setString(2, courseCode);
            pstmt.setString(3, day);
            pstmt.setTime(4, java.sql.Time.valueOf(String.format("%02d:%02d:00", startHour.getValue(), startMinute.getValue())));
            pstmt.setTime(5, java.sql.Time.valueOf(String.format("%02d:%02d:00", endHour.getValue(), endMinute.getValue())));
            pstmt.executeUpdate();

            pstmt.close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Can not add course to MySql.");
        }

        courseNameField.clear();
        dayComboBox.getSelectionModel().clearSelection();

        startHour.getValueFactory().setValue(8);
        startMinute.getValueFactory().setValue(0);
        endHour.getValueFactory().setValue(10);
        endMinute.getValueFactory().setValue(0);
    }

    @FXML
    void removeCourse() {
        int selectedIndex = courseListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            courseListView.getItems().remove(selectedIndex);
            CourseStorage.getCourses().remove(selectedIndex);
        } else {
            showAlert("Please select a course to remove.");
        }
    }

    @FXML
    void continueToSchedule() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/ScheduleView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Generated Schedule");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) courseNameField.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Failed to load schedule view.");
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
