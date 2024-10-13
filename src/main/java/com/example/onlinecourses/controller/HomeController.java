package com.example.onlinecourses.controller;

import com.example.onlinecourses.model.Course;
import com.example.onlinecourses.model.Student;
import com.example.onlinecourses.model.Enrollment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("Spring Boot Basics", "Learn the basics of Spring Boot", 10));
        courses.add(new Course("React Native", "Build mobile apps using React Native", 8));

        List<Student> students = new ArrayList<>();
        students.add(new Student("John Doe", 22));
        students.add(new Student("Jane Doe", 21));

        List<Enrollment> enrollments = new ArrayList<>();
        enrollments.add(new Enrollment(students.get(0), courses.get(0)));
        enrollments.add(new Enrollment(students.get(1), courses.get(1)));

        model.addAttribute("courses", courses);
        model.addAttribute("students", students);
        model.addAttribute("enrollments", enrollments);

        return "index"; // Looks for 'index.html' in 'src/main/resources/templates'
    }
}
