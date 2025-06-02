package models;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String name;
    private List<String> timeSlots;

    public Course(String name, String day, String timeSlot) {
        this.name = name;
        this.timeSlots = new ArrayList<>();
        this.timeSlots.add(day + " " + timeSlot); 
    }

    public Course(String name, List<String> timeSlots) {
        this.name = name;
        this.timeSlots = timeSlots;
    }

    public String getName() {
        return name;
    }

    public List<String> getTimeSlots() {
        return timeSlots;
    }

    public void addTimeSlot(String timeSlot) {
        timeSlots.add(timeSlot);
    }

    @Override
    public String toString() {
        return name + " " + String.join(", ", timeSlots);
    }
}
