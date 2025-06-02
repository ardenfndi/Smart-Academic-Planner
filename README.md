# Smart Academic Planner & Course Clash Detector 🧠📅

This project is a JavaFX-based desktop application designed to help university students generate conflict-free course schedules, with built-in prerequisite validation and optional AI support.

## 🚀 Features

- ✅ Add courses with custom days and time slots  
- ✅ Automatic detection of time conflicts  
- ✅ Prerequisite validation using MySQL  
- ✅ Weekly schedule table visualization  
- ✅ Optional AI integration using LM Studio (e.g., MythoMax)  
- ✅ Fallback backtracking algorithm if AI is unavailable   

## 🧠 AI Integration (Optional)

If LM Studio is running locally with a model like MythoMax, the app sends a natural language prompt describing the user's selected courses and possible time slots.  
The AI responds with a human-readable schedule suggestion.

If LM Studio is not running, the app automatically switches to a backtracking algorithm to generate the best possible non-conflicting schedule.

## 🛠️ Technologies Used

- Java 17  
- JavaFX  
- MySQL  
- JDBC  
- LM Studio / MythoMax (optional)  
- MVC Architecture  

## 🗄️ Database Overview

- `courses(course_id, course_code, course_name)`  
- `prerequisites(course_id, prerequisite_course_id)`  
- `studentcourses(student_id, course_id)`  
- `usercourses(user_course_id, course_code, course_name, day, start_time, end_time)`  

## 📄 Project Report

The complete technical and academic report is included in the repository:  
📎 `Smart_Academic_Planner_Report.docx`

## 👨‍💻 Authors

- **Ethem Kesim** – 210302205  
- **Emre Çubuk** – 220302393  
- **Ozan Umut Güney** – 200302109  

## 🧪 How to Run

1. Clone the repo  
2. Ensure JavaFX and MySQL Connector JARs are in your classpath  
3. Configure the DB connection in `DBHelper.java`  
4. Run `AppMain.java`

--- 

