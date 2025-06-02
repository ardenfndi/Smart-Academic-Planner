package models;

import java.util.ArrayList;
import java.util.List;

public class CourseStorage {
    private static final List<Course> courses = new ArrayList<>();

    public static void addCourse(Course course) {
        courses.add(course);
    }

    public static void removeCourse(int index) {
        if (index >= 0 && index < courses.size()) {
            courses.remove(index);
        }
    }

    public static List<Course> getCourses() {
        return courses;
    }

    public static void clearCourses() {
        courses.clear();
    }
}
