package ai;

import models.Course;
import models.CourseStorage;

public class PromptBuilder {

    public static String buildSystemPrompt() {
        return """
        You are a university course schedule optimizer AI.
        Your job is to create a schedule by selecting ONE time slot for EACH course from the given options,
        such that no selected time slots overlap.

        Instructions:
        - Select exactly one time slot for each course.
        - Avoid any time conflicts between selected slots.
        - If it's not possible to schedule all courses conflict-free, schedule as many as possible without overlap.
        - Clearly list the selected courses and their scheduled time slots.
        - ONLY select one time slot per course.
        - OUTPUT the schedule in bullet points exactly as requested.
        - DO NOT list all time slots.

        Format your output like:
        Selected Schedule:
        - Course Name → Day HH:MM–HH:MM
        - Another Course → Day HH:MM–HH:MM
        """;
    }

    public static String buildUserPrompt() {
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the available courses and their possible time slots:\n");

        for (Course course : CourseStorage.getCourses()) {
            sb.append("- ").append(course.getName()).append(" → ");
            for (int i = 0; i < course.getTimeSlots().size(); i++) {
                sb.append(course.getTimeSlots().get(i));
                if (i < course.getTimeSlots().size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
