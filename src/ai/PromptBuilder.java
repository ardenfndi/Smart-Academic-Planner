package ai;

import models.Course;
import models.CourseStorage;

public class PromptBuilder {

    public static String buildSystemPrompt() {
        return """
You are a course scheduling AI.

Some courses have multiple time slot options using suffixes (e.g., CS105, CS105.1, CS105.2).
These are different time options for the SAME course.
Select ONLY ONE time slot per base course (e.g., only one of CS105, CS105.1, etc.)

Rules:
- Select exactly one time slot per course
- Avoid all time conflicts
- Do not list skipped variants
- Include all non-conflicting courses
- Skip only if ALL time options conflict

Valid course codes include:
CS105, CS204, CS203, CS306, CS302, CS307, CS301, CS406, CS405, CS304, CS310, CS491, CS492, CS325, CS308, CS311, 
SE201, SE202, SE301, SE308, SE309, ELIT100, ELIT200, ELIT300, MATH100, MATH101, MATH203, MATH204, IE408

Course Code Group Examples:
- CS105: CS105, CS105.1, CS105.2
- CS306: CS306, CS306.1, CS306.2
- CS304: CS304, CS304.1, CS304.2
- ELIT100: ELIT100, ELIT100.1
- MATH204: MATH209, MATH209.1, MATH209.2


Output format:
Selected Schedule:
- Course Name → Day HH:MM – HH:MM
- Course Name → Day HH:MM – HH:MM
- Course Name → Day HH:MM – HH:MM
- Course Name → Day HH:MM – HH:MM
- Course Name → Day HH:MM – HH:MM
""";
    }

    public static String buildUserPrompt() {
        StringBuilder sb = new StringBuilder();
        sb.append("Courses:\n");

        for (Course course : CourseStorage.getCourses()) {
            for (String slot : course.getTimeSlots()) {
                sb.append(course.getName()).append(" → ").append(slot).append("\n");
            }
        }

        return sb.toString();
    }
}
