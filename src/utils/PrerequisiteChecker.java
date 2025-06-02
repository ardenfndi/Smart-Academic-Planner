package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PrerequisiteChecker {

    public static boolean hasPrerequisites(String courseCode, String studentId) {
        try (Connection conn = DBHelper.getConnection()) {
            String sql = "SELECT prerequisite_course_id FROM Prerequisites p " +
                         "JOIN Courses c ON p.prerequisite_course_id = c.course_id " +
                         "WHERE p.course_id = (SELECT course_id FROM Courses WHERE course_code = ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, courseCode);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int prereqCourseId = rs.getInt("prerequisite_course_id");
                String prereqCourseCode = getCourseCodeById(prereqCourseId);

                boolean passedPrereq = checkIfStudentPassed(studentId, prereqCourseCode);
                if (!passedPrereq) {
                    return false; 
                }
            }
            return true; 
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getPrerequisiteNames(String courseCode) {
        List<String> prereqs = new ArrayList<>();
        try (Connection conn = DBHelper.getConnection()) {
            String sql = "SELECT c2.course_code FROM Prerequisites p " +
                         "JOIN Courses c1 ON p.course_id = c1.course_id " +
                         "JOIN Courses c2 ON p.prerequisite_course_id = c2.course_id " +
                         "WHERE c1.course_code = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, courseCode);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                prereqs.add(rs.getString("course_code"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.join(", ", prereqs);
    }

    private static String getCourseCodeById(int courseId) {
        try (Connection conn = DBHelper.getConnection()) {
            String sql = "SELECT course_code FROM Courses WHERE course_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("course_code");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static boolean checkIfStudentPassed(String studentId, String courseCode) {
        try (Connection conn = DBHelper.getConnection()) {
            String sql = "SELECT * FROM StudentCourses sc " +
                         "JOIN Courses c ON sc.course_id = c.course_id " +
                         "WHERE sc.student_id = ? AND c.course_code = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, studentId);
            ps.setString(2, courseCode);
            ResultSet rs = ps.executeQuery();
            return rs.next(); 
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
