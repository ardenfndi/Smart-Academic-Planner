package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import utils.DBHelper;
import utils.PrerequisiteChecker;

public class StudentCourseController {

    public static boolean addStudentCourse(String studentId, String courseCode) {
        try (Connection conn = DBHelper.getConnection()) {

            String sqlCourse = "SELECT course_id FROM Courses WHERE course_code = ?";
            PreparedStatement stmtCourse = conn.prepareStatement(sqlCourse);
            stmtCourse.setString(1, courseCode);
            ResultSet rs = stmtCourse.executeQuery();

            if (!rs.next()) {
                System.out.println("Course not found!");
                return false;
            }
            int courseId = rs.getInt("course_id");
            rs.close();
            stmtCourse.close();

            if (!PrerequisiteChecker.hasPrerequisites(courseCode, studentId)) {
                System.out.println("Prerequisite not passed.");
                return false;
            }

            String sqlCheck = "SELECT COUNT(*) FROM StudentCourses WHERE student_id = ? AND course_id = ?";
            PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck);
            stmtCheck.setString(1, studentId);
            stmtCheck.setInt(2, courseId);
            ResultSet rsCheck = stmtCheck.executeQuery();
            if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                System.out.println("Course already added for this student.");
                return false;
            }
            rsCheck.close();
            stmtCheck.close();

            String sqlInsert = "INSERT INTO StudentCourses (student_id, course_id) VALUES (?, ?)";
            PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert);
            stmtInsert.setString(1, studentId);
            stmtInsert.setInt(2, courseId);
            stmtInsert.executeUpdate();
            stmtInsert.close();

            System.out.println("Course added successfully.");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
